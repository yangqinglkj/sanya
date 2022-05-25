package org.springblade.modules.data.config;

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
import org.springblade.modules.system.mapper.LogMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
 * @Date 2020/10/20 11:13
 */
@Component
@Slf4j
@ConditionalOnProperty(prefix = "scheduling", name = "enabled", havingValue = "true")
public class XcData {
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
	 * 2.游客出游方式分布
	 * 每天凌晨1点 获取前天数据
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void outingWay() {
		String url = "游客出游方式分布";
		//封装参数
		HashMap<String, String> paramMap = new HashMap<>(16);
		Map<String, String> map = TimeUtils.getDate();
		paramMap.put("account", XcConstant.XC_ACCOUNT);
		paramMap.put("token", XcConstant.XC_TOKEN);
		paramMap.put("startTime", map.get("theDayBeforeYesterday"));
		paramMap.put("endTime", map.get("yesterday"));
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
					entity.setCreateTime(LocalDate.parse(map.get("theDayBeforeYesterday"), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
					outingWayMapper.insert(entity);
				}
			} else {
				InterfaceLogUtils.saveLog(url, null, "接口无数据");
			}
		} else {
			InterfaceLogUtils.saveLog(url, null, "请求接口失败");
		}
	}

	/**
	 * 4.各交通方式人次分布
	 * 每天凌晨1点 获取前天数据
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void transportation() {
		String url = "各交通方式人次分布";
		//封装参数
		HashMap<String, Object> paramMap = new HashMap<>(16);
		Map<String, String> map = TimeUtils.getDate();
		paramMap.put("account", XcConstant.XC_ACCOUNT);
		paramMap.put("token", XcConstant.XC_TOKEN);
		paramMap.put("startTime", map.get("theDayBeforeYesterday"));
		paramMap.put("endTime", map.get("yesterday"));
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
					DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					LocalDate dateTime = LocalDate.parse(map.get("theDayBeforeYesterday"), dateTimeFormatter);
					entity.setCreateTime(dateTime);
					transportationMapper.insert(entity);
				}
			} else {
				InterfaceLogUtils.saveLog(url, null, "接口无数据");
			}
		} else {
			InterfaceLogUtils.saveLog(url, null, "请求接口失败");
		}
	}

	/**
	 * 5.热门酒店TOP10
	 * 每天凌晨1点 获取前天数据
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void popularHotel() {
		String url = "热门酒店TOP10";
		//封装参数
		HashMap<String, Object> paramMap = new HashMap<>(16);
		Map<String, String> map = TimeUtils.getDate();
		paramMap.put("account", XcConstant.XC_ACCOUNT);
		paramMap.put("token", XcConstant.XC_TOKEN);
		paramMap.put("startTime", map.get("theDayBeforeYesterday"));
		paramMap.put("endTime", map.get("yesterday"));
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
					DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					LocalDate dateTime = LocalDate.parse(map.get("theDayBeforeYesterday"), dateTimeFormatter);
					entity.setCreateTime(dateTime);
					popularHotelMapper.insert(entity);
				}
			} else {
				InterfaceLogUtils.saveLog(url, null, "接口无数据");
			}
		} else {
			InterfaceLogUtils.saveLog(url, null, "请求接口失败");
		}
	}

	/**
	 * 6.酒店游客出行目的
	 * 每天凌晨1点 获取前天数据
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void outingPurpose() {
		String url = "酒店游客出行目的";
		//封装参数
		HashMap<String, Object> paramMap = new HashMap<>(16);
		Map<String, String> map = TimeUtils.getDate();
		paramMap.put("account", XcConstant.XC_ACCOUNT);
		paramMap.put("token", XcConstant.XC_TOKEN);
		paramMap.put("startTime", map.get("theDayBeforeYesterday"));
		paramMap.put("endTime", map.get("yesterday"));
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
					DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					LocalDate dateTime = LocalDate.parse(map.get("theDayBeforeYesterday"), dateTimeFormatter);
					entity.setCreateTime(dateTime);
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
	 * 预定渠道
	 * 每天凌晨1点 获取前天数据
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void reserveChannel() {
		String url = "预定渠道";
		//封装参数
		HashMap<String, Object> paramMap = new HashMap<>(16);
		Map<String, String> map = TimeUtils.getDate();
		paramMap.put("account", XcConstant.XC_ACCOUNT);
		paramMap.put("token", XcConstant.XC_TOKEN);
		paramMap.put("startTime", map.get("theDayBeforeYesterday"));
		paramMap.put("endTime", map.get("yesterday"));
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
					entity.setCreateTime(dateTime);
					reserveChannelMapper.insert(entity);
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
	@Scheduled(cron = "0 0 1 * * ?")
	public void reserveDay() {
		String url = "提前预定天数";
		//封装参数
		HashMap<String, Object> paramMap = new HashMap<>(16);
		Map<String, String> map = TimeUtils.getDate();
		paramMap.put("account", XcConstant.XC_ACCOUNT);
		paramMap.put("token", XcConstant.XC_TOKEN);
		paramMap.put("startTime", map.get("theDayBeforeYesterday"));
		paramMap.put("endTime", map.get("yesterday"));
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
							DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
							LocalDate dateTime = LocalDate.parse(map.get("theDayBeforeYesterday"), dateTimeFormatter);
							entity.setCreateTime(dateTime);
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
	}
}
