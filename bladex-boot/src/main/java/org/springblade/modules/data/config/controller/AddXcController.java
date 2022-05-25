package org.springblade.modules.data.config.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springblade.common.constant.XcConstant;
import org.springblade.common.entity.xc.*;
import org.springblade.common.mapper.xc.*;
import org.springblade.common.utils.CommonUtil;
import org.springblade.common.utils.InterfaceLogUtils;
import org.springblade.common.utils.TimeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author yq
 * @Date 2021/2/1 10:35
 */
@Slf4j
@RestController
@RequestMapping("/saveXc")
public class AddXcController {

	@Resource
	private OutingWayMapper outingWayMapper;
	@Resource
	private TransportationMapper transportationMapper;
	@Resource
	private PopularHotelMapper popularHotelMapper;
	@Resource
	private OutingPurposeMapper outingPurposeMapper;
	@Resource
	private ReserveChannelMapper reserveChannelMapper;
	@Resource
	private ReserveDayMapper reserveDayMapper;

	/**
	 * 游客出游方式分布
	 */
	@GetMapping("/outingWay")
	public void outingWay() {
		LocalDate startTime = LocalDate.of(2021, 3, 24);
		LocalDate endTime = LocalDate.of(2021, 3, 25);
		int count = 0;
		for (int i = 0; i < 2; i++) {
			String url = "游客出游方式分布";
			//封装参数
			HashMap<String, String> paramMap = new HashMap<>(16);
			Map<String, String> map = TimeUtils.getDate();
			paramMap.put("account", XcConstant.XC_ACCOUNT);
			paramMap.put("token", XcConstant.XC_TOKEN);
			paramMap.put("startTime", startTime.plusDays(count).toString());
			paramMap.put("endTime", endTime.plusDays(count).toString());
			paramMap.put("country", XcConstant.XC_COUNTRY);
			paramMap.put("province", XcConstant.XC_PROVINCE);
			paramMap.put("city", XcConstant.XC_CITY);
			paramMap.put("district", "");
			paramMap.put("period", "");
			String param = JSON.toJSONString(paramMap);
			//发送请求
			String result = CommonUtil.post(XcConstant.XC_OUTING_WAY_URL, param);
			//返回结果
			JSONObject json = JSON.parseObject(result);
			String responseStatus = json.getString("ResponseStatus");
			JSONObject jsonObject = JSON.parseObject(responseStatus);
			if ("Success".equals(jsonObject.getString("Ack"))) {
				JSONArray objects = json.getJSONArray("value");
				if (objects.size() > 0) {
					for (Object o : objects) {
						JSONObject object = (JSONObject) o;
						String data = object.getString("data");
						String value = object.getString("value");
						OutingWayEntity entity = new OutingWayEntity();
						entity.setData(data);
						entity.setValue(value);
						entity.setCreateTime(startTime.plusDays(count));
						outingWayMapper.insert(entity);
					}
				} else {
					InterfaceLogUtils.saveLog(url, null, "接口无数据");
				}
			} else {
				InterfaceLogUtils.saveLog(url, null, "请求接口失败");
			}
			count++;
		}

	}

	/**
	 * 酒店游客出行目的
	 */
	@GetMapping("/outingPurpose")
	public void outingPurpose() {
		LocalDate startTime = LocalDate.of(2021, 3, 25);
		LocalDate endTime = LocalDate.of(2021, 3, 26);
		String url = "酒店游客出行目的";
		//封装参数
		HashMap<String, Object> paramMap = new HashMap<>(16);
		Map<String, String> map = TimeUtils.getDate();
		paramMap.put("account", XcConstant.XC_ACCOUNT);
		paramMap.put("token", XcConstant.XC_TOKEN);
		paramMap.put("startTime", startTime.toString());
		paramMap.put("endTime", endTime.toString());
		paramMap.put("country", XcConstant.XC_COUNTRY);
		paramMap.put("province", XcConstant.XC_PROVINCE);
		paramMap.put("city", XcConstant.XC_CITY);
		paramMap.put("district", "");
		paramMap.put("period", "");
		String param = JSON.toJSONString(paramMap);
		//发送请求
		String result = CommonUtil.post(XcConstant.XC_OUTING_PURPOSE, param);
		//返回结果
		JSONObject json = JSON.parseObject(result);
		String responseStatus = json.getString("ResponseStatus");
		JSONObject jsonObject = JSON.parseObject(responseStatus);
		if ("Success".equals(jsonObject.getString("Ack"))) {
			//当前日期是否有数据
			if (json.getJSONArray("value").size() > 0) {
				String value = json.getString("value");
				List<OutingPurposeEntity> entityList = JSONArray.parseArray(value, OutingPurposeEntity.class);
				for (OutingPurposeEntity entity : entityList) {
					entity.setData(entity.getData());
					entity.setValue(entity.getValue());
					entity.setCreateTime(startTime);
					outingPurposeMapper.insert(entity);
				}
			} else {
				InterfaceLogUtils.saveLog(url, null, "接口无数据");
			}
		} else {
			InterfaceLogUtils.saveLog(url, null, "请求接口失败");
		}

	}


