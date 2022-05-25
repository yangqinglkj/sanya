package org.springblade.modules.screen.service.Impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springblade.common.entity.xc.*;
import org.springblade.common.mapper.xc.*;
import org.springblade.modules.screen.service.CtripService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yangqing
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CtripServiceImpl implements CtripService {

	@Resource
	private OutingWayMapper outingWayMapper;

	@Resource
	private OutingTypeValueMapper outingTypeValueMapper;

	@Resource
	private ReserveDayMapper reserveDayMapper;

	@Resource
	private ReserveChannelMapper reserveChannelMapper;

	@Resource
	private PopularHotelMapper popularHotelMapper;

	@Resource
	private OutingPurposeMapper outingPurposeMapper;

	@Override
	public JSONObject travelMode(Integer type) {

		List<OutingWayEntity> outingWayValueEntities = outingWayMapper.seNewTime();

		//List<OutingWayEntity> outingWayValueEntities = outingWayMapper.selectList(new QueryWrapper<>());
		JSONObject jsonObject = new JSONObject();
		for (OutingWayEntity outingWayEntity : outingWayValueEntities) {
			if (type == 1 && "自由行".equals(outingWayEntity.getData())){
				jsonObject.put("value", outingWayEntity.getValue());
				return jsonObject;
			}
			if (type == 2 && "跟团游".equals(outingWayEntity.getData())){
				jsonObject.put("value", outingWayEntity.getValue());
				return jsonObject;
			}
		}

		return null;

	}

	/**
	 * 旅游目的占比
	 * @return
	 */
	@Override
	public List<Map<String,Object>> travelPurpose() {
		String newDate = outingPurposeMapper.newestDate();
		List<Map<String,Object>> listReturn = new ArrayList<>();
		List<OutingPurposeEntity> outEntityList = outingPurposeMapper.newSeleteList(newDate);
		Double num = 0.0;
		for (OutingPurposeEntity entity : outEntityList) {
			Map<String,Object> map = new HashMap<>();
			String dimValue = entity.getData();
			String ratePercentage = entity.getValue();
			if (dimValue.equals("商务")){
				String newValue = ratePercentage.substring(0, ratePercentage.length() - 1);
				map.put("name","商务");
				map.put("value",newValue);
				listReturn.add(map);
			}else if (dimValue.equals("休闲-带小孩")){
				String newValue = ratePercentage.substring(0, ratePercentage.length() - 1);
				map.put("name","亲子出行");
				map.put("value",newValue);
				listReturn.add(map);
			}else if (dimValue.equals("休闲-带老人")){
				String newValue = ratePercentage.substring(0, ratePercentage.length() - 1);
				map.put("name","陪老人");
				map.put("value",newValue);
				listReturn.add(map);
			}else{
				num += Double.parseDouble(ratePercentage.substring(0,ratePercentage.length()-1));
			}
		}
		Map<String,Object> map1 = new HashMap<>();
		String value = new DecimalFormat("#.##").format(num);
		map1.put("name","度假");
		map1.put("value",value);
		listReturn.add(map1);
		return listReturn;
	}

	/**
	 * 停留天数占比
	 * @return
	 */
	@Override
	public List<Map<String,Object>> reserveDay(String classType) {
		if (StringUtils.isEmpty(classType)){
			classType = "0";
		}
		//type： 1代表0天  2代表1天-3天  3代表3天-7天  4代表7天以上
		List<String> mapZero = reserveDayMapper.dayValue(classType,1);//0天
		List<String> mapsOne = reserveDayMapper.dayValue(classType,2);//"1天"-"3天"
		List<String> mapsThree = reserveDayMapper.dayValue(classType,3);//"3天"-"7天"
		List<String> mapsSeven = reserveDayMapper.dayValue(classType,4);//7天以上

		Map<String,Object> zero = listValueToInter(mapZero,"1");//0天的数值
		Map<String,Object> one = listValueToInter(mapsOne,"2");//"1天"-"3天"
		Map<String,Object> three = listValueToInter(mapsThree,"3");//"3天"-"7天"
		Map<String,Object> seven = listValueToInter(mapsSeven,"4");//7天以上

		List<Map<String,Object>> list = new ArrayList<>();
		list.add(zero);
		list.add(one);
		list.add(three);
		list.add(seven);
		return list;
	}

	private Map<String,Object> listValueToInter(List<String> list,String type){
		Double num = 0.0;
		for (String s : list) {
			if (!StringUtils.isEmpty(s)){
				String newValue = s.substring(0, s.length() - 1);
				num += Double.parseDouble(newValue);
			}
		}
		num =  doubleMethod(num);
		Map<String,Object> map = new HashMap<>();
		if (type.equals("1")){
			map.put("name","0天");
			map.put("value",num);
		}else if (type.equals("2")){
			map.put("name","1-3天");
			map.put("value",num);
		}else if (type.equals("3")){
			map.put("name","3-7天");
			map.put("value",num);
		}else if (type.equals("4")){
			map.put("name","7天以上");
			map.put("value",num);
		}
		return map;
	}

	private static Double doubleMethod(Double db){
		float floa = db.floatValue();
		BigDecimal big = new BigDecimal(String.valueOf(floa));
		return big.doubleValue();
	}

	/**
	 * 预定渠道占比
	 * @return
	 */
	@Override
	public List<Map<String,Object>> reserveChannel() {
//		String dateTime = reserveChannelMapper.findNewTime();//最新时间
		/*LocalDateTime now = LocalDateTime.now();
		String format = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String dateTime = format.substring(0, format.lastIndexOf("-"));*/
		List<ReserveChannelEntity> list = reserveChannelMapper.channelValue();
		//H5
		List<ReserveChannelEntity> H5 = list.stream().filter(x -> "H5".equals(x.getData())).collect(Collectors.toList());
		//app
		List<ReserveChannelEntity> app = list.stream().filter(x -> "APP".equals(x.getData())).collect(Collectors.toList());
		//小程序
		List<ReserveChannelEntity> mini = list.stream().filter(x -> "MINI_PROGRAM".equals(x.getData())).collect(Collectors.toList());
		//分销
		List<ReserveChannelEntity> allianceapi = list.stream().filter(x -> "ALLIANCEAPI".equals(x.getData())).collect(Collectors.toList());
		//PC
		List<ReserveChannelEntity> pc = list.stream().filter(x -> "ONLINE".equals(x.getData())).collect(Collectors.toList());
		//剔除后剩余的对象
		list.removeIf(next -> "H5".equals(next.getData()) || "APP".equals(next.getData()) || "MINI_PROGRAM".equals(next.getData()) || "ALLIANCEAPI".equals(next.getData()) || "ONLINE".equals(next.getData()));
		System.out.println(list);
		//H5 百分比
		List<Map<String,Object>> listReturn = new ArrayList<>();
		for (ReserveChannelEntity entity : H5) {
			Map<String,Object> map = new HashMap<>(16);
			String data = entity.getData();
			String value = entity.getValue();
			String newValue = value.substring(0, value.length() - 1);
			map.put("name",data);
			map.put("value",newValue);
			listReturn.add(map);
		}
		//APP 百分比
		for (ReserveChannelEntity entity : app) {
			Map<String,Object> map = new HashMap<>(16);
			String data = entity.getData();
			String value = entity.getValue();
			String newValue = value.substring(0, value.length() - 1);
			map.put("name",data);
			map.put("value",newValue);
			listReturn.add(map);
		}
		//小程序 百分比
		for (ReserveChannelEntity entity : mini) {
			Map<String,Object> map = new HashMap<>(16);
			String data = entity.getData();
			String value = entity.getValue();
			String newValue = value.substring(0, value.length() - 1);
			map.put("name","小程序");
			map.put("value",newValue);
			listReturn.add(map);
		}
		//分销 百分比
		for (ReserveChannelEntity entity : allianceapi) {
			Map<String,Object> map = new HashMap<>(16);
			String data = entity.getData();
			String value = entity.getValue();
			String newValue = value.substring(0, value.length() - 1);
			map.put("name","分销");
			map.put("value",newValue);
			listReturn.add(map);
		}
		//PC 百分比
		for (ReserveChannelEntity entity : pc) {
			Map<String,Object> map = new HashMap<>(16);
			String data = entity.getData();
			String value = entity.getValue();
			String newValue = value.substring(0, value.length() - 1);
			map.put("name","PC");
			map.put("value",newValue);
			listReturn.add(map);
		}

		//其他类型百分比总和
		List<Double> doubleList = new ArrayList<>();
		for (ReserveChannelEntity entity : list) {
			doubleList.add(Double.parseDouble(entity.getValue().substring(0,entity.getValue().length()-1)));
		}
		double sum = doubleList.stream().mapToDouble(x -> x).sum();
		Map<String,Object> map = new HashMap<>();
		map.put("name","其他");
		map.put("value",String.valueOf(sum));
		listReturn.add(map);

		return listReturn;
	}

	/**
	 * 预定酒店TOP10 占比
	 * @return
	 */
	@Override
	public List<Map<String,Object>> reserveHotel() {
		List<PopularHotelEntity> entityList = popularHotelMapper.channelValue();
		List<Map<String,Object>> listReturn = new ArrayList<>();
		for (int i = 0; i < entityList.size(); i++) {
			Map<String,Object> map = new HashMap<>();
			String hotelName = entityList.get(i).getData();
			StringBuilder sbu = new StringBuilder();
			sbu.append(i+1+"、"+hotelName);
			map.put("type1",sbu.toString());
			listReturn.add(map);
		}

		return listReturn;
	}

	/**
	 * 集合对象拷贝
	 * @param list 原集合
	 * @param tClass 目标class
	 * @param <T> 无类型
	 * @return 新集合
	 */
	public static <T> List copyList(List<T> list,Class tClass) {
		if (CollectionUtils.isEmpty(list)) {
			return new ArrayList();
		}
		return JSON.parseArray(JSON.toJSONString(list), tClass);
	}

	/**
	 * 以百分比方式计数
	 *
	 * @param percent 百分比
	 */
	public static String decimalFormat(double percent) {
//		DecimalFormat df = new DecimalFormat("#%");
		DecimalFormat df = new DecimalFormat("#.##%");
		return df.format(percent);
	}
}
