package org.springblade.modules.data.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springblade.common.constant.ServiceConstant;
import org.springblade.common.entity.tx.AreaStayDayEntity;
import org.springblade.common.entity.tx.HeatingValue;
import org.springblade.common.entity.tx.MigrationEntity;
import org.springblade.common.entity.tx.MigrationProvincesEntity;
import org.springblade.common.entity.tx.analysis.AnalysisEntity;
import org.springblade.common.entity.tx.analysis.AnalysisOriginEntity;
import org.springblade.common.entity.tx.analysis.AnalysisPopulationEntity;
import org.springblade.common.entity.tx.analysisNew.AnalysisNewEntity;
import org.springblade.common.entity.tx.analysisNew.AnalysisOriginNewEntity;
import org.springblade.common.mapper.analysis.AnalysisMapper;
import org.springblade.common.mapper.analysis.AnalysisOriginMapper;
import org.springblade.common.mapper.analysis.AnalysisPopulationMapper;
import org.springblade.common.mapper.analysisNew.AnalysisNewMapper;
import org.springblade.common.mapper.analysisNew.AnalysisOriginNewMapper;
import org.springblade.common.mapper.tx.AreaStayDayMapper;
import org.springblade.common.mapper.tx.HeatingValueMapper;
import org.springblade.common.mapper.tx.MigrationMapper;
import org.springblade.common.mapper.tx.MigrationProvincesMapper;
import org.springblade.common.utils.AreaCodeUtils;
import org.springblade.common.utils.CommonUtil;
import org.springblade.common.utils.InterfaceLogUtils;
import org.springblade.common.vo.HeatingValueVO;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Author yq
 * @Date 2020/9/17 19:50
 */

@Component
@Slf4j
@ConditionalOnProperty(prefix = "scheduling", name = "enabled", havingValue = "true")
public class TxData {
	@Resource
	private HeatingValueMapper heatingValueMapper;
	@Resource
	private AreaStayDayMapper areaStayDayMapper;
	@Resource
	private AnalysisMapper analysisMapper;
	@Resource
	private AnalysisOriginMapper analysisOriginMapper;
	@Resource
	private AnalysisPopulationMapper analysisPopulationMapper;
	@Resource
	private AnalysisNewMapper analysisNewMapper;
	@Resource
	private AnalysisOriginNewMapper analysisOriginNewMapper;
	@Resource
	private MigrationMapper migrationMapper;
	@Resource
	private MigrationProvincesMapper migrationProvincesMapper;
	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 热力值任务（5分钟拉取一次数据）
	 * 要跑
	 */
	@Scheduled(cron = "0 0/5 * * * ?")
	public void heatingValueTask() {
		String url = "区划热力值 5分钟";
		//redis中的key名
		String redisName = "heatingValue";
		//封装参数
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("key", ServiceConstant.TX_KEY);
		paramMap.put("adcode", ServiceConstant.TX_SAN_YA_CODE);
		String param = JSON.toJSONString(paramMap);
		//发送请求
		String result = CommonUtil.post(ServiceConstant.TX_HEATING_URL, param);
		//返回结果
		JSONObject json = JSON.parseObject(result);
		//成功 插入数据库
		if ("0".equals(json.getString("status"))) {
			LocalDateTime startTime = null;
			String data = json.getString("data");
			List<HeatingValue> valueList = JSONArray.parseArray(data, HeatingValue.class);
			for (int i = 0; i < valueList.size(); i++) {
				LocalDateTime time = LocalDateTime.now();
				if (i == 0) {
					startTime = time;
				}
				//坐标转换，腾讯地图转换成百度地图坐标
				Map<String, Object> map = TxData.mapTxToBd(valueList.get(i).getLat(), valueList.get(i).getLng());
				HeatingValue heatingValue = valueList.get(i);
				heatingValue.setCreateTime(time);
				Double lon = (Double) map.get("lon");
				heatingValue.setLng(lon);
				Double lat = (Double) map.get("lat");
				heatingValue.setLat(lat);
				heatingValueMapper.insert(heatingValue);
			}
			List<HeatingValueVO> heatingValueVOList = new ArrayList<>();
			for (HeatingValue heatingValue : valueList) {
				List<Double> list = new ArrayList<>();
				HeatingValueVO heatingValueVO = new HeatingValueVO();
				//坐标转换，腾讯地图转换成百度地图坐标
				Map<String, Object> map = TxData.mapTxToBd(heatingValue.getLat(), heatingValue.getLng());
				Double lon = (Double) map.get("lon");
				list.add(lon);
				Double lat = (Double) map.get("lat");
				list.add(lat);
				heatingValueVO.setCoord(list);
				heatingValueVO.setElevation(heatingValue.getCount());
				heatingValueVOList.add(heatingValueVO);
			}
			if (redisTemplate.hasKey(redisName)) {
				redisTemplate.delete(redisName);
			}
			redisTemplate.opsForList().rightPushAll(redisName, heatingValueVOList);
			//插入数据库成功  并且缓存也存好了  删除5分钟以前的数据
			if (!StringUtils.isEmpty(startTime)) {
				heatingValueMapper.deleteFiveMinute(startTime);
			}
		} else {
			InterfaceLogUtils.saveLog(url, ServiceConstant.TX_SAN_YA_CODE, "失败");
		}
	}