	/**
	 * 提前预定天数
	 * 每天凌晨1点 获取前天数据
	 */
	@GetMapping("/reserveDay")
	public void reserveDay() {
		LocalDate startTime = LocalDate.of(2021, 1, 28);
		LocalDate endTime = LocalDate.of(2021, 1, 29);
		int count = 0;
		for (int i = 0; i < 2; i++) {
			String url = "提前预定天数";
			//封装参数
			HashMap<String, Object> paramMap = new HashMap<>(16);
			Map<String, String> map = TimeUtils.getDate();
			paramMap.put("account", XcConstant.XC_ACCOUNT);
			paramMap.put("token", XcConstant.XC_TOKEN);
			paramMap.put("startTime", startTime.plusDays(count).toString());
			paramMap.put("endTime", endTime.plusDays(count).toString());
			paramMap.put("country", XcConstant.XC_COUNTRY);
			paramMap.put("province", XcConstant.XC_PROVINCE);
			paramMap.put("city", XcConstant.XC_CITY);
			paramMap.put("district", "");
			paramMap.put("period", "");
			String param = JSON.toJSONString(paramMap);
			//发送请求
			String result = CommonUtil.post(XcConstant.XC_RESERVE_DAY, param);
			//返回结果
			JSONObject json = JSON.parseObject(result);
			String responseStatus = json.getString("ResponseStatus");
			JSONObject jsonObject = JSON.parseObject(responseStatus);
			if ("Success".equals(jsonObject.getString("Ack"))) {
				//当前日期是否有数据
				if (json.getJSONArray("ratioInfos").size() > 0) {
					//汽车票 度假 门票  机票  其他  酒店
					JSONArray ratioInfos = json.getJSONArray("ratioInfos");
					//删除玩乐
					Iterator<Object> iterator = ratioInfos.iterator();
					while (iterator.hasNext()) {
						JSONObject next = (JSONObject) iterator.next();
						if (next.getString("name").equals("玩乐")) {
							iterator.remove();
						}
					}
					//1：门票  2：酒店  3：机票  4：汽车票  5：度假  6：其他
					int finalCount = count;
					ratioInfos.forEach(item -> {
						JSONObject object = (JSONObject) item;
						String name = object.getString("name");
						JSONArray values = object.getJSONArray("values");
						if (values.size() > 0) {
							ReserveDayEntity entity = new ReserveDayEntity();
							if ("门票".equals(name)) {
								entity.setType(1);
							} else if ("酒店".equals(name)) {
								entity.setType(2);
							} else if ("机票".equals(name)) {
								entity.setType(3);
							} else if ("汽车票".equals(name)) {
								entity.setType(4);
							} else if ("度假".equals(name)) {
								entity.setType(5);
							} else if ("其他".equals(name)) {
								entity.setType(6);
							}
							//获取每种类型值
							values.forEach(value -> {
								JSONObject resultValue = (JSONObject) value;
								String data = resultValue.getString("data");
								String percent = resultValue.getString("value");
								entity.setData(data);
								entity.setValue(percent);
								entity.setCreateTime(startTime.plusDays(finalCount));
								reserveDayMapper.insert(entity);
							});
						}

					});
				} else {
					InterfaceLogUtils.saveLog(url, null, "接口无数据");
				}
			} else {
				InterfaceLogUtils.saveLog(url, null, "请求接口失败");
			}
			count++;
		}
	}

	/**
	 * 预订方式
	 */
	@GetMapping("/reserveChannel")
	public void reserveChannel() {
		LocalDate startTime = LocalDate.of(2021, 1, 28);
		LocalDate endTime = LocalDate.of(2021, 1, 29);
		int count = 0;
		for (int i = 0; i < 2; i++) {
			String url = "预定渠道";
			//封装参数
			HashMap<String, Object> paramMap = new HashMap<>(16);
			Map<String, String> map = TimeUtils.getDate();
			paramMap.put("account", XcConstant.XC_ACCOUNT);
			paramMap.put("token", XcConstant.XC_TOKEN);
			paramMap.put("startTime", startTime.plusDays(count).toString());
			paramMap.put("endTime", endTime.plusDays(count).toString());
			paramMap.put("country", XcConstant.XC_COUNTRY);
			paramMap.put("province", XcConstant.XC_PROVINCE);
			paramMap.put("city", XcConstant.XC_CITY);
			paramMap.put("district", "");
			paramMap.put("period", "");
			String param = JSON.toJSONString(paramMap);
			//发送请求
			String result = CommonUtil.post(XcConstant.XC_RESERVE_CHANNEL, param);
			//返回结果
			JSONObject json = JSON.parseObject(result);
			String responseStatus = json.getString("ResponseStatus");
			JSONObject jsonObject = JSON.parseObject(responseStatus);
			if ("Success".equals(jsonObject.getString("Ack"))) {
				//当前日期是否有数据
				if (json.getJSONArray("value").size() > 0) {
					String value = json.getString("value");
					List<ReserveChannelEntity> list = JSONArray.parseArray(value, ReserveChannelEntity.class);
					List<ReserveChannelEntity> newList = list.stream().filter(x -> !x.getData().equals("NULL")).collect(Collectors.toList());
					for (ReserveChannelEntity entity : newList) {
						entity.setData(entity.getData());
						entity.setValue(entity.getValue());
						DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						LocalDate dateTime = LocalDate.parse(map.get("theDayBeforeYesterday"), dateTimeFormatter);
						entity.setCreateTime(startTime.plusDays(count));
						reserveChannelMapper.insert(entity);
					}
				} else {
					InterfaceLogUtils.saveLog(url, null, "接口无数据");
				}
			} else {
				InterfaceLogUtils.saveLog(url, null, "请求接口失败");
			}
			count++;
		}
	}

