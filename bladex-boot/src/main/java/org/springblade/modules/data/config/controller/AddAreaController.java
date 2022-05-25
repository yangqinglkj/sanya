package org.springblade.modules.data.config.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springblade.common.constant.ServiceConstant;
import org.springblade.common.entity.tx.analysisNew.AnalysisNewEntity;
import org.springblade.common.entity.tx.analysisNew.AnalysisOriginNewEntity;
import org.springblade.common.entity.tx.area.AreaPeopleCount;
import org.springblade.common.entity.tx.area.AreaPeopleOneHour;
import org.springblade.common.entity.tx.area.AreaRealTimePeople;
import org.springblade.common.mapper.analysisNew.AnalysisNewMapper;
import org.springblade.common.mapper.analysisNew.AnalysisOriginNewMapper;
import org.springblade.common.mapper.tx.area.*;
import org.springblade.common.utils.CommonUtil;
import org.springblade.common.utils.InterfaceLogUtils;
import org.springblade.common.utils.TimeUtils;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Author yq
 * @Date 2020/9/17 19:50
 */

@Slf4j
@RestController
@RequestMapping("/area")
public class AddAreaController {

	@Resource
	private AreaPeopleOneMapper areaPeopleOneMapper;
	@Resource
	private AreaPeopleOneHourMapper areaPeopleOneHourMapper;
	@Resource
	private AreaHeatingValueMapper areaHeatingValueMapper;
	@Resource
	private AreaPeopleCountMapper areaPeopleCountMapper;
	@Resource
	private AreaRealTimePeopleMapper areaRealTimePeopleMapper;
	@Resource
	private AnalysisNewMapper analysisNewMapper;
	@Resource
	private AnalysisOriginNewMapper analysisOriginNewMapper;


	/**
	 * 1.区域实时人数任务（每个小时多一分钟拉取，保存历史数据表里）
	 */
	@GetMapping("/areaPeopleOneHour")
	public void areaPeopleOneHour() {
		int count = 0;
		LocalDateTime startTime = LocalDateTime.of(2021, 1, 29, 5, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2021, 1, 29, 6, 0, 0);
		for (int i = 0; i < 78; i++) {
			int finalCount = count;
			analysisHashMap.forEach((k, v) -> {
				LocalDateTime start = startTime.plusHours(finalCount);
				LocalDateTime end = endTime.plusHours(finalCount);
				long startMilliSecond = start.toInstant(ZoneOffset.of("+8")).toEpochMilli() / 1000;
				long endMilliSecond = end.toInstant(ZoneOffset.of("+8")).toEpochMilli() / 1000;
				//封装参数
				HashMap<String, Object> paramMap = new HashMap<>(16);
				paramMap.put("id", v);
				paramMap.put("begin", startMilliSecond);
				paramMap.put("end", endMilliSecond);
				paramMap.put("interval", 60);
				paramMap.put("key", ServiceConstant.TX_KEY);
				String param = JSON.toJSONString(paramMap);
				//发送请求
				String result = CommonUtil.post(ServiceConstant.TX_AREA_PEOPLE_URL, param);
				//返回结果
				JSONObject json = JSON.parseObject(result);
				if ("0".equals(json.getString("status"))) {
					String data = json.getString("data");
					JSONArray array = JSONArray.parseArray(data);
					//获取时间段最新的一条数据
					JSONObject jsonObject = array.getJSONObject(array.size() - 1);
					String time = jsonObject.getString("time");
					String value = jsonObject.getString("value");
					AreaPeopleOneHour areaPeopleOneHour = new AreaPeopleOneHour();
					areaPeopleOneHour.setPeople(value);
					areaPeopleOneHour.setAreaCode(v);
					DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					LocalDateTime dateTime = LocalDateTime.parse(time, dateTimeFormatter);
					areaPeopleOneHour.setCreateTime(dateTime);
					areaPeopleOneHourMapper.insert(areaPeopleOneHour);
				}
			});
			count++;
		}


	}