	/**
	 * 坐标转换，腾讯地图转换成百度地图坐标
	 *
	 * @param lat 腾讯纬度
	 * @param lon 腾讯经度
	 * @return 返回结果：经度,纬度
	 */
	public static Map<String, Object> mapTxToBd(double lat, double lon) {
		double bdLat;
		double bdLon;
		double x_pi = 3.14159265358979324;
		double x = lon, y = lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
		bdLon = z * Math.cos(theta) + 0.0065;
		bdLat = z * Math.sin(theta) + 0.006;
		Map<String, Object> map = new HashMap<>(16);
		map.put("lon", bdLon);
		map.put("lat", bdLat);
		return map;
	}


	/**
	 * 游客出访分析
	 * 每天中午12点
	 */
	@Scheduled(cron = "0 0 12 * * ?")
	public void visit() {
		zoningMap.forEach((k, v) -> {
			String strData = LocalDate.now().plusDays(-1).toString();
			HashMap<String, Object> paramMap = new HashMap<>(16);
			paramMap.put("date", strData);
			paramMap.put("type", "0");
			paramMap.put("adcode", v);
			paramMap.put("rid_type", "20");
			paramMap.put("key", ServiceConstant.TX_KEY);
			String param = JSON.toJSONString(paramMap);
			String result = CommonUtil.post(ServiceConstant.TX_ANALYSIS_URL, param);
			JSONObject json = JSON.parseObject(result);
			saveVisit(json, v);
		});
	}

	/**
	 * 保存出访分析人数
	 *
	 * @param json     数据
	 * @param typeCode 区划id
	 */
	public void saveVisit(JSONObject json, String typeCode) {
		if ("0".equals(json.getString("status"))) {
			String data = json.getString("data");
			JSONObject jsonObject = JSON.parseObject(data);
			//全国市级别来源地
			AnalysisPopulationEntity entity = new AnalysisPopulationEntity();
			JSONArray population = jsonObject.getJSONArray("population");
			if (population.size() > 0) {
				for (Object o : population) {
					JSONObject keys = (JSONObject) o;
					//人数
					String property = keys.getString("property");
					//值
					String number = keys.getString("number");
					entity.setProperty(property);
					entity.setNumber(number);
					entity.setTypeCode(Integer.valueOf(parseTypeCode(typeCode)));
					entity.setCreateTime(LocalDateTime.now().plusDays(-1));
					analysisPopulationMapper.insert(entity);
				}
			}else {
				InterfaceLogUtils.saveLog("游客出访分析", typeCode, "接口无数据");
			}
		} else {
			InterfaceLogUtils.saveLog("游客出访分析", typeCode, "请求接口失败");
		}
	}