	/**
	 * 热门酒店TOP10
	 */
	@GetMapping("/popularHotel")
	public void popularHotel() {
		LocalDate startTime = LocalDate.of(2021, 2, 27);
		LocalDate endTime = LocalDate.of(2021, 2, 27);
		int count = 0;
		for (int i = 0; i < 1; i++) {
			String url = "热门酒店TOP10";
			//封装参数
			HashMap<String, Object> paramMap = new HashMap<>(16);
			Map<String, String> map = TimeUtils.getDate();
			paramMap.put("account", XcConstant.XC_ACCOUNT);
			paramMap.put("token", XcConstant.XC_TOKEN);
			paramMap.put("startTime", startTime.plusDays(count).toString());
			paramMap.put("endTime", endTime.plusDays(count).toString());
			paramMap.put("country", XcConstant.XC_COUNTRY);
			paramMap.put("province", XcConstant.XC_PROVINCE);
			paramMap.put("city", XcConstant.XC_CITY);
			paramMap.put("district", "");
			paramMap.put("period", "");
			String param = JSON.toJSONString(paramMap);
			//发送请求
			String result = CommonUtil.post(XcConstant.XC_POPULAR_HOTEL, param);
			//返回结果
			JSONObject json = JSON.parseObject(result);
			String responseStatus = json.getString("ResponseStatus");
			JSONObject jsonObject = JSON.parseObject(responseStatus);
			if ("Success".equals(jsonObject.getString("Ack"))) {
				//当前日期是否有数据
				if (json.getJSONArray("value").size() > 0) {
					String value = json.getString("value");
					List<PopularHotelEntity> entityList = JSONArray.parseArray(value, PopularHotelEntity.class);
					for (PopularHotelEntity entity : entityList) {
						entity.setData(entity.getData());
						entity.setValue(entity.getValue());
						entity.setCreateTime(startTime.plusDays(count));
						popularHotelMapper.insert(entity);
					}
				} else {
					InterfaceLogUtils.saveLog(url, null, "接口无数据");
				}
			} else {
				InterfaceLogUtils.saveLog(url, null, "请求接口失败");
			}
			count++;
		}
	}

	/**
	 * 各交通方式人次分布
	 */
	@GetMapping("/transportation")
	public void transportation() {
		LocalDate startTime = LocalDate.of(2021, 1, 28);
		LocalDate endTime = LocalDate.of(2021, 1, 29);
		int count = 0;
		for (int i = 0; i < 2; i++) {
			String url = "各交通方式人次分布";
			//封装参数
			HashMap<String, Object> paramMap = new HashMap<>(16);
			Map<String, String> map = TimeUtils.getDate();
			paramMap.put("account", XcConstant.XC_ACCOUNT);
			paramMap.put("token", XcConstant.XC_TOKEN);
			paramMap.put("startTime", startTime.plusDays(count).toString());
			paramMap.put("endTime", endTime.plusDays(count).toString());
			paramMap.put("country", XcConstant.XC_COUNTRY);
			paramMap.put("province", XcConstant.XC_PROVINCE);
			paramMap.put("city", XcConstant.XC_CITY);
			paramMap.put("district", "");
			paramMap.put("period", "");
			String param = JSON.toJSONString(paramMap);
			//发送请求
			String result = CommonUtil.post(XcConstant.XC_TRANSPORTATION_URL, param);
			//返回结果
			JSONObject json = JSON.parseObject(result);
			String responseStatus = json.getString("ResponseStatus");
			JSONObject jsonObject = JSON.parseObject(responseStatus);
			if ("Success".equals(jsonObject.getString("Ack"))) {
				JSONArray objects = json.getJSONArray("value");
				if (objects.size() > 0) {
					for (Object o : objects) {
						JSONObject object = (JSONObject) o;
						String data = object.getString("data");
						String value = object.getString("value");
						TransportationEntity entity = new TransportationEntity();
						entity.setData(data);
						entity.setValue(value);
						entity.setCreateTime(startTime.plusDays(count));
						transportationMapper.insert(entity);
					}
				} else {
					InterfaceLogUtils.saveLog(url, null, "接口无数据");
				}
			} else {
				InterfaceLogUtils.saveLog(url, null, "请求接口失败");
			}
			count++;
		}
	}
}
