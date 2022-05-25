package org.springblade.modules.data.config.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springblade.common.constant.ServiceConstant;
import org.springblade.common.entity.tx.MigrationEntity;
import org.springblade.common.entity.tx.MigrationProvincesEntity;
import org.springblade.common.entity.tx.analysis.AnalysisEntity;
import org.springblade.common.entity.tx.analysis.AnalysisOriginEntity;
import org.springblade.common.entity.tx.analysis.AnalysisPopulationEntity;
import org.springblade.common.mapper.analysis.AnalysisMapper;
import org.springblade.common.mapper.analysis.AnalysisOriginMapper;
import org.springblade.common.mapper.analysis.AnalysisPopulationMapper;
import org.springblade.common.mapper.tx.MigrationMapper;
import org.springblade.common.mapper.tx.MigrationProvincesMapper;
import org.springblade.common.utils.CommonUtil;
import org.springblade.common.utils.InterfaceLogUtils;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @Author yq
 * @Date 2021/2/1 9:38
 */
@Slf4j
@RestController
@RequestMapping("/tx")
public class AddTxController {
	@Resource
	private AnalysisPopulationMapper analysisPopulationMapper;
	@Resource
	private AnalysisMapper analysisMapper;
	@Resource
	private AnalysisOriginMapper analysisOriginMapper;
	@Resource
	private MigrationMapper migrationMapper;
	@Resource
	private MigrationProvincesMapper migrationProvincesMapper;


	/**
	 * 区划画像、客源地
	 */
	@GetMapping("/analysisTask")
	public void analysisTask() {
		LocalDate startTime = LocalDate.of(2022, 1, 28);
		int count = 0;
		for (int i = 0; i < 54; i++) {
			int finalCount = count;
			zoningMap.forEach((k, v) -> {
				StringBuilder stringBuilder = new StringBuilder();
				for (int j = 0; j < 20; j++) {
					stringBuilder.append(j).append(",");
				}
				String types = stringBuilder.substring(0, stringBuilder.length() - 1);
				//封装请求参数 三亚市
				HashMap<String, Object> paramMap = new HashMap<>(16);
//			paramMap.put("date", date);
				String date = startTime.plusDays(finalCount).toString();
				paramMap.put("date", date);
				paramMap.put("adcode", v);
				paramMap.put("type", types);
				paramMap.put("rid_type", "10");
				paramMap.put("key", ServiceConstant.TX_KEY);
				String result = JSON.toJSONString(paramMap);
				String requestResult = CommonUtil.post(ServiceConstant.TX_ANALYSIS_URL, result);
				JSONObject jsonObject = JSON.parseObject(requestResult);
				saveZoneImage(jsonObject, v, date);
			});
			count++;
		}

	}


	public void saveZoneImage(JSONObject json, String typeCode, String date) {
		if ("0".equals(json.getString("status"))) {
//			LocalDateTime localDateTime = LocalDateTime.now().plusDays(-1);
//			LocalDateTime localDateTime = LocalDateTime.of(2021, 9, 7, 11, 40, 0);
			LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			LocalDateTime localDateTime = localDate.atStartOfDay();
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
			InterfaceLogUtils.saveLog("区划画像", typeCode, "失败");
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
	 * 常住人口出访
	 */
	@GetMapping("/visit")
	public void visit(String date) {
		zoningMap.forEach((k, v) -> {
			HashMap<String, Object> paramMap = new HashMap<>(16);
			paramMap.put("date", date);
			paramMap.put("type", "0");
			paramMap.put("adcode", v);
			paramMap.put("rid_type", "20");
			paramMap.put("key", ServiceConstant.TX_KEY);
			String param = JSON.toJSONString(paramMap);
			String result = CommonUtil.post(ServiceConstant.TX_ANALYSIS_URL, param);
			JSONObject json = JSON.parseObject(result);
			saveVisit(json, v, date);
		});
	}

	/**
	 * 保存出访分析人数
	 *
	 * @param json     数据
	 * @param typeCode 区划id
	 */
	public void saveVisit(JSONObject json, String typeCode, String date) {
		if ("0".equals(json.getString("status"))) {
			String data = json.getString("data");
			JSONObject jsonObject = JSON.parseObject(data);
			//全国市级别来源地
			AnalysisPopulationEntity entity = new AnalysisPopulationEntity();
			JSONArray population = jsonObject.getJSONArray("population");
			for (Object o : population) {
				JSONObject keys = (JSONObject) o;
				//人数
				String property = keys.getString("property");
				//值
				String number = keys.getString("number");
				entity.setProperty(property);
				entity.setNumber(number);
				entity.setTypeCode(Integer.valueOf(parseTypeCode(typeCode)));
				LocalDate localDate = LocalDate.parse(date);
				LocalDateTime localDateTime = localDate.atTime(LocalTime.of(12, 0, 0));
				entity.setCreateTime(localDateTime);
				analysisPopulationMapper.insert(entity);
			}
		} else {
			InterfaceLogUtils.saveLog("游客出访分析", typeCode, "失败");
		}
	}

	/**
	 * 区划迁徙接口
	 */
	@GetMapping("/migration")
	public void migration(String date) {
		String url = "区划迁徙接口";
		//封装参数
		HashMap<String, Object> paramMap = new HashMap<>(16);
//		LocalDate localDate = LocalDate.now().plusDays(-1);
//		LocalDate localDate = LocalDate.of(2021, 3, 24);
		LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		paramMap.put("date", date);
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
			InterfaceLogUtils.saveLog(url, ServiceConstant.TX_SAN_YA_CODE, "失败");
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
}
