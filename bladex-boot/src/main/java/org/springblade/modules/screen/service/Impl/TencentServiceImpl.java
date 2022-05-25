package org.springblade.modules.screen.service.Impl;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springblade.common.entity.SanYaAnalysisOrigin;
import org.springblade.common.entity.SpTemporary;
import org.springblade.common.entity.tx.AreaStayDayEntity;
import org.springblade.common.entity.tx.HeatingValue;
import org.springblade.common.entity.tx.MigrationEntity;
import org.springblade.common.entity.tx.area.AreaHeatingValue;
import org.springblade.common.mapper.analysis.AnalysisMapper;
import org.springblade.common.mapper.analysis.AnalysisPopulationMapper;
import org.springblade.common.mapper.tx.AreaStayDayMapper;
import org.springblade.common.mapper.tx.HeatingValueMapper;
import org.springblade.common.mapper.tx.area.AreaHeatingValueMapper;
import org.springblade.common.utils.AreaCodeUtils;
import org.springblade.common.utils.MapUtils;
import org.springblade.common.vo.*;
import org.springblade.common.vo.xc.area.AreaBusinessVO;
import org.springblade.modules.backstage.mapper.TxBackstageTencentMapper;
import org.springblade.modules.backstage.service.TouristImageDataHandler;
import org.springblade.modules.backstage.vo.tx.PeopleVo;
import org.springblade.modules.backstage.vo.tx.SourcePlaceVO;
import org.springblade.modules.backstage.vo.tx.ZoneSourcePlaceVO;
import org.springblade.modules.screen.mapper.TencentMapper;
import org.springblade.modules.screen.service.TencentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author yq
 * @Date 2020/9/17 15:40
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class TencentServiceImpl implements TencentService {

	@Resource
	private AreaStayDayMapper areaStayDayMapper;
	@Resource
	private TencentMapper tencentMapper;
	@Resource
	private HeatingValueMapper heatingValueMapper;
	@Autowired
	private RedisTemplate redisTemplate;
	@Resource
	private AnalysisPopulationMapper analysisPopulationMapper;
	@Resource
	private AreaHeatingValueMapper areaHeatingValueMapper;


	@Override
	public Object getGender(String typeCode, Integer type) {
		//性别画像
		String gender = "gender";
		JSONObject image;
		//typeCode > 4 代表景点和商圈
		if (Long.parseLong(typeCode) > 4) {
			//区域最新总人数
			Integer areaTotalPeople = tencentMapper.getAreaPeople(typeCode);
			//区域性别画像
			List<AreaVO> areaImageList = tencentMapper.getAreaImage(gender, typeCode);
			List<AreaVO> areaGenderList = TouristImageDataHandler.commonHandlerData(areaImageList, areaTotalPeople);
			image = getGenderImage(areaGenderList, type);
		} else {
			//三亚市和四大区域
			//区划最新总人数
			Integer zoneTotalPeople = tencentMapper.getZonePeople(typeCode);
			//区划性别画像
			List<AreaVO> genderImageList = tencentMapper.getZoneImage(gender, typeCode);
			List<AreaVO> areaGenderList = TouristImageDataHandler.commonHandlerData(genderImageList, zoneTotalPeople);
			image = getGenderImage(areaGenderList, type);
		}
		return image;
	}

	private JSONObject getGenderImage(List<AreaVO> list, Integer type) {
		JSONObject jsonObject = new JSONObject();
		for (AreaVO vo : list) {
			if (type == 1 && "男".equals(vo.getName())) {
				jsonObject.put("value", vo.getValue() + "%");
			}
			if (type == 2 && "女".equals(vo.getName())) {
				jsonObject.put("value", vo.getValue() + "%");
			}
		}
		return jsonObject;
	}

	@Override
	public List<AreaVO> getCar(String typeCode) {
		List<AreaVO> carList;
		if (Long.parseLong(typeCode) > 4) {
			//区域最新总人数
			Integer areaTotalPeople = tencentMapper.getAreaPeople(typeCode);
			//持车画像
			List<AreaVO> areaImage = tencentMapper.getAreaImage("car", typeCode);
			carList = TouristImageDataHandler.commonHandlerData(areaImage, areaTotalPeople);
		} else {
			//三亚市和四大区域
			//区划最新总人数
			Integer zoneTotalPeople = tencentMapper.getZonePeople(typeCode);
			//区划持车画像
			List<AreaVO> zoneImage = tencentMapper.getZoneImage("car", typeCode);
			carList = TouristImageDataHandler.commonHandlerData(zoneImage, zoneTotalPeople);
		}
		carList.forEach(car -> {
			if ("否".equals(car.getName())) {
				car.setName("无车");
			}
			if ("是".equals(car.getName())) {
				car.setName("有车");
			}
			if ("没车".equals(car.getName())) {
				car.setName("无车");
			}
		});
		return carList;
	}


	@Override
	public List<AreaVO> getAge(String typeCode) {
		//年龄画像
		String age = "age";
		// typeCode > 4 景点和商圈
		List<AreaVO> ageList;
		if (Long.parseLong(typeCode) > 4) {
			//区域最新总人数
			Integer areaTotalPeople = tencentMapper.getAreaPeople(typeCode);
			//区域年龄画像
			List<AreaVO> areaAgeImage = tencentMapper.getAreaImage(age, typeCode);
//			List<BigDecimal> otherList = new ArrayList<>();
//			List<AreaVO> list = new ArrayList<>();
//			for (AreaVO areaVO : areaAgeImage) {
//				if ("70以上".equals(areaVO.getName()) || "other".equals(areaVO.getName())) {
//					otherList.add(areaVO.getValue());
//				} else {
//					list.add(areaVO);
//				}
//			}
//			AreaVO vo = new AreaVO();
//			vo.setName("70岁以上");
//			vo.setValue(otherList.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
//			list.add(vo);
			ageList = TouristImageDataHandler.commonHandlerData(areaAgeImage, areaTotalPeople);
		} else {
			//区划最新总人数
			Integer zoneTotalPeople = tencentMapper.getZonePeople(typeCode);
			//区划年龄画像
			List<AreaVO> zoneAgeImage = tencentMapper.getZoneImage(age, typeCode);
			List<BigDecimal> list = new ArrayList<>();
			zoneAgeImage.forEach(item -> {
				if (">=70".equals(item.getName()) || "other".equals(item.getName())){
					list.add(item.getValue());
				}
			});
			List<AreaVO> collect = zoneAgeImage.stream().filter(x -> !">=70".equals(x.getName())).filter(x -> !"other".equals(x.getName())).collect(Collectors.toList());
			AreaVO vo = new AreaVO();
			vo.setName("70岁以上");
			vo.setValue(list.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
			collect.add(vo);
			ageList = TouristImageDataHandler.commonHandlerData(collect, zoneTotalPeople);
		}
		return ageList;
	}


	@Override
	public JSONObject getEducation(String typeCode) {
		String education = "education";
		List<Integer> peopleList;
		List<AreaVO> educationList;
		if (Long.parseLong(typeCode) > 4) {
			//区域最新总人数
			Integer areaTotalPeople = tencentMapper.getAreaPeople(typeCode);
			//区域学历画像
			List<AreaVO> areaEducationList = tencentMapper.getAreaImage(education, typeCode);
			educationList = TouristImageDataHandler.commonHandlerData(areaEducationList, areaTotalPeople);
			//获取人数
			peopleList = educationList.stream().map(AreaVO::getPeople).collect(Collectors.toList());
		} else {
			//区划最新总人数
			Integer zoneTotalPeople = tencentMapper.getZonePeople(typeCode);
			//区划学历画像
			List<AreaVO> zoneEducationList = tencentMapper.getZoneImage(education, typeCode);
			educationList = TouristImageDataHandler.commonHandlerData(zoneEducationList, zoneTotalPeople);
			//获取人数
			peopleList = educationList.stream().map(AreaVO::getPeople).collect(Collectors.toList());
		}
		educationList.forEach(vo ->{
			if ("博士".equals(vo.getName())){
				vo.setName("博士及以上");
			}
		});
		return returnJson(educationList, peopleList);
	}


	private JSONObject returnJson(List<AreaVO> list, List<Integer> peopleList) {
		//名称数组
		JSONArray array = JSONArray.parseArray(JSON.toJSONString(list));
		JSONArray values = JSONArray.parseArray(JSON.toJSONString(peopleList));

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("indicator", array);

		JSONObject valueJson = new JSONObject();
		valueJson.put("value", values);

		JSONArray jsonArray = new JSONArray();
		jsonArray.add(valueJson);

		JSONObject jsonObject3 = new JSONObject();

		List<JSONObject> dataList = new ArrayList<>();
		dataList.add(jsonObject3);

		jsonObject3.put("data", jsonArray);
		jsonObject.put("series", dataList);
		return jsonObject;
	}


	@Override
	public List<AreaVO> getConsumer(String typeCode) {
		String consumer = "consumer";
		int totalPeople;
		List<AreaVO> list;
		List<AreaVO> consumerList;
		if (Long.parseLong(typeCode) > 4) {
			//区域最新总人数
			totalPeople = tencentMapper.getAreaPeople(typeCode);
			//区域消费能力画像
			consumerList = tencentMapper.getAreaImage(consumer, typeCode);
			list = TouristImageDataHandler.commonHandlerData(consumerList, totalPeople);
		} else {
			//三亚市和四大区域
			//区划最新总人数
			totalPeople = tencentMapper.getZonePeople(typeCode);
			//区划持车画像
			consumerList = tencentMapper.getZoneImage(consumer, typeCode);
			list = TouristImageDataHandler.commonHandlerData(consumerList, totalPeople);
		}
		list.forEach(item -> {
			if ("次低".equals(item.getName())){
				item.setName("较低");
			}
			if ("次高".equals(item.getName())){
				item.setName("较高");
			}
		});
		return list;
	}


	@Override
	public List<AreaVO> getShopping(String typeCode) {
		List<AreaVO> list = new ArrayList<>();
		if (Long.parseLong(typeCode) > 4) {
			//区域最新总人数
			Integer areaTotalPeople = tencentMapper.getAreaPeople(typeCode);
			List<AreaVO> areaShoppingList = tencentMapper.getAreaImage("shopping", typeCode);
			for (AreaVO areaVO : areaShoppingList) {
				//百分比乘以人数  得到每一项所占的人数
				BigDecimal multiply = areaVO.getValue().multiply(new BigDecimal(areaTotalPeople.toString()));
				//百分比乘以100 四舍五入保留小数点后两位
				areaVO.setValue(areaVO.getValue().multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_UP));
				areaVO.setPeople(multiply.intValue());
				list.add(areaVO);
			}
			//list = TouristImageDataHandler.commonHandlerData(areaShoppingList, areaTotalPeople);
		} else {
			//区划最新总人数
			Integer zoneTotalPeople = tencentMapper.getZonePeople(typeCode);
			List<AreaVO> zoneShoppingList = tencentMapper.getZoneImage("shopping", typeCode);
			list = TouristImageDataHandler.commonHandlerData(zoneShoppingList, zoneTotalPeople);
		}
		list.forEach(item -> {
			if ("家具家居建材".equals(item.getName())) {
				item.setName("其它类型");
			}
		});
		int index = getLocation(list);
		Collections.swap(list, index, list.size() - 1);
		return list;
	}


	@Override
	public List<AreaVO> getFinance(String typeCode) {
		String finance = "finance";
		int totalPeople;
		List<AreaVO> list;
		List<AreaVO> financeList;
		if (Long.parseLong(typeCode) > 4) {
			//区域最新总人数
			totalPeople = tencentMapper.getAreaPeople(typeCode);
			//区域消费能力画像
			financeList = tencentMapper.getAreaImage(finance, typeCode);
			list = TouristImageDataHandler.commonHandlerData(financeList, totalPeople);
		}else {
			//区划
			//区划最新总人数
			Integer zoneTotalPeople = tencentMapper.getZonePeople(typeCode);
			//区划理财画像
			List<AreaVO> areaFinanceList = tencentMapper.getZoneImage(finance, typeCode);
			list = TouristImageDataHandler.commonHandlerData(areaFinanceList, zoneTotalPeople);
		}
		return list;
	}


	@Override
	public List<AreaVO> getLife(String typeCode) {
		String life = "life";
		int totalPeople;
		List<AreaVO> list;
		List<AreaVO> lifeList;
		if (Long.parseLong(typeCode) > 4) {
			//区域最新总人数
			totalPeople = tencentMapper.getAreaPeople(typeCode);
			//区域消费能力画像
			lifeList = tencentMapper.getAreaImage(life, typeCode);
			list = TouristImageDataHandler.commonHandlerData(lifeList, totalPeople);
		}else {
			//区划
			//区划最新总人数
			Integer zoneTotalPeople = tencentMapper.getZonePeople(typeCode);

			//区划人生阶段画像
			List<AreaVO> areaLifeList = tencentMapper.getZoneImage(life, typeCode);
			list = TouristImageDataHandler.commonHandlerData(areaLifeList, zoneTotalPeople);
		}
		List<AreaVO> collect = list.stream().filter(name -> name.getName().contains("学生")).collect(Collectors.toList());
		//学生总人数
		int studentTotalPeople = collect.stream().mapToInt(AreaVO::getPeople).sum();
		BigDecimal studentTotalPercent = collect.stream().map(AreaVO::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
		list.removeIf(item -> item.getName().contains("学生"));
		AreaVO areaVO = new AreaVO();
		areaVO.setName("学生");
		areaVO.setValue(studentTotalPercent);
		areaVO.setPeople(studentTotalPeople);
		list.add(areaVO);
		return list;
	}


	/**
	 * 返回指定对象的索引
	 *
	 * @param list 集合对象
	 * @return 索引
	 */
	private int getLocation(List<AreaVO> list) {
		int index = 0;
		for (int i = 0; i < list.size(); i++) {
			if ("其它类型".equals(list.get(i).getName())) {
				index = i;
			}
		}
		return index;
	}

	@Override
	public JSONObject getAreaStayDay(String typeCode) {
		//1-3,4-7,8-10,10
		List<String> listMonth = new ArrayList<>();
		//1-3
		List<Double> listOneToThree = new ArrayList<>();
		//4-7
		List<Double> listFourToSeven = new ArrayList<>();
		//8-10
		List<Double> listEight = new ArrayList<>();
		//10以上
		List<Double> listTen = new ArrayList<>();

		List<AreaStayDayEntity> areaStayDayEntityList = areaStayDayMapper.stayDay(typeCode);
		List<AreaStayDayEntity> collect = areaStayDayEntityList.stream().sorted(Comparator.comparing(AreaStayDayEntity::getCreateTime).reversed()).limit(12).sorted(Comparator.comparing(AreaStayDayEntity::getCreateTime)).collect(Collectors.toList());

		if (collect.size() > 0) {
			for (AreaStayDayEntity entity : collect) {
				int month = entity.getCreateTime().getMonthValue();
				listMonth.add(month + "月");

				double num = 0.00;
				//1-3,4-7,8-10,10
				double v1 = Double.parseDouble(entity.getValue1());
				double v2 = Double.parseDouble(entity.getValue2());
				double v3 = Double.parseDouble(entity.getValue3());
				double v4 = Double.parseDouble(entity.getValue4());
				double v5 = Double.parseDouble(entity.getValue5());
				double v6 = Double.parseDouble(entity.getValue6());
				double v7 = Double.parseDouble(entity.getValue7());
				double v8 = Double.parseDouble(entity.getValue8());
				double v9 = Double.parseDouble(entity.getValue9());
				double v10 = Double.parseDouble(entity.getValue10());
				double v11 = Double.parseDouble(entity.getValue11());
				num = v1 + v2 + v3 + v4 + v5 + v6 + v7 + v8 + v9 + v10 + v11;

				String perOne = decimalFormat(v1 + v2 + v3 / num);//1-3
				String perFour = decimalFormat(v4 + v5 + v6 + v7 / num);//4-7
				String perEight = decimalFormat(v8 + v9 + v10 / num);//8-10
				String perTen = decimalFormat(v11 / num);//10天以上


				listOneToThree.add(Double.parseDouble(perOne.substring(0, perOne.length() - 1)));
				listFourToSeven.add(Double.parseDouble(perFour.substring(0, perFour.length() - 1)));
				listEight.add(Double.parseDouble(perEight.substring(0, perEight.length() - 1)));
				listTen.add(Double.parseDouble(perTen.substring(0, perTen.length() - 1)));

			}
		}
		JSONObject jsonObject = new JSONObject();
		JSONArray arrayMonth = JSONArray.parseArray(JSON.toJSONString(listMonth));
		jsonObject.put("categories", arrayMonth);

		List<Map<String, Object>> listMap = new ArrayList<>();
		Map<String, Object> mapOneThree = methodMap(listOneToThree, "1");//1 代表1-3天
		Map<String, Object> mapFourSeven = methodMap(listFourToSeven, "2");//2 代表 4-7天
		Map<String, Object> mapEightTen = methodMap(listEight, "3");//3 代表8-10天
		Map<String, Object> mapTen = methodMap(listTen, "4");//4 代表10天以上
		listMap.add(mapOneThree);
		listMap.add(mapFourSeven);
		listMap.add(mapEightTen);
		listMap.add(mapTen);
		jsonObject.put("series", listMap);

		return jsonObject;
	}


	private Map<String, Object> methodMap(List<Double> list, String type) {
		//1-3,4-7,8-10,10
		Map<String, Object> map = new HashMap<>();
		//1 代表1-3天
		if ("1".equals(type)) {
			map.put("name", "1-3天");
			//2 代表 4-7天
		} else if ("2".equals(type)) {
			map.put("name", "4-7天");
			//3 代表8-10天
		} else if ("3".equals(type)) {
			map.put("name", "8-10天");
			//4 代表10天以上
		} else if ("4".equals(type)) {
			map.put("name", "10天以上");
		}
		map.put("stack", "总量");
		map.put("data", list);
		return map;
	}


	@Override
	public JSONObject getPopulation() {
		JSONObject jsonObject = new JSONObject();
		String population = tencentMapper.getPopulation();
		DecimalFormat decimalFormat = new DecimalFormat("00000000");
		String format = decimalFormat.format(Integer.parseInt(population));
		jsonObject.put("value", format);
		return jsonObject;
	}

	@Override
	public Map<String, Object> getAreaPopulation() {
		//0三亚市 1海棠区 2吉阳区 3天涯区 4崖州区
		List<AreaPopulationVO> areaPopulation = tencentMapper.getAreaPopulation();
		List<AreaPopulationVO> list = new ArrayList<>();
		for (AreaPopulationVO areaPopulationVO : areaPopulation) {
			AreaPopulationVO vo = new AreaPopulationVO();
			if (areaPopulationVO.getTypeCode() == 1) {
				vo.setProperty("海棠区");
				vo.setPercent(areaPopulationVO.getPercent());
				list.add(vo);
			}
			if (areaPopulationVO.getTypeCode() == 2) {
				vo.setProperty("吉阳区");
				vo.setPercent(areaPopulationVO.getPercent());
				list.add(vo);
			}
			if (areaPopulationVO.getTypeCode() == 3) {
				vo.setProperty("天涯区");
				vo.setPercent(areaPopulationVO.getPercent());
				list.add(vo);
			}
			if (areaPopulationVO.getTypeCode() == 4) {
				vo.setProperty("崖州区");
				vo.setPercent(areaPopulationVO.getPercent());
				list.add(vo);
			}
		}
		List<String> nameList = new ArrayList<>();
		//根据人数排序
		List<AreaPopulationVO> collect = list.stream().sorted(Comparator.comparing(AreaPopulationVO::getPercent).reversed()).collect(Collectors.toList());
		for (AreaPopulationVO vo : collect) {
			nameList.add(vo.getProperty());
		}
		Map<String, Object> map = new HashMap<>(16);
		map.put("list", list);
		map.put("names", nameList);
		return map;
	}

	@Override
	public List<AreaNameVO> areaRanking() {
		List<String> types = tencentMapper.areaRanking();
		List<AreaNameVO> list = new ArrayList<>();
		for (String type : types) {
			AreaNameVO areaNameVO = new AreaNameVO();
			if ("haiTangArea".equals(type)) {
				areaNameVO.setAreaName("海堂区");
				list.add(areaNameVO);
			}
			if ("jiYangArea".equals(type)) {
				areaNameVO.setAreaName("吉阳区");
				list.add(areaNameVO);
			}
			if ("tianYaArea".equals(type)) {
				areaNameVO.setAreaName("天涯区");
				list.add(areaNameVO);
			}
			if ("yaZhouArea".equals(type)) {
				areaNameVO.setAreaName("崖州区");
				list.add(areaNameVO);
			}
		}
		return list;
	}

	@Override
	public JSONObject migration(Integer type, Integer typeId) {
		String typeName = "";
		if (type == 10) {
			typeName = "in";
		}
		if (type == 11) {
			typeName = "out";
		}
		//1航空  2铁路 3公路
		MigrationEntity migration = tencentMapper.migration(typeName);
		if (typeId == 1) {
			String plane = migration.getPlane();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("value", decimalFormat(Double.parseDouble(plane)));
			return jsonObject;
		}
		if (typeId == 2) {
			String train = migration.getTrain();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("value", decimalFormat(Double.parseDouble(train)));
			return jsonObject;
		}
		if (typeId == 3) {
			String car = migration.getCar();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("value", decimalFormat(Double.parseDouble(car)));
			return jsonObject;
		}
		return null;
	}

	@Override
	public JSONObject entryAndExit(Integer typeCode, Integer type) {
		String percent = "";
		String s = tencentMapper.entryAndExit(typeCode);
		if (type == 2) {//代表省外
			Double d_100 = 100.00;
			double sd = Double.parseDouble(s);//省内
			String prov = decimalFormat(sd);//省内百分比
			String subs = prov.substring(0, prov.length() - 1);
			Double subsDouble = Double.valueOf(subs);
			double d_out = d_100 - subsDouble;
			percent = d_out + "%";//省外百分比  100 - 省内的百分比 = 省外的百分比
		} else {
			//省内
			percent = decimalFormat(Double.parseDouble(s));
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("value", percent);
		return jsonObject;
	}

	@Resource
	private TxBackstageTencentMapper backstageTencentMapper;

	@Resource
	private AnalysisMapper analysisMapper;

	@Override
	public JSONObject sourcePlace() {
		String startTime = "";
		String endTime = "";
		int sanyaToltalPeople = 0;
		List<PeopleVo> sourcePeople = backstageTencentMapper.getSourcePeople(startTime, endTime);
		for (PeopleVo vo : sourcePeople) {
			if ("0".equals(vo.getTypeCode())) {
				sanyaToltalPeople = vo.getPeople();
			}
		}

		List<ZoneSourcePlaceVO> list = backstageTencentMapper.zoneSourcePlace(startTime, endTime);
		List<SanYaAnalysisOrigin> originList = new ArrayList<>();

		for (ZoneSourcePlaceVO vo : list) {
			SanYaAnalysisOrigin origin = new SanYaAnalysisOrigin();
			origin.setName(vo.getProvinceName());
			String sanyaSum = vo.getSanyaSumPercent();
			double sanyaPeople = Convert.convert(Double.class, sanyaSum) * Convert.convert(Double.class, sanyaToltalPeople);
			double sanyaPercent = Convert.convert(Double.class, sanyaPeople) / Convert.convert(Double.class, sanyaToltalPeople);
			origin.setPercent(new DecimalFormat("#.##%").format(sanyaPercent));
			origin.setPeopleNum(new DecimalFormat("#").format(sanyaPeople));
			originList.add(origin);
		}
		originList.sort((o1, o2) -> Integer.parseInt(o2.getPeopleNum()) - Integer.parseInt(o1.getPeopleNum()));

//		String typeCode = "0";
//		SourcePlaceVO sourcePlaceVO = backstageTencentMapper.sourceSanyaNewTime(typeCode);
//		String name = sourcePlaceVO.getName();//省份和百分比  天津市-0.0121
//		String code = sourcePlaceVO.getTypeCode();
//		String time = sourcePlaceVO.getTime();
//		String subTime = time.substring(0, 10);
//		String toPeople = analysisMapper.seByTypeCodeAndTypa(code, subTime);//获取对应的人数
//		if (StringUtils.isEmpty(toPeople)) {
//			return null;
//		}
//		List<String> list = methodOne(name, 1);//拿到去重后省份的总和 只取排名前十的省份
//		List<String> listAll = methodOne(name, 2);//拿到去重后省份的总和 取所有
//		List<SanYaAnalysisOrigin> listRank = mePlace(list, toPeople);
//		List<SanYaAnalysisOrigin> listAnalysis = mePlace(listAll, toPeople);
//
		List<SanYaAnalysisOrigin> listRank = originList.stream().limit(10).collect(Collectors.toList());

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("listRank", listRank);
		jsonObject.put("listAnalysis", originList);
		return jsonObject;
	}

	//抽取出来的方法
	public List<SanYaAnalysisOrigin> mePlace(List<String> list, String toPeople) {
		List<SanYaAnalysisOrigin> list_rank = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			String[] split = list.get(i).split("-");
			BigDecimal bigDecimal1 = new BigDecimal(split[1]);
			BigDecimal bigDecimal2 = new BigDecimal(toPeople);
			BigDecimal multiply = bigDecimal1.multiply(bigDecimal2);
			int inPeople = multiply.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();//省对应的人数 整数

			double doPeople = Double.parseDouble(toPeople);
			Double aDouble = Double.valueOf(inPeople);
			String perce = decimalFormat(aDouble / doPeople);//省对应的百分比

			SanYaAnalysisOrigin so = new SanYaAnalysisOrigin();
			so.setName(split[0]);
			so.setPeopleNum(inPeople + "");
			so.setPercent(perce);
			list_rank.add(so);
		}
		return list_rank;
	}

	@Override
	public List<HeatingValueVO> getHeatingValue(String areaCode) {
		//redis中的key名
		String redisName = "heatingValue";
		List<HeatingValueVO> heatingValueVOList = new ArrayList<>();
		//传递了area_code
		if (!"0".equals(areaCode)) {
			List<AreaHeatingValue> areaHeatingValues = areaHeatingValueMapper.seByAreaCode(areaCode);
			for (AreaHeatingValue areaHeatingValue : areaHeatingValues) {
				List<Double> list = new ArrayList<>();
				HeatingValueVO heatingValueVO = new HeatingValueVO();
				//坐标转换，腾讯地图转换成百度地图坐标
				Map<String, Object> map = MapUtils.mapTxToBd(areaHeatingValue.getLat(), areaHeatingValue.getLng());
				Double lon = (Double) map.get("lon");
				list.add(lon);
				Double lat = (Double) map.get("lat");
				list.add(lat);
				heatingValueVO.setCoord(list);
				heatingValueVO.setElevation(Integer.parseInt(areaHeatingValue.getWeight()));
				heatingValueVOList.add(heatingValueVO);
			}
		} else {
			//没传area_code 查询三亚市
			if (redisTemplate.hasKey(redisName)) {
				heatingValueVOList = redisTemplate.opsForList().range(redisName, 0, -1);
			} else {
				Date data = heatingValueMapper.seleteOneHeatingValue();
				//获取最新时间-4分钟
				int minute = -4;
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(data);
				calendar.add(Calendar.MINUTE, minute);
				String startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

				List<HeatingValue> valueList = heatingValueMapper.newestSelete(startDate, data.toString());
				for (HeatingValue heatingValue : valueList) {
					List<Double> list = new ArrayList<>();
					HeatingValueVO heatingValueVO = new HeatingValueVO();
					list.add(heatingValue.getLng());
					list.add(heatingValue.getLat());
					heatingValueVO.setCoord(list);
					heatingValueVO.setElevation(heatingValue.getCount());
					heatingValueVOList.add(heatingValueVO);
				}
				if (redisTemplate.hasKey(redisName)) {
					redisTemplate.delete(redisName);
				}
				redisTemplate.opsForList().rightPushAll(redisName, heatingValueVOList);
			}
		}
		return heatingValueVOList;
	}

	@Override
	public JSONObject getDistance(Integer typeCode, Integer type) {
		if (typeCode > 4) {
			typeCode = 0;
		}
		JSONObject jsonObject = new JSONObject();
		List<AnalysisVO> distance = tencentMapper.getDistance(typeCode);
		for (AnalysisVO analysisVO : distance) {
			if (type == 1 && "无旅行".equals(analysisVO.getName())) {
				Double value = Double.parseDouble(analysisVO.getValue());
				jsonObject.put("value", new DecimalFormat("#.##%").format(value));
				return jsonObject;
			}
			if (type == 2 && "短途".equals(analysisVO.getName())) {
				Double value = Double.parseDouble(analysisVO.getValue());
				jsonObject.put("value", new DecimalFormat("#.##%").format(value));
				return jsonObject;
			}
			if (type == 3 && "中途".equals(analysisVO.getName())) {
				Double value = Double.parseDouble(analysisVO.getValue());
				jsonObject.put("value", new DecimalFormat("#.##%").format(value));
				return jsonObject;
			}
			if (type == 4 && "长途".equals(analysisVO.getName())) {
				Double value = Double.parseDouble(analysisVO.getValue());
				jsonObject.put("value", new DecimalFormat("#.##%").format(value));
				return jsonObject;
			}
		}
		return null;
	}


	@Override
	public JSONObject tourVisiting() {
		List<String> listMonth = new ArrayList<>();
		List<Integer> monthValue = new ArrayList<>();

		List<Map<String, String>> listMap = analysisPopulationMapper.visitList();
		for (Map<String, String> maps : listMap) {
			for (Map.Entry<String, String> map : maps.entrySet()) {
				String key = map.getKey();
				if ("time".equals(key)) {
					LocalDate parse = LocalDate.parse(map.getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
					int month = parse.getMonthValue();
					int day = parse.getDayOfMonth();
					listMonth.add(month + "/" + day);
				}
				if ("percent".equals(key)) {
					monthValue.add(Integer.parseInt(map.getValue()));
				}
			}
		}
		JSONObject jsonObject = new JSONObject();
		JSONArray arrayMonth = JSONArray.parseArray(JSON.toJSONString(listMonth));
		jsonObject.put("categories", arrayMonth);

		List<Map<String, Object>> serieslistMap = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("name", "每日存量访客");
		map.put("data", monthValue);
		serieslistMap.add(map);
		jsonObject.put("series", serieslistMap);
		return jsonObject;
	}

	@Override
	public Map<String, Object> getAreaBusiness(String type) {
		Map<String, Object> map = new HashMap<>(16);
		List<AreaBusinessVO> list = new ArrayList<>();
		List<AreaBusinessVO> areaBusiness = tencentMapper.getAreaBusiness();
		for (AreaBusinessVO business : areaBusiness) {
			//景点
			if ("1".equals(type)) {
				Map<String, String> areaMap = AreaCodeUtils.areaMap();
				areaMap.forEach((k, v) -> {
					AreaBusinessVO areaBusinessVO = new AreaBusinessVO();
					if (business.getAreaCode().equals(v)) {
						areaBusinessVO.setAreaName(k);
						areaBusinessVO.setPeople(business.getPeople());
						areaBusinessVO.setAreaCode(business.getAreaCode());
						list.add(areaBusinessVO);
					}
				});
			}
			//商圈
			if ("2".equals(type)) {
				Map<String, String> businessMap = AreaCodeUtils.businessMap();
				businessMap.forEach((k, v) -> {
					AreaBusinessVO areaBusinessVO = new AreaBusinessVO();
					if (business.getAreaCode().equals(v)) {
						areaBusinessVO.setAreaName(k);
						areaBusinessVO.setPeople(business.getPeople());
						areaBusinessVO.setAreaCode(business.getAreaCode());
						list.add(areaBusinessVO);
					}
				});
			}
		}
		//根据人数倒序取前5个名称
		List<AreaBusinessVO> collect = list.stream().sorted(Comparator.comparing(AreaBusinessVO::getPeople).reversed()).limit(5).collect(Collectors.toList());
		map.put("list", list);
		map.put("names", collect.stream().map(AreaBusinessVO::getAreaName).collect(Collectors.toList()));
		return map;
	}

	/**
	 * 以百分比方式计数
	 *
	 * @param percent 百分比
	 */
	public static String decimalFormat(double percent) {
		DecimalFormat df = new DecimalFormat("#.##%");
		return df.format(percent);
	}


	/**
	 * 对传递过来的字符串去重并取10个省份
	 *
	 * @param str
	 * @return
	 */
	public List<String> methodOne(String str, Integer itg) {
		//海南省-0.5545,海南省-0.1902,湖南省-0.0585,海南省-0.0228,河北省-0.0211,山西省-0.0195,河南省-0.0114,广东省-0.0098,海南省-0.0098,
		// 重庆市-0.0081,海南省-0.0081,江苏省-0.0081,湖南省-0.0081,海南省-0.0065,山东省-0.0049,广西壮族自治区-0.0049,海南省-0.0049,
		// 河北省-0.0049,浙江省-0.0049,福建省-0.0049,海南省-0.0033,河南省-0.0033,广东省-0.0033,海南省-0.0033,海南省-0.0016,北京市-0.0016,
		// 海南省-0.0016,山西省-0.0016,云南省-0.0016,广东省-0.0016,浙江省-0.0016,黑龙江省-0.0016,贵州省-0.0016,山东省-0.0016,海南省-0.0016,
		// 贵州省-0.0016,福建省-0.0016
		List<String> allList = new ArrayList<>();

		String[] sp = str.split(",");
		List<String> lit = Arrays.asList(sp);
		List<String> split = new ArrayList(lit);

		while (true) {//对每个省份进行依次去重获取最大值，直到最后一个
			Map<String, Object> map = methodSplit(split);
			String value = (String) map.get("value");
			if (!StringUtils.isEmpty(value)) {
				allList.add(value);
				List<String> reList = (List<String>) map.get("split");
				if (reList.size() > 0) {
					split = reList;
				}
			} else {
				break;
			}
		}
		List<SpTemporary> listSp = new ArrayList<>();
		//对所有省份进行排序 取高的10个
		for (String oneStr : allList) {
			String[] oneSplit = oneStr.split("-");
			SpTemporary spTemporary = new SpTemporary();
			spTemporary.setName(oneSplit[0]);
			spTemporary.setNameValue(new BigDecimal(oneSplit[1]));
			listSp.add(spTemporary);
		}
		Collections.sort(listSp, new Comparator<SpTemporary>() {
			@Override
			public int compare(SpTemporary o1, SpTemporary o2) {
				//降序
				return o2.getNameValue().compareTo(o1.getNameValue());
			}
		});
		//重新组装数据
		List<String> allListNew = new ArrayList<>();
		//为1取排名前十的省份
		if (itg == 1) {
			int num = 0;
			for (SpTemporary spTemporary : listSp) {
				if (num == 10) {
					break;
				}
				String name = spTemporary.getName();
				BigDecimal nameValue = spTemporary.getNameValue();
				String value = name + "-" + nameValue;
				allListNew.add(value);
				num += 1;
			}
		} else {//取所有省份
			for (SpTemporary spTemporary : listSp) {
				String name = spTemporary.getName();
				BigDecimal nameValue = spTemporary.getNameValue();
				String value = name + "-" + nameValue;
				allListNew.add(value);
			}
		}
		return allListNew;
	}


	/**
	 * 对省份集合 进行去重 并回去最大值。返回一个省份的最大值和剩余的省份
	 *
	 * @param split
	 * @return
	 */
	public static Map<String, Object> methodSplit(List<String> split) {
		//把相同省份装进集合
		List<String> list = new ArrayList<>();
		String provinceName = null;
		for (int i = 0; i < split.size(); i++) {
			if (split.get(i).contains("-") && split.get(i).substring(split.get(i).indexOf("-") + 1, split.get(i).length()).length() > 0) {//校验数据的正确性
				String[] split1 = split.get(i).split("-");
				String name = split1[0];
				if (StringUtils.isEmpty(provinceName)) {
					provinceName = name;
					list.add(split.get(i));
					split.remove(split.get(i));
					i--;
				} else {//不为空
					if (provinceName.equals(name)) {
						list.add(split.get(i));
						split.remove(split.get(i));
						i--;
					}
				}
			}
		}
		//获取最大值
		String name = "";
		BigDecimal value = new BigDecimal(0);
		for (String s : list) {
			String[] split1 = s.split("-");
			if (StringUtils.isEmpty(name)) {
				name = split1[0];
			}
			String va = split1[1];
			BigDecimal bigDecimal = new BigDecimal(va);
			//计算相同省份的总和
			value = value.add(bigDecimal);
		}
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isEmpty(name)) {
			map.put("value", null);
		} else {
			map.put("value", name + "-" + value);
		}
		map.put("split", split);
		return map;
	}
}
