package org.springblade.modules.data.config;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springblade.common.constant.ServiceConstant;
import org.springblade.common.entity.tx.area.*;
import org.springblade.common.mapper.tx.area.*;
import org.springblade.common.utils.CommonUtil;
import org.springblade.common.utils.InterfaceLogUtils;
import org.springblade.common.utils.TimeUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author yq
 * @Date 2020/9/17 19:50
 */

@Component
@Slf4j
@ConditionalOnProperty(prefix = "scheduling", name = "enabled", havingValue = "true")
public class TxAreaData {

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


	/**
	 * 1.区域实时人数任务（每5分钟拉取，更新原有数据）
	 */
	@Scheduled(cron = "0 0/5 * * * ?")
	public void areaPeople() {
		Map<String, Long> timestamp = TimeUtils.getLocalDate();
		List<AreaPeopleOne> list = new ArrayList<>();
		if (timestamp != null) {
			analysisHashMap.forEach((k, v) -> {
				//封装参数
				HashMap<String, Object> paramMap = new HashMap<>(16);
				paramMap.put("id", v);
				paramMap.put("begin", timestamp.get("startTime"));
				paramMap.put("end", timestamp.get("endTime"));
				paramMap.put("interval", 5);
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
					AreaPeopleOne areaPeopleOne = new AreaPeopleOne();
					areaPeopleOne.setPeople(value);
					areaPeopleOne.setAreaCode(v);
					DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					LocalDateTime dateTime = LocalDateTime.parse(time, dateTimeFormatter);
					areaPeopleOne.setCreateTime(dateTime);
					list.add(areaPeopleOne);
				}
			});
		}
		List<AreaPeopleOne> areaPeopleOnesList = areaPeopleOneMapper.selectList(new QueryWrapper<>());
		if (CollectionUtils.isEmpty(areaPeopleOnesList)) {
			//如果数据库为空，插入最新的数据
			areaPeopleOneMapper.saveAreaPeople(list);
		} else if (CollectionUtils.isNotEmpty(list) && list.size() == 20) {
			//数据库不为空，先删除旧数据，再插入新数据
			List<AreaPeopleOne> areaPeopleOnes = areaPeopleOneMapper.selectList(new QueryWrapper<>());
			areaPeopleOneMapper.deleteBatchIds(areaPeopleOnes.stream().map(AreaPeopleOne::getId).collect(Collectors.toList()));
			areaPeopleOneMapper.saveAreaPeople(list);
		} else {
			InterfaceLogUtils.saveLog("1.区域实时人数 5分钟", null, "请求接口失败");
		}

	}

	/**
	 * 1.区域实时人数任务（每个小时多一分钟拉取，保存历史数据表里）
	 */
	@Scheduled(cron = "0 1 * * * ?")
	public void areaPeopleOneHour() {
		Map<String, Long> timestamp = TimeUtils.getTimestamp();
		if (timestamp != null) {
			analysisHashMap.forEach((k, v) -> {
				//封装参数
				HashMap<String, Object> paramMap = new HashMap<>(16);
				paramMap.put("id", v);
				paramMap.put("begin", timestamp.get("startTime"));
				paramMap.put("end", timestamp.get("endTime"));
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
				}else {
					InterfaceLogUtils.saveLog("区域实时人数 60分钟", v, "请求接口失败");
				}
			});

		}
	}

	/**
	 * 区域热力值（每5分钟拉取，更新原有数据）
	 */
	@Scheduled(cron = "0 0/5 * * * ?")
	public void areaHeatingValue() {
		Map<String, Long> timestamp = TimeUtils.getTimestamp();
		//查询历史数据
		List<AreaHeatingValue> list1 = areaHeatingValueMapper.selectList(new QueryWrapper<>());
		//如果存在则清空
		if (CollectionUtils.isNotEmpty(list1)) {
			areaHeatingValueMapper.deleteBatchIds(list1.stream().map(AreaHeatingValue::getId).collect(Collectors.toList()));
		}
		if (timestamp != null) {
			analysisHashMap.forEach((k, v) -> {
				//封装参数
				HashMap<String, Object> paramMap = new HashMap<>(16);
				paramMap.put("id", v);
				paramMap.put("begin", timestamp.get("startTime"));
				paramMap.put("end", timestamp.get("endTime"));
				paramMap.put("interval", 5);
				paramMap.put("key", ServiceConstant.TX_KEY);
				String param = JSON.toJSONString(paramMap);
				//发送请求
				String result = CommonUtil.post(ServiceConstant.TX_AREA_HEATING_VALUE_URL, param);
				//返回结果
				JSONObject json = JSON.parseObject(result);
				if ("0".equals(json.getString("status"))) {
					String data = json.getString("data");
					JSONArray array = JSONArray.parseArray(data);
					//获取时间段最新的一条数据
					JSONObject jsonObject = array.getJSONObject(array.size() - 1);
					String time = jsonObject.getString("time");
					String points = jsonObject.getString("points");
					List<AreaHeatingValue> list = new ArrayList<>();
					List<AreaHeatingValue> areaHeatingValues = JSONArray.parseArray(points, AreaHeatingValue.class);
					for (AreaHeatingValue entity : areaHeatingValues) {
						AreaHeatingValue value = new AreaHeatingValue();
						value.setWeight(entity.getWeight());
						value.setLng(entity.getLng());
						value.setLat(entity.getLat());
						value.setAreaCode(v);
						DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
						LocalDateTime dateTime = LocalDateTime.parse(time, dateTimeFormatter);
						value.setCreateTime(dateTime);
						list.add(value);
					}
					areaHeatingValueMapper.saveBatch(list);
				}
			});
		}
	}

	/**
	 * 4.区域实时人流累计（每天23：10拉取前一天的数据）
	 * TODO 好像没使用
	 */
	@Scheduled(cron = "0 10 23 * * ?")
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
	@Scheduled(cron = "0 5 * * * ?")
	public void areaTealTimePeople() {
		Map<String, Long> timestamp = TimeUtils.getTimestamp();
		if (timestamp != null) {
			analysisHashMap.forEach((k, v) -> {
				//封装参数
				HashMap<String, Object> paramMap = new HashMap<>(16);
				paramMap.put("id", v);
				paramMap.put("begin", timestamp.get("startTime"));
				paramMap.put("end", timestamp.get("endTime"));
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
				} else {
					InterfaceLogUtils.saveLog("3.区域实时人流", v, "请求接口失败");
				}
			});
		}
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


}