	/**
	 * 4.区域实时人流累计（每天23：10拉取前一天的数据）
	 * TODO 好像没使用
	 */
	public void areaPeopleCount() {
		Map<String, Long> timestamp = TimeUtils.getYesterdayTimestamp();
		if (timestamp != null) {
			analysisHashMap.forEach((k, v) -> {
				//封装参数
				HashMap<String, Object> paramMap = new HashMap<>(16);
				paramMap.put("id", v);
				paramMap.put("begin", timestamp.get("startTime"));
				paramMap.put("end", timestamp.get("endTime"));
				paramMap.put("key", ServiceConstant.TX_KEY);
				String param = JSON.toJSONString(paramMap);
				//发送请求
				String result = CommonUtil.post(ServiceConstant.TX_AREA_PEOPLE_COUNT_URL, param);
				//返回结果
				JSONObject json = JSON.parseObject(result);
				if ("0".equals(json.getString("status"))) {
					String data = json.getString("data");
					JSONObject jsonObject = JSON.parseObject(data);
					Integer inPeople = jsonObject.getInteger("in");
					Integer outPeople = jsonObject.getInteger("out");
					AreaPeopleCount areaPeopleCount = new AreaPeopleCount();
					areaPeopleCount.setInPeople(inPeople);
					areaPeopleCount.setOutPeople(outPeople);
					areaPeopleCount.setAreaCode(v);
					areaPeopleCount.setCreateTime(LocalDate.now());
					areaPeopleCountMapper.insert(areaPeopleCount);
				} else {
					InterfaceLogUtils.saveLog("4.区域实时人流累计", v, "请求接口失败");
				}
			});
		}
	}

	/**
	 * 3.区域实时人流(每个小时多5分钟拉取)
	 */
	@GetMapping("/areaTealTimePeople")
	public void areaTealTimePeople() {
		int count = 0;
		LocalDateTime startTime = LocalDateTime.of(2021, 11, 17, 0, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2021, 11, 17, 1, 0, 0);
		for (int i = 0; i < 11; i++) {
			int finalCount = count;
			analysisHashMap.forEach((k, v) -> {
				LocalDateTime start = startTime.plusHours(finalCount);
				LocalDateTime end = endTime.plusHours(finalCount);
				long startMilliSecond = start.toInstant(ZoneOffset.of("+8")).toEpochMilli() / 1000;
				long endMilliSecond = end.toInstant(ZoneOffset.of("+8")).toEpochMilli() / 1000;
				//封装参数
				HashMap<String, Object> paramMap = new HashMap<>(16);
				paramMap.put("id", v);
				paramMap.put("begin", startMilliSecond);
				paramMap.put("end", endMilliSecond);
				paramMap.put("interval", 60);
				paramMap.put("key", ServiceConstant.TX_KEY);
				String param = JSON.toJSONString(paramMap);
				//发送请求
				String result = CommonUtil.post(ServiceConstant.TX_AREA_REAL_TIME_PEOPLE_URL, param);
				//返回结果
				JSONObject json = JSON.parseObject(result);
				if ("0".equals(json.getString("status"))) {
					JSONArray data = json.getJSONArray("data");
					for (Object datum : data) {
						JSONObject jsonObject = (JSONObject) datum;
						String time = jsonObject.getString("time");
						String in = jsonObject.getString("in");
						String out = jsonObject.getString("out");
						AreaRealTimePeople entity = new AreaRealTimePeople();
						entity.setInPeople(in);
						entity.setOutPeople(out);
						entity.setAreaCode(v);
						DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
						LocalDateTime dateTime = LocalDateTime.parse(time, dateTimeFormatter);
						entity.setCreateTime(dateTime);
						areaRealTimePeopleMapper.insert(entity);
					}
				}
			});
			count++;
		}


	}

