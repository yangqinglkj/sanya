package org.springblade.modules.backstage.service.lmpl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.common.entity.*;
import org.springblade.common.entity.tx.AreaStayDayEntity;
import org.springblade.common.entity.tx.MigrationEntity;
import org.springblade.common.entity.tx.analysis.AnalysisEntity;
import org.springblade.common.entity.tx.analysis.AnalysisPopulationEntity;
import org.springblade.common.mapper.analysis.AnalysisMapper;
import org.springblade.common.mapper.analysis.AnalysisOriginMapper;
import org.springblade.common.mapper.analysis.AnalysisPopulationMapper;
import org.springblade.common.mapper.tx.AreaStayDayMapper;
import org.springblade.common.mapper.tx.MigrationMapper;
import org.springblade.common.utils.AreaCodeUtils;
import org.springblade.common.vo.AreaVO;
import org.springblade.common.vo.MigrationVO;
import org.springblade.modules.backstage.mapper.TxBackstageTencentMapper;
import org.springblade.modules.backstage.service.TouristImageDataHandler;
import org.springblade.modules.backstage.service.TxBackstageTencentService;
import org.springblade.modules.backstage.vo.TimeVO;
import org.springblade.modules.backstage.vo.tx.PeopleVo;
import org.springblade.modules.backstage.vo.tx.SourcePlaceVO;
import org.springblade.modules.backstage.vo.tx.ZoneSourcePlaceVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import sun.font.FontDesignMetrics;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author yq
 * @Date 2020/9/17 15:40
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class TxBackstageTencentServiceImpl implements TxBackstageTencentService {

	@Resource
	private TxBackstageTencentMapper backstageTencentMapper;

	@Resource
	private AnalysisMapper analysisMapper;
	@Resource
	private AnalysisOriginMapper analysisOriginMapper;
	@Resource
	private AnalysisPopulationMapper analysisPopulationMapper;
	@Resource
	private AreaStayDayMapper areaStayDayMapper;
	@Resource
	private MigrationMapper migrationMapper;


	private static final List<String> propertyList = Arrays.asList("gender", "car", "age", "education", "consumer", "shopping", "finance", "life");

	public List<AreaVO> methodAge(List<AreaVO> areaList) {
		List<BigDecimal> otherList = new ArrayList<>();
		List<AreaVO> list = new ArrayList<>();
		for (AreaVO areaVO : areaList) {
			if (">=70".equals(areaVO.getName()) || "other".equals(areaVO.getName())) {
				otherList.add(areaVO.getValue());
			} else {
				list.add(areaVO);
			}
		}
		AreaVO vo = new AreaVO();
		vo.setName("70岁以上");
		vo.setValue(otherList.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
		list.add(vo);
		return list;
	}


	@Override
	public Map<String, Object> touristImage(String typeCode, String startTime, String endTime) {
		//总人数
		List<Integer> totalPeople = backstageTencentMapper.getTotalPeople(typeCode, startTime, endTime);
		int sum = totalPeople.stream().mapToInt(x -> x).sum();
		Map<String, Object> map = new HashMap<>(16);
		for (String property : propertyList) {
			List<AreaVO> areaList = backstageTencentMapper.touristImageNew(property, typeCode, startTime, endTime, totalPeople.size());
			if ("age".equals(property)) {
				areaList = methodAge(areaList);
			}
			if ("shopping".equals(property)) {
				getShopping(areaList);
			}
			if ("car".equals(property)) {
				areaList.forEach(car -> {
					if ("没车".equals(car.getName())) {
						car.setName("无车");
					}
				});
			}
			if ("education".equals(property)) {
				areaList.forEach(vo -> {
					if ("博士".equals(vo.getName())) {
						vo.setName("博士及以上");
					}
				});
			}
			if ("life".equals(property)) {
				areaList = getLife(areaList);
			}
			map.put(property, TouristImageDataHandler.commonHandlerData(areaList, sum));
		}
		return map;
	}

	private List<AreaVO> getLife(List<AreaVO> areaList) {
		List<BigDecimal> studentPercent = new ArrayList<>();
		List<AreaVO> list = new ArrayList<>();
		areaList.forEach(areaVO -> {
			if (areaVO.getName().contains("学生")) {
				studentPercent.add(areaVO.getValue());
			} else {
				list.add(areaVO);
			}
		});
		AreaVO vo = new AreaVO();
		vo.setName("学生");
		vo.setValue(studentPercent.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
		list.add(vo);
		return list;
	}


	private void getShopping(List<AreaVO> list) {
		list.forEach(shopping -> {
			if ("家具家居建材".equals(shopping.getName())) {
				shopping.setName("其它类型");
			}
		});
	}


	@Override
	public Map<String, Object> touristVolume(IPage<AnalysisEntity> page, String typeCode, String startTime, String endTime) {
		int count = 0;
		//开始时间  结束时间都为空 查询当前时间往前推30天的数据
		if (StringUtils.isEmpty(startTime) && StringUtils.isEmpty(endTime)) {
			endTime = LocalDate.now().toString();
			startTime = LocalDate.now().minusDays(15).toString();
			count = 1;
		}
		Map<String, Object> map = new HashMap<>(16);
		//折线图数据
		List<Map<String, Object>> list = analysisMapper.touristVolume(typeCode, startTime, endTime);
		List<Map<String, Object>> newList = new ArrayList<>();
		for (Map<String, Object> touristMap : list) {
			Map<String, Object> newMap = new HashMap<>(16);
			String createTime = (String) touristMap.get("createTime");
			LocalDate localDate = LocalDate.parse(createTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			int dayOfMonth = localDate.getDayOfMonth();
			int monthValue = localDate.getMonthValue();
			newMap.put("createTime", monthValue + "/" + dayOfMonth);
			newMap.put("percent", touristMap.get("percent"));
			newList.add(newMap);
		}
		//重新给时间赋值
		if (count == 1) {
			startTime = null;
			endTime = null;
		}
		//表格数据
		IPage<AnalysisEntity> tablePage = analysisMapper.touristTable(page, typeCode, startTime, endTime);
		TimeVO time = analysisMapper.getTime(typeCode, "population");
		map.put("mouth", newList);
		map.put("page", tablePage);
		map.put("maxTime", time.getMaxTime());
		map.put("minTime", time.getMinTime());
		return map;
	}


	@Override
	public Map<String, Object> travelDistance(IPage<TravelDistance> page, String typeCode, String startTime, String endTime) {
		if (!StringUtils.isEmpty(endTime) && endTime.length() == 10) {
			endTime += " 23:59:59";
		}
		IPage<TravelDistance> travelDistancePage = analysisMapper.travelDistance(page, typeCode, startTime, endTime);
		List<TravelDistance> entityList = travelDistancePage.getRecords();
		for (TravelDistance entity : entityList) {
			//当天总人数
			Integer totalPeople = entity.getTotalPeople();
			//近途百分比
			Double nearlyWayPercent = Double.parseDouble(entity.getNearlyWayPercent());
			//短途百分比
			Double shortPercent = Double.parseDouble(entity.getShortPercent());
			//中途百分比
			Double halfwayPercent = Double.parseDouble(entity.getHalfwayPercent());
			//长途百分比
			Double longPercent = Double.parseDouble(entity.getLongPercent());

			Double convert = Convert.convert(Double.class, totalPeople);
			//近途人数
			String nearlyWayPeople = new DecimalFormat("#").format(nearlyWayPercent * convert);
			//短途人数
			String shortPeople = new DecimalFormat("#").format(shortPercent * convert);
			//中途人数
			String halfwayPeople = new DecimalFormat("#").format(halfwayPercent * convert);
			//长途人数
			String longPeople = new DecimalFormat("#").format(longPercent * convert);

			entity.setNearlyWayPeople(nearlyWayPeople);
			entity.setNearlyWayPercent(new DecimalFormat("#.##%").format(nearlyWayPercent));
			entity.setShortPeople(shortPeople);
			entity.setShortPercent(new DecimalFormat("#.##%").format(shortPercent));
			entity.setHalfwayPeople(halfwayPeople);
			entity.setHalfwayPercent(new DecimalFormat("#.##%").format(halfwayPercent));
			entity.setLongPeople(longPeople);
			entity.setLongPercent(new DecimalFormat("#.##%").format(longPercent));

		}
		Map<String, Object> map = new HashMap<>(16);
		TimeVO time = analysisMapper.getTime(typeCode, "distance");
		map.put("option", travelDistancePage);
		map.put("maxTime", time.getMaxTime());
		map.put("minTime", time.getMinTime());
		return map;
	}


	@Override
	public Map<String, Object> touristReception(IPage<TouristReception> page, String startTime, String endTime, String typeCode) {
		IPage<TouristReception> receptionPage = analysisOriginMapper.touristReception(page, startTime, endTime, typeCode);
		List<TouristReception> records = receptionPage.getRecords();
		for (TouristReception record : records) {
			//省内百分比总和
			String provinceSumPercent = new DecimalFormat("#.####").format(Double.parseDouble(record.getProv()));
			//省外百分比总和
			String provinceOutSumPercent = new DecimalFormat("#.####").format(Double.parseDouble(record.getOutprov()));
			//每天的总人数
			Integer everyDayTotalPeople = record.getTotalPeople();

			//省内人数
			double provincePeople = Convert.convert(Double.class, provinceSumPercent) * Convert.convert(Double.class, everyDayTotalPeople);
			//省外人数
			double provinceOutPeople = Convert.convert(Double.class, provinceOutSumPercent) * Convert.convert(Double.class, everyDayTotalPeople);

			//省内百分比
			double provincePercent = provincePeople / Convert.convert(Double.class, everyDayTotalPeople);
			//省外百分比
			double provinceOutPercent = provinceOutPeople / Convert.convert(Double.class, everyDayTotalPeople);

			record.setCreateTime(record.getCreateTime());
			record.setTypeCode(record.getTypeCode());
			record.setProv(new DecimalFormat("#").format(provincePeople));
			record.setProvValue(new DecimalFormat("#.##%").format(provincePercent));

			record.setOutprov(new DecimalFormat("#").format(provinceOutPeople));
			record.setOutprovValue(new DecimalFormat("#.##%").format(provinceOutPercent));
		}
		Map<String, Object> map = new HashMap<>(16);
		TimeVO time = analysisOriginMapper.getTime(typeCode);
		map.put("option", receptionPage);
		map.put("maxTime", time.getMaxTime());
		map.put("minTime", time.getMinTime());
		return map;
	}


	@Override
	public Map<String, Object> touristVisiting(IPage<AnalysisPopulationEntity> page, String typeCode, String startTime, String endTime) {
		typeCode = "0";
		int count = 0;
		//开始时间  结束时间都为空 查询当前时间往前推30天的数据
		if (StringUtils.isEmpty(startTime) && StringUtils.isEmpty(endTime)) {
			endTime = LocalDate.now().toString();
			startTime = LocalDate.now().minusDays(15).toString();
			count = 1;
		}
		if (!StringUtils.isEmpty(endTime) && endTime.length() == 10) endTime += " 23:59:59";
		{
		}
		//折线图数据
		List<Map<String, Object>> list = analysisPopulationMapper.touristVisiting(typeCode, startTime, endTime);
		List<Map<String, Object>> newList = new ArrayList<>();
		for (Map<String, Object> map : list) {
			Map<String, Object> newMap = new HashMap<>(16);
			String createTime = (String) map.get("createTime");
			LocalDate localDate = LocalDate.parse(createTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			int dayOfMonth = localDate.getDayOfMonth();
			int monthValue = localDate.getMonthValue();
			newMap.put("createTime", monthValue + "/" + dayOfMonth);
			newMap.put("percent", map.get("number"));
			newList.add(newMap);
		}
		//重新给时间赋值
		if (count == 1) {
			startTime = null;
			endTime = null;
		}
		//表格数据
		IPage<AnalysisPopulationEntity> analysisPopulationEntityIPage = analysisPopulationMapper.sanYaAndTimePage(startTime, endTime, page, typeCode);
		TimeVO time = analysisPopulationMapper.getTime();
		Map<String, Object> map = new HashMap<>();
		map.put("mouth", newList);
		map.put("page", analysisPopulationEntityIPage);
		map.put("maxTime", time.getMaxTime());
		map.put("minTime", time.getMinTime());
		return map;
	}

	@Override
	public Map<String, Object> touristStop(IPage<AreaStayDayEntity> page, String typeCode, String startTime, String endTime) {
//		String sql = "";
//		//开始时间结束时间都为空
//		if (StringUtils.isEmpty(startTime) && StringUtils.isEmpty(endTime)) {
//			sql = " YEAR (create_time) = date_format(now(),'%Y') ";
//		}
//		//开始时间不为空  结束时间为空
//		if (!StringUtils.isEmpty(startTime) && StringUtils.isEmpty(endTime)) {
//			sql = " YEAR (create_time) = date_format('" + startTime + "','%Y') ";
//		}
//		//结束时间不为空  开始时间为空
//		if (!StringUtils.isEmpty(endTime) && StringUtils.isEmpty(startTime)) {
//			sql = " YEAR (create_time) = date_format('" + endTime + "','%Y') ";
//		}
//		//开始和结束时间都不为空
//		if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
//			sql = " YEAR (create_time) = date_format('" + endTime + "','%Y') ";
//		}
		Map<String, Object> reMap = new HashMap<>();
		//计算柱状图数据
		List<Map<String, Object>> list1 = new ArrayList<>();
		List<AreaStayDayEntity> enetityList = areaStayDayMapper.stayDaySanYa(typeCode, startTime, endTime);
		List<AreaStayDayEntity> collect = enetityList.stream().sorted(Comparator.comparing(AreaStayDayEntity::getCreateTime).reversed()).limit(12).sorted(Comparator.comparing(AreaStayDayEntity::getCreateTime)).collect(Collectors.toList());
		for (AreaStayDayEntity entity : collect) {
			List<Map<String, Object>> list = new ArrayList<>();
			Map<String, Object> map = new HashMap<>();
			Map<String, Object> map2 = new HashMap<>();
			Double num = 0.00;
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
			//1-3
			String perOne = decimalFormat(v1 + v2 + v3 / num);
			//4-7
			String perFour = decimalFormat(v4 + v5 + v6 + v7 / num);
			//8-10
			String perEight = decimalFormat(v8 + v9 + v10 / num);
			//10天以上
			String perTen = decimalFormat(v11 / num);

			map.put("mou", entity.getCreateTime().getMonthValue());
			map2.put("1-3天", perOne);
			map2.put("4-7天", perFour);
			map2.put("8-10天", perEight);
			map2.put("10天以上", perTen);
			list.add(map2);
			map.put("list", list);
			list1.add(map);
		}
		IPage<AreaStayDayEntity> areaStayDayEntityIPage = areaStayDayMapper.touristStop(page, typeCode, startTime, endTime);
		List<AreaStayDayEntity> records = areaStayDayEntityIPage.getRecords();
		List list = new ArrayList();
		for (AreaStayDayEntity entity : records) {
			TouristStop touristStop = new TouristStop();

			Double num = 0.00;
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

			//1-3
			String perOne = decimalFormat(v1 + v2 + v3 / num);
			//4-7
			String perFour = decimalFormat(v4 + v5 + v6 + v7 / num);
			String perEight = decimalFormat(v8 + v9 + v10 / num);//8-10
			String perTen = decimalFormat(v11 / num);//10天以上
			touristStop.setOneToThree(perOne);
			touristStop.setFourToSeven(perFour);
			touristStop.setEightToTen(perEight);
			touristStop.setTen(perTen);
			LocalDate createTime = entity.getCreateTime();

			touristStop.setCreateTime(createTime.getYear() + "年" + createTime.getMonthValue() + "月");
			touristStop.setTypeCode(entity.getTypeCode());
			list.add(touristStop);
		}
		areaStayDayEntityIPage.setRecords(list);
		TimeVO time = areaStayDayMapper.getTime();
		reMap.put("mouth", list1);
		reMap.put("page", areaStayDayEntityIPage);
		reMap.put("maxTime", time.getMaxTime());
		reMap.put("minTime", time.getMinTime());
		return reMap;
	}


	@Override
	public Map<String, Object> getMigration(IPage<MigrationEntity> page, String startTime, String endTime) {
		IPage<MigrationVO> migration = migrationMapper.getMigration(page, startTime, endTime);
		List<MigrationVO> records = migration.getRecords();
		for (MigrationVO record : records) {
			//迁入 汽车  飞机  火车 百分比
			String inCarPercent = new DecimalFormat("#.##%").format(Double.parseDouble(record.getInCarPercent()));
			String inPlanePercent = new DecimalFormat("#.##%").format(Double.parseDouble(record.getInPlanePercent()));
			String inTrainPercent = new DecimalFormat("#.##%").format(Double.parseDouble(record.getInTrainPercent()));
			//迁入 汽车  飞机  火车 人数
			double inCarNumber = Double.parseDouble(record.getInCarPercent()) * Double.parseDouble(record.getInPopulation());
			double inPlaneNumber = Double.parseDouble(record.getInPlanePercent()) * Double.parseDouble(record.getInPopulation());
			double inTrainNumber = Double.parseDouble(record.getInTrainPercent()) * Double.parseDouble(record.getInPopulation());
			//格式化迁入人数
			String inCarPopulation = new DecimalFormat("#").format(inCarNumber);
			String inPlanePopulation = new DecimalFormat("#").format(inPlaneNumber);
			String inTrainPopulation = new DecimalFormat("#").format(inTrainNumber);


			//迁出 汽车  飞机  火车 百分比
			String outCarPercent = new DecimalFormat("#.##%").format(Double.parseDouble(record.getOutCarPercent()));
			String outPlanePercent = new DecimalFormat("#.##%").format(Double.parseDouble(record.getOutPlanePercent()));
			String outTrainPercent = new DecimalFormat("#.##%").format(Double.parseDouble(record.getOutTrainPercent()));
			//迁出 汽车  飞机  火车 人数
			double outCarNumber = Double.parseDouble(record.getOutCarPercent()) * Double.parseDouble(record.getOutPopulation());
			double outPlaneNumber = Double.parseDouble(record.getOutPlanePercent()) * Double.parseDouble(record.getOutPopulation());
			double outTrainNumber = Double.parseDouble(record.getOutTrainPercent()) * Double.parseDouble(record.getOutPopulation());
			//格式化迁出人数
			String outCarPopulation = new DecimalFormat("#").format(outCarNumber);
			String outPlanePopulation = new DecimalFormat("#").format(outPlaneNumber);
			String outTrainPopulation = new DecimalFormat("#").format(outTrainNumber);

			//迁入信息
			record.setInCar(inCarPopulation);
			record.setInCarPercent(inCarPercent);
			record.setInPlane(inPlanePopulation);
			record.setInPlanePercent(inPlanePercent);
			record.setInTrain(inTrainPopulation);
			record.setInTrainPercent(inTrainPercent);
			//迁出信息
			record.setOutCar(outCarPopulation);
			record.setOutCarPercent(outCarPercent);
			record.setOutPlane(outPlanePopulation);
			record.setOutPlanePercent(outPlanePercent);
			record.setOutTrain(outTrainPopulation);
			record.setOutTrainPercent(outTrainPercent);
			LocalDate beginDateTime = LocalDate.parse(record.getCreateTime().toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			record.setCreateTime(beginDateTime);
		}
		Map<String, Object> map = new HashMap<>(16);
		TimeVO time = migrationMapper.getTime();
		map.put("option", migration);
		map.put("maxTime", time.getMaxTime());
		map.put("minTime", time.getMinTime());
		return map;
	}


	@Override
	public Map<String, Object> zoneSourcePlace(String startTime, String endTime) {
		long day = 1;
		if (org.apache.commons.lang3.StringUtils.isNotEmpty(startTime) && org.apache.commons.lang3.StringUtils.isNotEmpty(endTime)) {
			LocalDate start = LocalDate.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			LocalDate end = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			//计算时间天数差值
			long timeDifference = ChronoUnit.DAYS.between(start, end);
			//差值上加一天
			day += timeDifference;
		}

		List<PeopleVo> sourcePeople = backstageTencentMapper.getSourcePeople(startTime, endTime);
		int sanyaToltalPeople = 0;
		int haitangToltalPeople = 0;
		int jiyangToltalPeople = 0;
		int tianyaToltalPeople = 0;
		int yazhouToltalPeople = 0;
		for (PeopleVo vo : sourcePeople) {
			if ("0".equals(vo.getTypeCode())) {
				sanyaToltalPeople = vo.getPeople();
			}
			if ("1".equals(vo.getTypeCode())) {
				haitangToltalPeople = vo.getPeople();
			}
			if ("2".equals(vo.getTypeCode())) {
				jiyangToltalPeople = vo.getPeople();
			}
			if ("3".equals(vo.getTypeCode())) {
				tianyaToltalPeople = vo.getPeople();
			}
			if ("4".equals(vo.getTypeCode())) {
				yazhouToltalPeople = vo.getPeople();
			}
		}
		List<ZoneSourcePlaceVO> newList;

		List<ZoneSourcePlaceVO> list = backstageTencentMapper.zoneSourcePlace(startTime, endTime);
		//如果时间为空，查询最新的数据，取总百分比
		if ("".equals(startTime) && "".equals(endTime)) {
			newList = getOne(list, sanyaToltalPeople, haitangToltalPeople, jiyangToltalPeople, tianyaToltalPeople, yazhouToltalPeople);
			//如果开始时间和结束时间相同，查询最新的数据，取总百分比
		} else if (startTime.equals(endTime)) {
			newList = getOne(list, sanyaToltalPeople, haitangToltalPeople, jiyangToltalPeople, tianyaToltalPeople, yazhouToltalPeople);
			//时间范围筛选，查询时间范围筛选
		} else {
			newList = getList(list, sanyaToltalPeople, haitangToltalPeople, jiyangToltalPeople, tianyaToltalPeople, yazhouToltalPeople, day);
		}
		newList.sort((o1, o2) -> Integer.parseInt(o2.getSanyaPeople()) - Integer.parseInt(o1.getSanyaPeople()));

		Map<String, Object> map = new HashMap<>(16);
		map.put("list", newList);
		if (org.apache.commons.lang3.StringUtils.isEmpty(startTime) && org.apache.commons.lang3.StringUtils.isEmpty(endTime)) {
			String newTime = backstageTencentMapper.getNewTime();
			map.put("time", newTime);
		}
		TimeVO time = backstageTencentMapper.getTime();
		map.put("maxTime", time.getMaxTime());
		map.put("minTime", time.getMinTime());
		return map;
	}

	/**
	 * 计算最新时间的数据，取总百分比
	 *
	 * @param list                集合对象
	 * @param sanyaToltalPeople   三亚市总人数
	 * @param haitangToltalPeople 海棠区总人数
	 * @param jiyangToltalPeople  吉阳区总人数
	 * @param tianyaToltalPeople  天涯区总人数
	 * @param yazhouToltalPeople  崖州区总人数
	 * @return 计算后的集合
	 */
	private List<ZoneSourcePlaceVO> getOne(List<ZoneSourcePlaceVO> list, Integer sanyaToltalPeople, Integer haitangToltalPeople, Integer jiyangToltalPeople, Integer tianyaToltalPeople, Integer yazhouToltalPeople) {
		String sanyaSum;
		String haitangSum;
		String jiyangSum;
		String tianyaSum;
		String yazhouSum;
		for (ZoneSourcePlaceVO vo : list) {
			sanyaSum = vo.getSanyaSumPercent();
			haitangSum = vo.getHaitangSumPercent();
			jiyangSum = vo.getJiyangSumPercent();
			tianyaSum = vo.getTianyaSumPercent();
			yazhouSum = vo.getYazhouSumPercent();
			settingVo(sanyaToltalPeople, haitangToltalPeople, jiyangToltalPeople, tianyaToltalPeople, yazhouToltalPeople, sanyaSum, haitangSum, jiyangSum, tianyaSum, yazhouSum, vo);
		}
		return list;
	}

	/**
	 * 计算时间范围的数据，取百分比平均数
	 *
	 * @param list                集合对象
	 * @param sanyaToltalPeople   三亚市总人数
	 * @param haitangToltalPeople 海棠区总人数
	 * @param jiyangToltalPeople  吉阳区总人数
	 * @param tianyaToltalPeople  天涯区总人数
	 * @param yazhouToltalPeople  崖州区总人数
	 * @return 计算后的集合
	 */
	private List<ZoneSourcePlaceVO> getList(List<ZoneSourcePlaceVO> list, Integer sanyaToltalPeople, Integer haitangToltalPeople, Integer jiyangToltalPeople, Integer tianyaToltalPeople, Integer yazhouToltalPeople, Long day) {
		String sanyaSum;
		String haitangSum;
		String jiyangSum;
		String tianyaSum;
		String yazhouSum;
		for (ZoneSourcePlaceVO vo : list) {
			sanyaSum = vo.getSanyaSumPercent();
			haitangSum = vo.getHaitangSumPercent();
			jiyangSum = vo.getJiyangSumPercent();
			tianyaSum = vo.getTianyaSumPercent();
			yazhouSum = vo.getYazhouSumPercent();

			//总百分比除以天数 得到每天的平均百分比
			double sanyaPercent = Convert.convert(Double.class, sanyaSum) / Convert.convert(Double.class, day);
			double haitangPercent = Convert.convert(Double.class, haitangSum) / Convert.convert(Double.class, day);
			double jiyangPercent = Convert.convert(Double.class, jiyangSum) / Convert.convert(Double.class, day);
			double tianyaPercent = Convert.convert(Double.class, tianyaSum) / Convert.convert(Double.class, day);
			double yazhouPercent = Convert.convert(Double.class, yazhouSum) / Convert.convert(Double.class, day);

			//平均百分比 * 总人数 / 天数 得到平均人数
			double sanyaPeople = Convert.convert(Double.class, sanyaPercent) * Convert.convert(Double.class, sanyaToltalPeople) / day;
			double haitangPeople = Convert.convert(Double.class, haitangPercent) * Convert.convert(Double.class, haitangToltalPeople) / day;
			double jiyangPeople = Convert.convert(Double.class, jiyangPercent) * Convert.convert(Double.class, jiyangToltalPeople) / day;
			double tianyaPeople = Convert.convert(Double.class, tianyaPercent) * Convert.convert(Double.class, tianyaToltalPeople) / day;
			double yazhouPeople = Convert.convert(Double.class, yazhouPercent) * Convert.convert(Double.class, yazhouToltalPeople) / day;


			vo.setCreateTime(vo.getCreateTime());
			vo.setProvinceName(vo.getProvinceName());
			vo.setSanyaPercent(new DecimalFormat("#.##%").format(sanyaPercent));
			vo.setSanyaPeople(new DecimalFormat("#").format(sanyaPeople));

			vo.setHaitangPercent(new DecimalFormat("#.##%").format(haitangPercent));
			vo.setHaitangPeople(new DecimalFormat("#").format(haitangPeople));

			vo.setJiyangPercent(new DecimalFormat("#.##%").format(jiyangPercent));
			vo.setJiyangPeople(new DecimalFormat("#").format(jiyangPeople));

			vo.setTianyaPercent(new DecimalFormat("#.##%").format(tianyaPercent));
			vo.setTianyaPeople(new DecimalFormat("#").format(tianyaPeople));

			vo.setYazhouPercent(new DecimalFormat("#.##%").format(yazhouPercent));
			vo.setYazhouPeople(new DecimalFormat("#").format(yazhouPeople));
		}
		return list;
	}

	private void settingVo(Integer sanyaToltalPeople, Integer haitangToltalPeople, Integer jiyangToltalPeople, Integer tianyaToltalPeople, Integer yazhouToltalPeople, String sanyaSum, String haitangSum, String jiyangSum, String tianyaSum, String yazhouSum, ZoneSourcePlaceVO vo) {
		double sanyaPeople = Convert.convert(Double.class, sanyaSum) * Convert.convert(Double.class, sanyaToltalPeople);
		double haitangPeople = Convert.convert(Double.class, haitangSum) * Convert.convert(Double.class, haitangToltalPeople);
		double jiyangPeople = Convert.convert(Double.class, jiyangSum) * Convert.convert(Double.class, jiyangToltalPeople);
		double tianyaPeople = Convert.convert(Double.class, tianyaSum) * Convert.convert(Double.class, tianyaToltalPeople);
		double yazhouPeople = Convert.convert(Double.class, yazhouSum) * Convert.convert(Double.class, yazhouToltalPeople);

		double sanyaPercent = Convert.convert(Double.class, sanyaPeople) / Convert.convert(Double.class, sanyaToltalPeople);
		double haitangPercent = Convert.convert(Double.class, haitangPeople) / Convert.convert(Double.class, haitangToltalPeople);
		double jiyangPercent = Convert.convert(Double.class, jiyangPeople) / Convert.convert(Double.class, jiyangToltalPeople);
		double tianyaPercent = Convert.convert(Double.class, tianyaPeople) / Convert.convert(Double.class, tianyaToltalPeople);
		double yazhouPercent = Convert.convert(Double.class, yazhouPeople) / Convert.convert(Double.class, yazhouToltalPeople);


		vo.setCreateTime(vo.getCreateTime());
		vo.setProvinceName(vo.getProvinceName());
		vo.setSanyaPercent(new DecimalFormat("#.##%").format(sanyaPercent));
		vo.setSanyaPeople(new DecimalFormat("#").format(sanyaPeople));

		vo.setHaitangPercent(new DecimalFormat("#.##%").format(haitangPercent));
		vo.setHaitangPeople(new DecimalFormat("#").format(haitangPeople));

		vo.setJiyangPercent(new DecimalFormat("#.##%").format(jiyangPercent));
		vo.setJiyangPeople(new DecimalFormat("#").format(jiyangPeople));

		vo.setTianyaPercent(new DecimalFormat("#.##%").format(tianyaPercent));
		vo.setTianyaPeople(new DecimalFormat("#").format(tianyaPeople));

		vo.setYazhouPercent(new DecimalFormat("#.##%").format(yazhouPercent));
		vo.setYazhouPeople(new DecimalFormat("#").format(yazhouPeople));
	}


	@Override
	public Map<String, Object> sourcePlace(IPage<SourcePlaceVO> page, String typeCode, String startTime, String endTime) {
		Map<String, String> originMap = AreaCodeUtils.originMap();
		String tableName = originMap.get(typeCode);
		//根据区域id，查询该区域客源地数据
		IPage<SourcePlaceVO> pages = backstageTencentMapper.sourceFind(page, tableName, typeCode, startTime, endTime);

		//返回的集合
		List<SourcePlaceVO> reList = new ArrayList<>();

		List<SourcePlaceVO> recordList = pages.getRecords();
		for (SourcePlaceVO sourcePlaceVO : recordList) {
			//省份和百分比  天津市-0.0121
			String name = sourcePlaceVO.getName();
			String totalPeople = "";

			totalPeople = Integer.parseInt(sourcePlaceVO.getInPeople()) + Integer.parseInt(sourcePlaceVO.getOutPeople()) + "";

			if (StringUtils.isEmpty(totalPeople)) {
				continue;
			}
			//拿到去重后省份的最大百分比
			List<String> list = methodOne(name);
			SourcePlaceVO sourceVo = new SourcePlaceVO();
			for (int i = 0; i < list.size(); i++) {
				String[] split = list.get(i).split("-");
				BigDecimal bigDecimal1 = new BigDecimal(split[1]);
				BigDecimal bigDecimal2 = new BigDecimal(totalPeople);
				BigDecimal multiply = bigDecimal1.multiply(bigDecimal2);
				//省对应的人数 整数
				int inPeople = multiply.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();

				double doPeople = Double.parseDouble(totalPeople);
				double aDouble = inPeople;
				String percent = decimalFormat(aDouble / doPeople);//省对应的百分比

				sourceVo.setName("");
				sourceVo.setTypeCode("");
				sourceVo.setTime(sourcePlaceVO.getTime());
				if (i == 0) {
					sourceVo.setTop1(split[0]);
					sourceVo.setTop1Value(inPeople + "");
					sourceVo.setTop1ValuePerce(percent);
				} else if (i == 1) {
					sourceVo.setTop2(split[0]);
					sourceVo.setTop2Value(inPeople + "");
					sourceVo.setTop2ValuePerce(percent);
				} else if (i == 2) {
					sourceVo.setTop3(split[0]);
					sourceVo.setTop3Value(inPeople + "");
					sourceVo.setTop3ValuePerce(percent);
				} else if (i == 3) {
					sourceVo.setTop4(split[0]);
					sourceVo.setTop4Value(inPeople + "");
					sourceVo.setTop4ValuePerce(percent);
				} else if (i == 4) {
					sourceVo.setTop5(split[0]);
					sourceVo.setTop5Value(inPeople + "");
					sourceVo.setTop5ValuePerce(percent);
				} else if (i == 5) {
					sourceVo.setTop6(split[0]);
					sourceVo.setTop6Value(inPeople + "");
					sourceVo.setTop6ValuePerce(percent);
				} else if (i == 6) {
					sourceVo.setTop7(split[0]);
					sourceVo.setTop7Value(inPeople + "");
					sourceVo.setTop7ValuePerce(percent);
				} else if (i == 7) {
					sourceVo.setTop8(split[0]);
					sourceVo.setTop8Value(inPeople + "");
					sourceVo.setTop8ValuePerce(percent);
				} else if (i == 8) {
					sourceVo.setTop9(split[0]);
					sourceVo.setTop9Value(inPeople + "");
					sourceVo.setTop9ValuePerce(percent);
				} else if (i == 9) {
					sourceVo.setTop10(split[0]);
					sourceVo.setTop10Value(inPeople + "");
					sourceVo.setTop10ValuePerce(percent);
				}
			}
			reList.add(sourceVo);
		}
		pages.setRecords(reList);
		Map<String, Object> map = new HashMap<>(16);
		TimeVO time = backstageTencentMapper.getTime();
		map.put("maxTime", time.getMaxTime());
		map.put("minTime", time.getMinTime());
		map.put("option", pages);
		return map;
	}


	/**
	 * 对传递过来的字符串去重并取10个省份
	 *
	 * @param str
	 * @return
	 */
	public List<String> methodOne(String str) {
		//海南省-0.5545,海南省-0.1902,湖南省-0.0585,海南省-0.0228,河北省-0.0211,山西省-0.0195,河南省-0.0114,广东省-0.0098,海南省-0.0098,
		// 重庆市-0.0081,海南省-0.0081,江苏省-0.0081,湖南省-0.0081,海南省-0.0065,山东省-0.0049,广西壮族自治区-0.0049,海南省-0.0049,
		// 河北省-0.0049,浙江省-0.0049,福建省-0.0049,海南省-0.0033,河南省-0.0033,广东省-0.0033,海南省-0.0033,海南省-0.0016,北京市-0.0016,
		// 海南省-0.0016,山西省-0.0016,云南省-0.0016,广东省-0.0016,浙江省-0.0016,黑龙江省-0.0016,贵州省-0.0016,山东省-0.0016,海南省-0.0016,
		// 贵州省-0.0016,福建省-0.0016
		//福建省-0.0012,辽宁省-0.0012,海南省-0.0012,广东省-0.0012,河南省-0.0012,江西省-0.0012,湖南省--0.0005
		List<String> allList = new ArrayList<>();

		String[] sp = str.split(",");
		List<String> lit = Arrays.asList(sp);
		List<String> split = new ArrayList(lit);
		//排除值为负数的
		List<String> splitNew = new ArrayList();
		for (String spl : split) {
			if (!spl.contains("--")) {
				splitNew.add(spl);
			}
		}

		while (true) {//对每个省份进行依次去重获取最大值，直到最后一个
			Map<String, Object> map = methodSplit(splitNew);
			String value = (String) map.get("value");
			if (!StringUtils.isEmpty(value)) {
				allList.add(value);
				List<String> reList = (List<String>) map.get("split");
				if (reList.size() > 0) {
					splitNew = reList;
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
		//重新组装数据 只组装10条数据
		List<String> allListNew = new ArrayList<>();
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
			//校验数据的正确性
			if (split.get(i).contains("-") && split.get(i).substring(split.get(i).indexOf("-") + 1, split.get(i).length()).length() > 0) {
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
	 * 以百分比方式计数
	 *
	 * @param percent 百分比
	 */
	public static String decimalFormatWhole(double percent) {
		DecimalFormat df = new DecimalFormat("#");
		return df.format(percent);

	}

	//时间字符串（yyyy-MM-dd）转localDateTime
	public static LocalDateTime strLocaTime(String timeStr) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date data = sdf.parse(timeStr);
		Instant instant = data.toInstant();
		ZoneId zone = ZoneId.systemDefault();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
		//int monthValue = localDateTime.getMonthValue();
		return localDateTime;
	}

	@Override
	public void getImage(HttpServletResponse response, String username, Integer width, Integer height, String color, String alpha) {

		Font font = new Font("Arial", Font.PLAIN, 27);
		// 获取font的样式应用在输出内容上整个的宽高
		int[] arr = getWidthAndHeight(username, font);
		int width2 = arr[0];
		int height2 = arr[1];

		if (width2 < width) {
			width2 = width;
		}
		if (height2 < height) {
			height2 = height;
		}


		BufferedImage image = createImageWithText(username, width2, height2, font, Color.decode("#" + color), Float.parseFloat(alpha));

		try {
			// 输出png图片，formatName 对应图片的格式
			ImageIO.write(image, "png", response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建背景透明的文字图片
	 *
	 * @param str       文本字符串
	 * @param width     图片宽度
	 * @param height    图片高度
	 * @param font      设置字体
	 * @param fontColor 字体颜色
	 * @param alpha     文字透明度，值从0.0f-1.0f，依次变得不透明
	 */
	private BufferedImage createImageWithText(String str, int width, int height, Font font, Color fontColor, float alpha) {
		Integer degree = -45;

		BufferedImage textImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = textImage.createGraphics();

		//设置背景透明
		textImage = g2.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
		g2.dispose();
		g2 = textImage.createGraphics();

		//开启文字抗锯齿
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		//设置字体
		g2.setFont(font);


		g2.rotate(Math.toRadians(degree), (double) textImage.getWidth() / 2, (double) textImage.getHeight() / 2);

		//设置字体颜色
		g2.setColor(fontColor);
		//设置透明度:1.0f为透明度 ，值从0-1.0，依次变得不透明
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		//图片旋转
//		g2.rotate(Math.toRadians(45));
		//计算字体位置：上下左右居中
		FontRenderContext context = g2.getFontRenderContext();
		LineMetrics lineMetrics = font.getLineMetrics(str, context);
		FontMetrics fontMetrics = FontDesignMetrics.getMetrics(font);
		float offset = (width - fontMetrics.stringWidth(str)) / 2;
		float y = (height + lineMetrics.getAscent() - lineMetrics.getDescent() - lineMetrics.getLeading()) / 2;
		//绘图
		g2.drawString(str, (int) offset, (int) y);
		//释放资源
		g2.dispose();

		return textImage;
	}


	private static int[] getWidthAndHeight(String text, Font font) {
		Rectangle2D r = font.getStringBounds(text, new FontRenderContext(
			AffineTransform.getScaleInstance(1, 1), false, false));
		int unitHeight = (int) Math.floor(r.getHeight());//
		// 获取整个str用了font样式的宽度这里用四舍五入后+1保证宽度绝对能容纳这个字符串作为图片的宽度
		int width = (int) Math.round(r.getWidth()) + 1;
		// 把单个字符的高度+3保证高度绝对能容纳字符串作为图片的高度
		int height = unitHeight + 3;
		return new int[]{width, height};
	}
}