	/**
	 * 区划访客/出访分析（区划画像）
	 * 每天中午12点
	 */
	@Scheduled(cron = "0 40 11 * * ?")
	public void analysisTask() {
		zoningMap.forEach((k, v) -> {
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < 20; i++) {
				stringBuilder.append(i).append(",");
			}
			String types = stringBuilder.substring(0, stringBuilder.length() - 1);
			//封装请求参数 三亚市
			String localDate = LocalDate.now().plusDays(-1).toString();
			HashMap<String, Object> paramMap = new HashMap<>(16);
			paramMap.put("date", localDate);
			paramMap.put("adcode", v);
			paramMap.put("type", types);
			paramMap.put("rid_type", "10");
			paramMap.put("key", ServiceConstant.TX_KEY);
			String result = JSON.toJSONString(paramMap);
			String requestResult = CommonUtil.post(ServiceConstant.TX_ANALYSIS_URL, result);
			JSONObject jsonObject = JSON.parseObject(requestResult);
			saveZoneImage(jsonObject, v);
		});
	}

	/**
	 * 保存区划画像
	 *
	 * @param json     json对象
	 * @param typeCode 区划id
	 */
	public void saveZoneImage(JSONObject json, String typeCode) {
		if ("0".equals(json.getString("status"))) {
			LocalDateTime localDateTime = LocalDateTime.now().plusDays(-1);
			String data = json.getString("data");
			JSONObject jsonObject = JSON.parseObject(data);
			Set<String> keySet = jsonObject.keySet();
			for (String key : keySet) {
				// 没有数据不处理
				String value = jsonObject.getString(key);
				if ("[]".equals(value)) {
					continue;
				}
				String type = dataMap.get(key);
				if (StringUtil.isNotBlank(type)) {
					//全国市级别来源地/出访地
					if ("origin".equals(type)) {
						List<AnalysisOriginEntity> originList = JSONArray.parseArray(value, AnalysisOriginEntity.class);
						for (AnalysisOriginEntity originEntity : originList) {
							originEntity.setTypeCode(Integer.valueOf(parseTypeCode(typeCode)));
							originEntity.setCreateTime(localDateTime);
						}
						if (CollectionUtils.isNotEmpty(originList)) {
							analysisOriginMapper.saveBatch(originList);
						}
						//画像人数
					} else if ("population".equals(type)) {
						AnalysisEntity entity = new AnalysisEntity();
						JSONArray population = jsonObject.getJSONArray("population");
						for (Object o : population) {
							JSONObject keys = (JSONObject) o;
							//人数
							String property = keys.getString("property");
							//值
							String number = keys.getString("number");
							entity.setProperty(property);
							entity.setPercent(number);
							entity.setType(type);
							entity.setTypeCode(Integer.valueOf(parseTypeCode(typeCode)));
							entity.setCreateTime(localDateTime);
							analysisMapper.insert(entity);
						}
					} else {
						//剩余画像
						List<AnalysisEntity> list = JSONArray.parseArray(value, AnalysisEntity.class);
						for (AnalysisEntity entity : list) {
							entity.setType(type);
							entity.setTypeCode(Integer.valueOf(parseTypeCode(typeCode)));
							entity.setCreateTime(localDateTime);
						}
						if (CollectionUtils.isNotEmpty(list)) {
							analysisMapper.saveBatch(list);
						}
					}
				}
			}
		} else {
			InterfaceLogUtils.saveLog("区划画像", typeCode, "请求接口失败");
		}
	}

	/**
	 * 区划画像类型
	 */
	HashMap<String, String> dataMap = new HashMap<String, String>() {
		{
			//人口
			put("population", "population");
			//年龄
			put("age", "age");
			//性别
			put("gender", "gender");
			//消费能力
			put("consumpting_ability", "consumer");
			//学历
			put("education", "education");
			//是否有车
			put("car", "car");
			//是否理财
			put("finance", "finance");
			//人生阶段
			put("life_stage", "life");
			//购物到访偏好
			put("shopping_visit_interest", "shopping");
			//旅游距离
			put("travel_distance", "distance");
			//全国市级别来源地/出访地
			put("origin", "origin");
			//是否健身
			put("fitness", "fitness");
			//手机类型
			put("mobile_phone", "mobilePhone");
			//差旅常
			put("travel_regular", "regular");
			//旅游目的地-国内
			put("travel_destination_domestic", "domestic");
			//旅游目的地-国外
			put("travel_destination_overseas", "overseas");
			//美食到访偏好
			put("food_visit_interest", "food");
			//娱乐到访偏好
			put("entertain_visit_interest", "entertainment");
			//酒店到访偏好
			put("hotel_visit_interest", "hotel");
		}
	};


	/**
	 * 区划迁徙接口
	 * 每天中午12点
	 */
	@Scheduled(cron = "0 40 11 * * ?")
	public void migration() {
		String url = "区划迁徙接口";
		//封装参数
		HashMap<String, Object> paramMap = new HashMap<>(16);
		LocalDate localDate = LocalDate.now().plusDays(-1);
		paramMap.put("date", localDate);
		paramMap.put("adcode", ServiceConstant.TX_SAN_YA_CODE);
		paramMap.put("key", ServiceConstant.TX_KEY);
		String param = JSON.toJSONString(paramMap);
		//发送请求
		String result = CommonUtil.post(ServiceConstant.TX_MIGRATION_URL, param);
		//返回结果
		JSONObject json = JSON.parseObject(result);
		if ("0".equals(json.getString("status"))) {
			String result1 = json.getString("result");
			JSONObject jsonObject = JSON.parseObject(result1);
			saveMoveIn(jsonObject, localDate);
			saveMoveOut(jsonObject, localDate);
		} else {
			InterfaceLogUtils.saveLog(url, ServiceConstant.TX_SAN_YA_CODE, "请求接口失败");
		}
	}

	/**
	 * 获取迁入信息
	 *
	 * @param jsonObject json对象
	 * @param localDate  日期
	 */
	private void saveMoveIn(JSONObject jsonObject, LocalDate localDate) {
		//获取迁入信息
		String in = jsonObject.getString("in");
		JSONObject inJson = JSON.parseObject(in);
		JSONArray inCity = inJson.getJSONArray("city");
		JSONArray inProvince = inJson.getJSONArray("province");
		String population = inJson.getString("population");
		String traffic = inJson.getString("traffic");
		JSONObject trafficJsonObject = JSON.parseObject(traffic);
		String car = trafficJsonObject.getString("car");
		String plane = trafficJsonObject.getString("plane");
		String train = trafficJsonObject.getString("train");
		//保存迁徒表
		MigrationEntity migrationEntity = new MigrationEntity();
		migrationEntity.setPopulation(population);
		migrationEntity.setCar(car);
		migrationEntity.setPlane(plane);
		migrationEntity.setTrain(train);
		migrationEntity.setCreateTime(localDate);
		migrationEntity.setType("in");
		migrationMapper.insert(migrationEntity);

		List<MigrationProvincesEntity> cityList = new ArrayList<>();
		List<MigrationProvincesEntity> provinceList = new ArrayList<>();
		for (int i = 0; i < inCity.size(); i++) {
			JSONObject inCityJson = (JSONObject) inCity.get(i);
			String cityAdcode = inCityJson.getString("adcode");
			String cityAdcodePopulation = inCityJson.getString("population");
			MigrationProvincesEntity entity = new MigrationProvincesEntity();
			entity.setMigrationId(migrationEntity.getId());
			entity.setAdcode(cityAdcode);
			entity.setPopulation(cityAdcodePopulation);
			entity.setAreaType("city");
			entity.setType("in");
			entity.setCreateTime(localDate);
			cityList.add(entity);
		}
		for (int i = 0; i < inProvince.size(); i++) {
			JSONObject inProvinceJson = (JSONObject) inProvince.get(i);
			String provinceAdcode = inProvinceJson.getString("adcode");
			String provincePopulation = inProvinceJson.getString("population");
			MigrationProvincesEntity entity = new MigrationProvincesEntity();
			entity.setMigrationId(migrationEntity.getId());
			entity.setAdcode(provinceAdcode);
			entity.setPopulation(provincePopulation);
			entity.setAreaType("province");
			entity.setType("in");
			entity.setCreateTime(localDate);
			provinceList.add(entity);
		}
		migrationProvincesMapper.saveBatch(cityList);
		migrationProvincesMapper.saveBatch(provinceList);
	}

	/**
	 * 获取迁出信息
	 *
	 * @param jsonObject json对象
	 * @param localDate  日期
	 */
	private void saveMoveOut(JSONObject jsonObject, LocalDate localDate) {
		//获取迁出信息
		String out = jsonObject.getString("out");
		JSONObject outJson = JSON.parseObject(out);
		JSONArray outCity = outJson.getJSONArray("city");
		JSONArray outProvince = outJson.getJSONArray("province");
		String population = outJson.getString("population");
		String traffic = outJson.getString("traffic");
		JSONObject trafficJsonObject = JSON.parseObject(traffic);
		String car = trafficJsonObject.getString("car");
		String plane = trafficJsonObject.getString("plane");
		String train = trafficJsonObject.getString("train");
		//保存迁徒表
		MigrationEntity migrationEntity = new MigrationEntity();
		migrationEntity.setPopulation(population);
		migrationEntity.setCar(car);
		migrationEntity.setPlane(plane);
		migrationEntity.setTrain(train);
		migrationEntity.setCreateTime(localDate);
		migrationEntity.setType("out");
		migrationMapper.insert(migrationEntity);

		List<MigrationProvincesEntity> cityList = new ArrayList<>();
		List<MigrationProvincesEntity> provinceList = new ArrayList<>();
		for (int i = 0; i < outCity.size(); i++) {
			JSONObject outCityJson = (JSONObject) outCity.get(i);
			String cityAdcode = outCityJson.getString("adcode");
			String cityAdcodePopulation = outCityJson.getString("population");
			MigrationProvincesEntity entity = new MigrationProvincesEntity();
			entity.setMigrationId(migrationEntity.getId());
			entity.setAdcode(cityAdcode);
			entity.setPopulation(cityAdcodePopulation);
			entity.setAreaType("city");
			entity.setType("out");
			entity.setCreateTime(localDate);
			cityList.add(entity);
		}
		for (int i = 0; i < outProvince.size(); i++) {
			JSONObject outProvinceJson = (JSONObject) outProvince.get(i);
			String provinceAdcode = outProvinceJson.getString("adcode");
			String provincePopulation = outProvinceJson.getString("population");
			MigrationProvincesEntity entity = new MigrationProvincesEntity();
			entity.setMigrationId(migrationEntity.getId());
			entity.setAdcode(provinceAdcode);
			entity.setPopulation(provincePopulation);
			entity.setAreaType("province");
			entity.setType("out");
			entity.setCreateTime(localDate);
			provinceList.add(entity);
		}
		migrationProvincesMapper.saveBatch(cityList);
		migrationProvincesMapper.saveBatch(provinceList);
	}


	/**
	 * 区划停留天数
	 */
	@Scheduled(cron = "0 0 22 5 * ?")
	public void areaStayDayTask() {
		LocalDate lastTime = LocalDate.now().minusMonths(1);
		String[] split = lastTime.toString().split("-");
		String yearMouth = split[0] + split[1] + "_3";//202009_3
		zoningMap.forEach((k, v) -> {
			methodMouth(yearMouth, v);
		});
	}

	public void methodMouth(String yearMouth, String typeCode) {
		String url = "区划停留天数";
		//封装请求参数
		HashMap<String, Object> paramMap = new HashMap<>(16);
		paramMap.put("o_adcode", "000000");
		paramMap.put("d_adcode", ServiceConstant.TX_SAN_YA_CODE);
		paramMap.put("month", yearMouth);
		paramMap.put("key", ServiceConstant.TX_KEY);
		String param = JSON.toJSONString(paramMap);

		//发送请求
		String result = CommonUtil.post(ServiceConstant.TX_AREA_STAY_DAY_URL, param);
		//返回结果
		JSONObject json = JSON.parseObject(result);
		if ("0".equals(json.getString("status"))) {
			String data = json.getString("result");
			JSONObject jsonObject = JSON.parseObject(data);
			String value1 = jsonObject.getString("1");
			String value2 = jsonObject.getString("2");
			String value3 = jsonObject.getString("3");
			String value4 = jsonObject.getString("4");
			String value5 = jsonObject.getString("5");
			String value6 = jsonObject.getString("6");
			String value7 = jsonObject.getString("7");
			String value8 = jsonObject.getString("8");
			String value9 = jsonObject.getString("9");
			String value10 = jsonObject.getString("10");
			String value11 = jsonObject.getString("10天以上");
			AreaStayDayEntity build = AreaStayDayEntity.builder().value1(value1).value2(value2).value3(value3).value4(value4).value5(value5)
				.value6(value6).value7(value7).value8(value8).value9(value9).value10(value10).value11(value11).build();
			build.setCreateTime(LocalDate.now().plusMonths(-1));
			build.setTypeCode(parseTypeCode(typeCode));
			areaStayDayMapper.insert(build);
		} else {
			InterfaceLogUtils.saveLog(url, typeCode, "请求接口失败");
		}
	}

	/**
	 * 区划id
	 */
	HashMap<String, String> zoningMap = new HashMap<String, String>() {
		{
			put("三亚市", ServiceConstant.TX_SAN_YA_CODE);
			put("海棠区", ServiceConstant.TX_HAI_TANG_AREA_CODE);
			put("吉阳区", ServiceConstant.TX_JI_YANG_AREA_CODE);
			put("天涯区", ServiceConstant.TX_TIAN_YA_AREA_CODE);
			put("崖州区", ServiceConstant.TX_YA_ZHOU_AREA_CODE);
		}
	};

	private String parseTypeCode(String typeCode) {
		switch (typeCode) {
			case ServiceConstant.TX_SAN_YA_CODE:
				return "0";
			case ServiceConstant.TX_HAI_TANG_AREA_CODE:
				return "1";
			case ServiceConstant.TX_JI_YANG_AREA_CODE:
				return "2";
			case ServiceConstant.TX_TIAN_YA_AREA_CODE:
				return "3";
			case ServiceConstant.TX_YA_ZHOU_AREA_CODE:
				return "4";
			default:
				return typeCode;
		}
	}


	/**
	 * 6.区域实时人口画像，每整点过1分执行，
	 * 例如
	 * 2020-10-06 10:00:01
	 * 2020-10-06 11:00:01
	 */