	@GetMapping("/analysisV2Task")
	public void analysisV2Task() {
		int count = 0;
		LocalDateTime startTime = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2021, 1, 2, 0, 0, 0);
		for (int i = 0; i < 50; i++) {
			int finalCount = count;
			analysisHashMap.forEach((k, v) -> {
				LocalDateTime start = startTime.plusHours(finalCount);
				LocalDateTime end = endTime.plusHours(finalCount);
				long startMilliSecond = start.toInstant(ZoneOffset.of("+8")).toEpochMilli() / 1000;
				long endMilliSecond = end.toInstant(ZoneOffset.of("+8")).toEpochMilli() / 1000;
				log.info("请求数据=》" + k);
				getAnalysisData(v, startMilliSecond, endMilliSecond);
			});
			log.info("==================结束=====================");
			count++;
		}


	}

	/**
	 * @param startMilliSecond 抓取的时间，时间到时间减去1小时的（传3点，就抓2点到3点的数据）
	 * @param endMilliSecond
	 * @param typeCode         区域ID
	 */
	private void getAnalysisData(String typeCode, Long startMilliSecond, Long endMilliSecond) {
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

//		//开始时间和结束时间
//		String begin = Long.toString(DateUtil.toMilliseconds(strData.minusHours(1)));
//		begin = begin.substring(0, 10);
//		String end = Long.toString(DateUtil.toMilliseconds(strData));
//		end = end.substring(0, 10);

		//发送请求
		String imageResult = postRequest(typeCode, startMilliSecond, endMilliSecond, imageTypes);
		String financeResult = postRequest(typeCode, startMilliSecond, endMilliSecond, shoppingAndLifeTypes);

		JSONObject imageJsonObject = JSON.parseObject(imageResult);
		JSONObject financeJsonObject = JSON.parseObject(financeResult);
		// 保存到mysql
		saveFiveNew(imageJsonObject, typeCode);
		saveFinance(financeJsonObject, typeCode);
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
							String tableName = originTable.get(typeCode);
							analysisOriginNewMapper.saveBatch(tableName, analysisOriginEntities);
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

	HashMap<String, String> financeMap = new HashMap<String, String>() {
		{
			put("是否理财", "9");
		}
	};

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

	/**
	 * 封装请求参数
	 *
	 * @param typeCode 区域id
	 * @param begin    开始时间
	 * @param end      结束时间
	 * @param types    类型数组
	 * @return 返回结果
	 */
	private String postRequest(String typeCode, Long begin, Long end, String types) {
		// 年龄,性别,是否有车,消费水平,学历,客源地,是否理财 参数封装
		HashMap<String, Object> param = new HashMap<>(16);
		param.put("id", typeCode);
		param.put("begin", begin);
		param.put("end", end);
		param.put("interval", 60);
		param.put("type", types);
		param.put("key", ServiceConstant.TX_KEY);
		String post = JSON.toJSONString(param);
		return CommonUtil.post(ServiceConstant.TX_AREA_IMAGE, post);
	}

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
//			put("【4A】西岛旅游区", "4069280573448888914");
			put("【4A】西岛旅游区", "4069280573219172332");
			put("【5A】海南南山景区", "4069462018333514283");
//			put("蜈支洲景区", "4069846942670665018");
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

	/**
	 * 区域客源地对应表名
	 */
	HashMap<String, String> originTable = new HashMap<String, String>() {
		{
			put("4069462959793577915", "tx_area_origin_scenic_yazhou");
			put("4069461880929738304", "tx_area_origin_scenic_daxiaodongtian");
			put("4069283472388272897", "tx_area_origin_scenic_meiguigu");
			put("4069281244018616740", "tx_area_origin_scenic_luhuitou");
			put("4069846805240994142", "tx_area_origin_scenic_buyecheng");
			put("4069470668819008983", "tx_area_origin_scenic_qianguqing");
			put("4069282708340861593", "tx_area_origin_scenic_dadonghai");
			put("4069283618460972764", "tx_area_origin_scenic_senlin");
			put("4069280573219172332", "tx_area_origin_scenic_xidao");
			put("4069462018333514283", "tx_area_origin_scenic_nanshan");
			put("4069846942546510768", "tx_area_origin_scenic_wuzhizhou");
			put("4069468482687799490", "tx_area_origin_scenic_tianya");

			put("4069283004616630384", "tx_area_origin_business_yingbinlu");
			put("4069847118364191869", "tx_area_origin_business_haitangwan");
			put("4069282712636644483", "tx_area_origin_business_dadonghai");
			put("4069282931181451953", "tx_area_origin_business_shangpinjie");
			put("4069281457871158129", "tx_area_origin_business_jiefanglu");
			put("4069847934674639643", "tx_area_origin_business_mianshuicheng");
			put("4069283497961413502", "tx_area_origin_business_yalongwan");
			put("4069469015674903436", "tx_area_origin_business_sanyawan");
		}
	};


}