//	@Scheduled(cron = "0 1 * * * ?")
	@Scheduled(cron = "0 30 * * * ?")
	public void analysisV2Task() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
		String dateNow = formatter.format(new Date());
		LocalDateTime strDate = LocalDateTime.parse(dateNow, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00:00"));
		analysisHashMap.forEach((k, v) -> {
			log.info("请求数据=》" + k);
			getAnalysisData(v, strDate);

		});

	}

	/**
	 * @param strData  抓取的时间，时间到时间减去1小时的（传3点，就抓2点到3点的数据）
	 * @param typeCode 区域ID
	 */
	private void getAnalysisData(String typeCode, LocalDateTime strData) {
		//年龄,性别,是否有车,消费水平,学历,客源地,是否理财
		List<String> imageList = new ArrayList<>();
		typeMap.forEach((k, v) -> {
			imageList.add(v);
		});
		//购物分析和人生阶段
		List<String> financeList = new ArrayList<>();
		financeMap.forEach((k, v) -> {
			financeList.add(v);
		});
		//请求的类型
		String imageTypes = String.join(",", imageList);
		String shoppingAndLifeTypes = String.join(",", financeList);

		//开始时间和结束时间
		String begin = Long.toString(DateUtil.toMilliseconds(strData.minusHours(1)));
		begin = begin.substring(0, 10);
		String end = Long.toString(DateUtil.toMilliseconds(strData));
		end = end.substring(0, 10);

		//发送请求
		String imageResult = postRequest(typeCode, begin, end, imageTypes);
		String financeResult = postRequest(typeCode, begin, end, shoppingAndLifeTypes);

		JSONObject imageJsonObject = JSON.parseObject(imageResult);
		JSONObject financeJsonObject = JSON.parseObject(financeResult);
		// 保存到mysql
		saveFiveNew(imageJsonObject, typeCode);
		saveFinance(financeJsonObject, typeCode);
	}

	/**
	 * 封装请求参数
	 *
	 * @param typeCode 区域id
	 * @param begin    开始时间
	 * @param end      结束时间
	 * @param types    类型数组
	 * @return 返回结果
	 */
	private String postRequest(String typeCode, String begin, String end, String types) {
		// 年龄,性别,是否有车,消费水平,学历,客源地,是否理财 参数封装
		HashMap<String, Object> param = new HashMap<>(16);
		param.put("id", typeCode);
		param.put("begin", Integer.parseInt(begin));
		param.put("end", Integer.parseInt(end));
		param.put("interval", 60);
		param.put("type", types);
		param.put("key", ServiceConstant.TX_KEY);
		String post = JSON.toJSONString(param);
		return CommonUtil.post(ServiceConstant.TX_AREA_IMAGE, post);
	}

	/**
	 * 保存理财画像
	 *
	 * @param json     返回数据
	 * @param typeCode 区域id
	 */
	private void saveFinance(JSONObject json, String typeCode) {
		if ("0".equals(json.getString("status"))) {
			JSONArray data = json.getJSONArray("data");
			for (Object datum : data) {
				JSONObject jsonObject = (JSONObject) datum;
				// 拿接口里面的时间，这个是数据生成的时间，而不是抓取的时间
				LocalDateTime localDate = LocalDateTime.parse(jsonObject.getString("time"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00:00"));
				Set<String> keySet = jsonObject.keySet();
				for (String key : keySet) {
					if ("time".equals(key)) {
						continue;
					}
					// 没得数据不处理
					String value = jsonObject.getString(key);
					if ("[]".equals(value)) {
						continue;
					}
					String typeTemp = tagHashMap.get(key);
					if (StringUtil.isNotBlank(typeTemp)) {
						List<AnalysisNewEntity> analysisEntities = JSONArray.parseArray(value, AnalysisNewEntity.class);
						for (AnalysisNewEntity analysisEntity : analysisEntities) {
							analysisEntity.setType(typeTemp);
							analysisEntity.setTypeCode(typeCode);
							analysisEntity.setCreateTime(localDate);
						}
						analysisNewMapper.saveBatch(analysisEntities);
					}
				}
			}

		} else {
			InterfaceLogUtils.saveLog("区域理财画像", typeCode, "请求接口失败");
		}
	}


	/**
	 * 保存年龄,性别,是否有车,消费水平,学历,客源地,购物分析，人生阶段 画像
	 *
	 * @param json     返回数据
	 * @param typeCode 区域id
	 */
	public void saveFiveNew(JSONObject json, String typeCode) {
		if ("0".equals(json.getString("status"))) {
			// 不晓得为什么要用数组返回
			JSONArray data = json.getJSONArray("data");
			for (Object datum : data) {
				JSONObject jsonObject = (JSONObject) datum;
				// 拿接口里面的时间，这个是数据生成的时间，而不是抓取的时间
				LocalDateTime localDate = LocalDateTime.parse(jsonObject.getString("time"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00:00"));

				Set<String> keySet = jsonObject.keySet();
				for (String key : keySet) {
					if ("time".equals(key)) {
						continue;
					}
					// 没得数据不处理
					String value = jsonObject.getString(key);
					if ("[]".equals(value)) {
						continue;
					}
					//年龄
					String typeTemp = tagHashMap.get(key);
					if (StringUtil.isNotBlank(typeTemp)) {
						// 有两个数据是另外的表的
						if ("origin".equals(typeTemp)) {
							List<AnalysisOriginNewEntity> analysisOriginEntities = JSONArray.parseArray(value, AnalysisOriginNewEntity.class);
							for (AnalysisOriginNewEntity analysisOriginEntity : analysisOriginEntities) {
								analysisOriginEntity.setTypeCode(typeCode);
								analysisOriginEntity.setCreateTime(localDate);
							}
							String tableName = AreaCodeUtils.originMap().get(typeCode);
							analysisOriginNewMapper.saveBatch(tableName,analysisOriginEntities);
						} else {
							List<AnalysisNewEntity> analysisEntities = JSONArray.parseArray(value, AnalysisNewEntity.class);
							for (AnalysisNewEntity analysisEntity : analysisEntities) {
								analysisEntity.setType(typeTemp);
								analysisEntity.setTypeCode(typeCode);
								analysisEntity.setCreateTime(localDate);
							}
							analysisNewMapper.saveBatch(analysisEntities);
						}
					}
				}
			}
		} else {
			InterfaceLogUtils.saveLog("区域其余画像", typeCode, "失败");
		}

	}

	/**
	 * 6.区域实时人口画像接口中文英文标签键值对
	 */

	HashMap<String, String> tagHashMap = new HashMap<String, String>() {
		{
			// 记录的数据表
			put("家乡地", "origin"); // 1015

			put("年龄（分段）", "age"); // 101012
			put("性别", "gender"); // 101010
			put("消费水平（分段）", "consumer"); // consumer  1112
			put("学历", "education"); // 101015
			put("是否有车", "car"); // 1110
			put("finance", "finance"); // 9 finance
			put("到访偏好-购物", "shopping"); // 1413
			put("人生阶段", "life"); // 1013
		}
	};
//	HashMap<String, String> tagHashMap = new HashMap<String, String>() {
//		{
//			// 记录的数据表
//			// tx_analysis_new_origin
//			put("家乡地", "origin"); // 1015
//
//
//			// tx_analysis_new
//			put("年龄（分段）", "age"); // 101012
//			put("性别", "gender"); // 101010
//			put("消费水平（分段）", "consumer"); // consumer  1112 原接口 consumpting_ability
//			put("学历", "education"); // 101015
//			put("是否有车", "car"); // 1110
//			put("购物", "shopping"); // 1413 shopping
//			put("人生阶段", "life"); // 1413 shopping
//		}
//	};


	/**
	 * 请求画像类型参数
	 */
//	HashMap<String, String> typeMap = new HashMap<String, String>() {
//		{
//			put("年龄", "1");
//			put("性别", "2");
//			put("是否有车", "7");
//			put("消费水平", "5");
//			put("学历", "6");
//			put("家乡地省份", "4");
//			put("是否理财", "9");
//		}
//	};
	static HashMap<String, String> typeMap = new HashMap<String, String>() {
		{
			put("年龄", "101012");
			put("性别", "101010");
			put("是否有车", "1110");
			put("消费水平", "1112");
			put("学历", "101015");
			put("家乡地省份", "1015");
			put("到访偏好-购物", "1413");
			put("人生阶段", "1013");
		}
	};

	HashMap<String, String> financeMap = new HashMap<String, String>() {
		{
			put("是否理财", "9");
		}
	};

	/**
	 * 区域ID列表
	 */
	HashMap<String, String> analysisHashMap = new HashMap<String, String>() {
		{
			put("崖州古城", "4069462959793577915");
			put("【5A】大小洞天景区", "4069461880929738304");
			put("【4A】海南玫瑰谷", "4069283472388272897");
			put("【4A】鹿回头风景区", "4069281244018616740");
			put("海昌梦幻海洋不夜城", "4069846805240994142");
			put("【4A】三亚千古情", "4069470668819008983");
			put("【4A】大东海景区", "4069282708340861593");
			put("【4A】热带天堂森林公园", "4069283618460972764");
			put("【4A】西岛旅游区", "4069280573219172332");
			put("【5A】海南南山景区", "4069462018333514283");
			put("蜈支洲景区", "4069846942546510768");
			put("【4A】天涯海角", "4069468482687799490");

			put("迎宾路商圈", "4069283004616630384");
			put("海棠湾商圈", "4069847118364191869");
			put("大东海商圈", "4069282712636644483");
			put("商品街商圈", "4069282931181451953");
			put("解放路步行街商圈", "4069281457871158129");
			put("免税城商圈", "4069847934674639643");
			put("亚龙湾商圈", "4069283497961413502");
			put("三亚湾商圈", "4069469015674903436");
		}
	};



}
