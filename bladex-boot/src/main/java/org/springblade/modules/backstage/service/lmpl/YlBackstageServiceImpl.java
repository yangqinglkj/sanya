package org.springblade.modules.backstage.service.lmpl;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.math.Money;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springblade.common.entity.capitaConsumptionTen;
import org.springblade.common.entity.capitaConsumptionTopNew;
import org.springblade.common.entity.consumptionScale;
import org.springblade.common.entity.yl.TravelDayIndex5;
import org.springblade.common.mapper.yl.TravelDayIndex2Mapper;
import org.springblade.common.mapper.yl.TravelDayIndex4Mapper;
import org.springblade.modules.backstage.mapper.YlBackstageMapper;
import org.springblade.modules.backstage.service.YlBackstageService;
import org.springblade.modules.backstage.vo.ConsumerAresGraphVO;
import org.springblade.modules.backstage.vo.TimeVO;
import org.springblade.modules.backstage.vo.yl.CityConsumptionVO;
import org.springblade.modules.backstage.vo.yl.ConsumerAresVO;
import org.springblade.modules.backstage.vo.yl.MonthMoneyAvgVO;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @Author yq
 * @Date 2020/9/17 15:40
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class YlBackstageServiceImpl implements YlBackstageService {

	@Resource
	private YlBackstageMapper ylBackstageMapper;
	@Resource
	private TravelDayIndex4Mapper travelDayIndex4Mapper;
	@Resource
	private TravelDayIndex2Mapper travelDayIndex2Mapper;

	@Override
	public Map<String,Object> consumerAres(IPage<TravelDayIndex5> page, String startTime, String endTime, String type, String business) {
		IPage<TravelDayIndex5> travelDayIndex5IPage ;
		List<TravelDayIndex5> allCount = new ArrayList<>();
		if ("-1".equals(business)){//????????????
			travelDayIndex5IPage = ylBackstageMapper.allBusinessDistrict(page, startTime, endTime, type);
			allCount = ylBackstageMapper.allBusinessDistrictCount(startTime, endTime, type);
		}else {
			travelDayIndex5IPage = ylBackstageMapper.consumerAres(page, startTime, endTime, type, business);
			 allCount = ylBackstageMapper.findAllCount(startTime, endTime, type, business);
		}
		IPage<ConsumerAresVO> page1 = new Page<>();
		BeanUtils.copyProperties(travelDayIndex5IPage, page1);

		List<TravelDayIndex5> records = travelDayIndex5IPage.getRecords();
		//List<TravelDayIndex5> collect = records.stream().filter(x -> x.getTransNum() != 0 && x.getAcctNum() != 0).collect(Collectors.toList());
		List<ConsumerAresVO> list = new ArrayList<>();
		for (TravelDayIndex5 travelDayIndex5 : records) {
			ConsumerAresVO vo = new ConsumerAresVO();
			if ("-1".equals(business)){
				vo.setZone("????????????");
			}else {
				vo.setZone(travelDayIndex5.getZone());
			}
			vo.setMonths(travelDayIndex5.getDealDay());
			if ("-1".equals(business)){
				vo.setTravellerType(type);
			}else {
				vo.setTravellerType(travelDayIndex5.getTravellerType());
			}
			vo.setTransAt(travelDayIndex5.getTransAt());
			vo.setTransNum(travelDayIndex5.getTransNum());
			vo.setAcctNum(travelDayIndex5.getAcctNum());
			//????????????
			BigDecimal transAt = travelDayIndex5.getTransAt();
			//????????????
			Integer transNum = travelDayIndex5.getTransNum();
			//????????????
			BigDecimal divide = transAt.divide(Convert.toBigDecimal(transNum), 2, RoundingMode.HALF_UP);
			vo.setPerCapita(divide);
			list.add(vo);
		}
		BigDecimal countConsumption = new BigDecimal(0);//???????????????
		Integer countNumber = 0;//?????????
		BigDecimal money = new BigDecimal(0);//?????????

		for (TravelDayIndex5 entity : allCount) {
			//????????????
			BigDecimal transAt = entity.getTransAt();
			//????????????
			Integer transNum = entity.getTransNum();
			//????????????
			BigDecimal divide = transAt.divide(Convert.toBigDecimal(transNum), 2, RoundingMode.HALF_UP);

			countConsumption = countConsumption.add(divide);//????????????
			countNumber += entity.getTransNum();//?????????
			money = money.add(entity.getTransAt());//?????????
		}
		ConsumerAresVO consumerAresVO = new ConsumerAresVO();
		consumerAresVO.setAcctNum(0);
		consumerAresVO.setTransNum(countNumber);
		consumerAresVO.setTransAt(money);
		consumerAresVO.setPerCapita(countConsumption);
		list.add(consumerAresVO);
		TimeVO time = ylBackstageMapper.getConsumerAresTime(type,business);
		Map<String,Object> map = new HashMap<>(16);
		map.put("maxTime",time.getMaxTime());
		map.put("minTime",time.getMinTime());
		map.put("option",page1.setRecords(list));
		return map;
	}

	@Override
	public Map<String, Object> consumerAresGraph(String startTime, String endTime, String type, String business) {
		Map<String, Object> map = new HashMap<>(16);
		if (StringUtils.isEmpty(type)) {
			List<ConsumerAresGraphVO> vos = ylBackstageMapper.consumerAresGraph(startTime, endTime, type, business);
			List<ConsumerAresGraphVO> newList = vos.stream().filter(x -> x.getAcctNum() != 0 && x.getAcctNum() != 0).collect(Collectors.toList());
			Map<String, List<ConsumerAresGraphVO>> collect = newList.stream().collect(Collectors.groupingBy(ConsumerAresGraphVO::getDealDay));
			List<MonthMoneyAvgVO> avgVOList = new ArrayList<>();
			for (Map.Entry<String, List<ConsumerAresGraphVO>> stringListEntry : collect.entrySet()) {
				MonthMoneyAvgVO monthMoneyAvgVO = new MonthMoneyAvgVO();

				String key = stringListEntry.getKey();
				List<ConsumerAresGraphVO> value = stringListEntry.getValue();
				//????????????
				List<ConsumerAresGraphVO> inner = value.stream().filter(x -> "????????????".equals(x.getTravellerType())).collect(Collectors.toList());
				//????????????
				List<ConsumerAresGraphVO> outer = value.stream().filter(x -> "????????????".equals(x.getTravellerType())).collect(Collectors.toList());

				//???????????????
				double innerSum = inner.stream().mapToDouble(x -> Convert.convert(Double.class, x.getTransAt())).sum();
				//???????????????
				double outerSum = outer.stream().mapToDouble(x -> Convert.convert(Double.class, x.getTransAt())).sum();

				//???????????????
				int innerAcctNum = inner.stream().mapToInt(x -> x.getAcctNum()).sum();
				//???????????????
				int outerAcctNum = outer.stream().mapToInt(x -> x.getAcctNum()).sum();

				//??????????????????
				BigDecimal innerTotalMoney = new BigDecimal(innerSum);
				BigDecimal innerAccNum = new BigDecimal(innerAcctNum);
				BigDecimal innerAvgMoney = innerTotalMoney.divide(innerAccNum, 2, BigDecimal.ROUND_HALF_UP);

				//??????????????????
				BigDecimal outerTotalMoney = new BigDecimal(outerSum);
				BigDecimal outerAccNum = new BigDecimal(outerAcctNum);
				BigDecimal outerAvgMoney = outerTotalMoney.divide(outerAccNum, 2, BigDecimal.ROUND_HALF_UP);

				monthMoneyAvgVO.setMonths(key + "???");
				monthMoneyAvgVO.setInner(innerAvgMoney);
				monthMoneyAvgVO.setOuter(outerAvgMoney);
				avgVOList.add(monthMoneyAvgVO);

			}
			map.put("list", avgVOList);
			return map;
		} else if ("????????????".equals(type)) {
			List<ConsumerAresGraphVO> vos = ylBackstageMapper.consumerAresGraph(startTime, endTime, type, business);
			List<ConsumerAresGraphVO> newList = vos.stream().filter(x -> x.getAcctNum() != 0 && x.getAcctNum() != 0).collect(Collectors.toList());
			Map<String, List<ConsumerAresGraphVO>> collect = newList.stream().collect(Collectors.groupingBy(ConsumerAresGraphVO::getDealDay));
			List<MonthMoneyAvgVO> avgVOList = new ArrayList<>();
			for (Map.Entry<String, List<ConsumerAresGraphVO>> stringListEntry : collect.entrySet()) {
				MonthMoneyAvgVO monthMoneyAvgVO = new MonthMoneyAvgVO();

				String key = stringListEntry.getKey();
				List<ConsumerAresGraphVO> value = stringListEntry.getValue();
				//????????????
				List<ConsumerAresGraphVO> inner = value.stream().filter(x -> "????????????".equals(x.getTravellerType())).collect(Collectors.toList());
				//???????????????
				double innerSum = inner.stream().mapToDouble(x -> Convert.convert(Double.class, x.getTransAt())).sum();
				//???????????????
				int innerAcctNum = inner.stream().mapToInt(x -> x.getAcctNum()).sum();

				//??????????????????
				BigDecimal innerTotalMoney = new BigDecimal(innerSum);
				BigDecimal innerAccNum = new BigDecimal(innerAcctNum);
				BigDecimal innerAvgMoney = innerTotalMoney.divide(innerAccNum, 2, BigDecimal.ROUND_HALF_UP);

				monthMoneyAvgVO.setMonths(key + "???");
				monthMoneyAvgVO.setInner(innerAvgMoney);
				monthMoneyAvgVO.setOuter(new BigDecimal(0));
				avgVOList.add(monthMoneyAvgVO);
			}
			map.put("list", avgVOList);
			return map;
		} else if ("????????????".equals(type)) {
			List<ConsumerAresGraphVO> vos = ylBackstageMapper.consumerAresGraph(startTime, endTime, type, business);
			List<ConsumerAresGraphVO> newList = vos.stream().filter(x -> x.getAcctNum() != 0).collect(Collectors.toList());
			Map<String, List<ConsumerAresGraphVO>> collect = newList.stream().collect(Collectors.groupingBy(ConsumerAresGraphVO::getDealDay));
			List<MonthMoneyAvgVO> avgVOList = new ArrayList<>();
			for (Map.Entry<String, List<ConsumerAresGraphVO>> stringListEntry : collect.entrySet()) {
				MonthMoneyAvgVO monthMoneyAvgVO = new MonthMoneyAvgVO();
				String key = stringListEntry.getKey();
				List<ConsumerAresGraphVO> value = stringListEntry.getValue();
				//????????????
				List<ConsumerAresGraphVO> outer = value.stream().filter(x -> "????????????".equals(x.getTravellerType())).collect(Collectors.toList());
				//???????????????
				double outerSum = outer.stream().mapToDouble(x -> Convert.convert(Double.class, x.getTransAt())).sum();
				//???????????????
				int outerAcctNum = outer.stream().mapToInt(x -> x.getAcctNum()).sum();

				//??????????????????
				BigDecimal outerTotalMoney = new BigDecimal(outerSum);
				BigDecimal outerAccNum = new BigDecimal(outerAcctNum);
				BigDecimal outerAvgMoney = outerTotalMoney.divide(outerAccNum, 2, BigDecimal.ROUND_HALF_UP);
				monthMoneyAvgVO.setMonths(key + "???");
				monthMoneyAvgVO.setOuter(outerAvgMoney);
				monthMoneyAvgVO.setInner(new BigDecimal(0));
				avgVOList.add(monthMoneyAvgVO);

			}
			map.put("list", avgVOList);
			return map;
		}
		return null;
	}

	@Override
	public Map<String, Object> cityConsumption(IPage<CityConsumptionVO> page,String startTime, String endTime) {
		IPage<CityConsumptionVO> cityConsumption = ylBackstageMapper.cityConsumption(page, startTime, endTime);
		IPage<CityConsumptionVO> newPage = new Page<>();
		BeanUtils.copyProperties(cityConsumption,newPage);
		List<CityConsumptionVO> list = new ArrayList<>();
		for (CityConsumptionVO cityConsumptionVO : cityConsumption.getRecords()) {
			CityConsumptionVO vo = new CityConsumptionVO();
			vo.setDate(cityConsumptionVO.getYears() + "-" + cityConsumptionVO.getMonths() + "-" + cityConsumptionVO.getDays());
			vo.setTotalTransAt(cityConsumptionVO.getTotalTransAt());
			vo.setTotalTransNum(cityConsumptionVO.getTotalTransNum());
			vo.setTotalAcctNum(cityConsumptionVO.getTotalAcctNum());
			list.add(vo);
		}
		BigDecimal money = new BigDecimal(0);//?????????
		Integer countNumber = 0;//?????????
		Integer countNumberConsumers = 0;//???????????????
		List<CityConsumptionVO> cityConsumptionVOList = ylBackstageMapper.allCountA(startTime, endTime);
		for (CityConsumptionVO entity : cityConsumptionVOList) {
			money = money.add(new BigDecimal(entity.getTotalTransAt()));
			countNumber += Integer.parseInt(entity.getTotalTransNum());
			countNumberConsumers += Integer.parseInt(entity.getTotalAcctNum());
		}
		CityConsumptionVO cityConsumptionVO = new CityConsumptionVO();
		cityConsumptionVO.setTotalTransAt(money+"");
		cityConsumptionVO.setTotalTransNum(countNumber+"");
		cityConsumptionVO.setTotalAcctNum(countNumberConsumers+"");
		list.add(cityConsumptionVO);
		newPage.setRecords(list);
		TimeVO time = ylBackstageMapper.getCityConsumptionTime();
		Map<String, Object> map = new HashMap<>();
		map.put("option", newPage);
		map.put("maxTime",time.getMaxTime());
		map.put("minTime",time.getMinTime());
		return map;
	}


	@Override
	public Map<String,Object> capitaConsumption(String startTime, String endTime) {

		List<capitaConsumptionTopNew> listCap = travelDayIndex4Mapper.capitaConsumption(startTime, endTime);
		//???????????????
		for (capitaConsumptionTopNew cap : listCap) {
			BigDecimal bg = new BigDecimal(cap.getConsume());
			String consumeNew = bg.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
			cap.setConsume(consumeNew);
		}
		capitaConsumptionTopNew entity = new capitaConsumptionTopNew();
		//?????????
		double totalTransAt = listCap.stream().mapToDouble(x -> Double.parseDouble(x.getTransAt())).sum();
		//???????????????
		double totalTransNum = listCap.stream().mapToDouble(x -> Double.parseDouble(x.getTransNum())).sum();
		//???????????????
		double totalAcctNum = listCap.stream().mapToDouble(x -> Double.parseDouble(x.getAcctNum())).sum();
		entity.setTransAt(new DecimalFormat("#.##").format(totalTransAt));
		entity.setTransNum(new DecimalFormat("#.##").format(totalTransNum));
		entity.setAcctNum(new DecimalFormat("#.##").format(totalAcctNum));
		listCap.add(entity);

		Map<String,Object> map = new HashMap<>();
		map.put("list",listCap);
		if (StringUtils.isEmpty(startTime) && StringUtils.isEmpty(endTime)){
			String newTime = travelDayIndex4Mapper.getNewTime();
			map.put("time",newTime);
		}
		TimeVO time = travelDayIndex4Mapper.getCapitaConsumptionTime();
		map.put("maxTime",time.getMaxTime());
		map.put("minTime",time.getMinTime());
		return map;
	}

	/**
	 * ??????????????????????????????
	 *
	 * @param startTime
	 * @param endTime
	 * @param page
	 * @return
	 */
	@Override
	public Map<String, Object> consumptionScaleA(String startTime, String endTime, IPage page) {
		IPage<consumptionScale> consumptionScaleIPage = travelDayIndex2Mapper.consumptionScaleA(startTime, endTime, page);

		List<consumptionScale> ce = travelDayIndex2Mapper.finbByAllCount(startTime, endTime);
		//???
		StringBuilder sbLive = new StringBuilder();
		Integer liveCountNumber = 0; //???????????????
		BigDecimal liveMoney = new BigDecimal(0);//???????????????
		Integer liveCountNumberConsumers = 0;//?????????????????????
		BigDecimal liveEvery = new BigDecimal(0);//?????????????????????

		//???
		StringBuilder sbEat = new StringBuilder();
		Integer eatCountNumber = 0; //???????????????
		BigDecimal eatMoney = new BigDecimal(0);//???????????????
		Integer eatCountNumberConsumers = 0;//?????????????????????
		BigDecimal eatEvery = new BigDecimal(0);//?????????????????????

		//???
		StringBuilder sbAmu = new StringBuilder();
		Integer amuCountNumber = 0; //???????????????
		BigDecimal amuMoney = new BigDecimal(0);//???????????????
		Integer amuCountNumberConsumers = 0;//?????????????????????
		BigDecimal amuEvery = new BigDecimal(0);//?????????????????????

		//???
		StringBuilder sbShop = new StringBuilder();
		Integer shopCountNumber = 0; //???????????????
		BigDecimal shopMoney = new BigDecimal(0);//???????????????
		Integer shopCountNumberConsumers = 0;//?????????????????????
		BigDecimal shopEvery = new BigDecimal(0);//?????????????????????

		for (consumptionScale entity : ce) {
			String top1 = entity.getTop1();//??? ???:105421:310189561.77:22341  ?????????  ?????????  ????????????   ?????????/????????????
			String[] split1 = top1.split(":");
			liveCountNumber += Integer.parseInt(split1[1]);
			liveMoney = liveMoney.add(new BigDecimal(split1[2]));
			liveCountNumberConsumers += Integer.parseInt(split1[3]);
			BigDecimal liveDecimal2 = new BigDecimal(split1[2]);//?????????
			BigDecimal liveDecimal3 = new BigDecimal(split1[3]);//????????????
			liveEvery = liveEvery.add(liveDecimal2.divide(liveDecimal3,2, BigDecimal.ROUND_HALF_UP));

			String top2 = entity.getTop2();//???
			String[] split2 = top2.split(":");
			eatCountNumber += Integer.parseInt(split2[1]);
			eatMoney = eatMoney.add(new BigDecimal(split2[2]));
			eatCountNumberConsumers += Integer.parseInt(split2[3]);
			BigDecimal eatDecimal2 = new BigDecimal(split2[2]);//?????????
			BigDecimal eatDecimal3 = new BigDecimal(split2[3]);//????????????
			eatEvery = eatEvery.add(eatDecimal2.divide(eatDecimal3,2, BigDecimal.ROUND_HALF_UP));

			String top3 = entity.getTop3();//???
			String[] split3 = top3.split(":");
			amuCountNumber += Integer.parseInt(split3[1]);
			amuMoney = amuMoney.add(new BigDecimal(split3[2]));
			amuCountNumberConsumers += Integer.parseInt(split3[3]);
			BigDecimal amuDecimal2 = new BigDecimal(split3[2]);//?????????
			BigDecimal amuDecimal3 = new BigDecimal(split3[3]);//????????????
			amuEvery = amuEvery.add(amuDecimal2.divide(amuDecimal3,2, BigDecimal.ROUND_HALF_UP));

			String top4 = entity.getTop4();//???
			String[] split4 = top4.split(":");
			shopCountNumber += Integer.parseInt(split4[1]);
			shopMoney = shopMoney.add(new BigDecimal(split4[2]));
			shopCountNumberConsumers += Integer.parseInt(split4[3]);
			BigDecimal shopDecimal2 = new BigDecimal(split4[2]);//?????????
			BigDecimal shopDecimal3 = new BigDecimal(split4[3]);//????????????
			shopEvery = shopEvery.add(shopDecimal2.divide(shopDecimal3,2, BigDecimal.ROUND_HALF_UP));
		}

		sbLive.append("???:"+liveCountNumber+":").append(liveMoney+":").append(liveCountNumberConsumers+":").append(liveEvery);
		sbEat.append("???:"+eatCountNumber+":").append(eatMoney+":").append(eatCountNumberConsumers+":").append(eatEvery);
		sbAmu.append("???:"+amuCountNumber+":").append(amuMoney+":").append(amuCountNumberConsumers+":").append(amuEvery);
		sbShop.append("???:"+shopCountNumber+":").append(shopMoney+":").append(shopCountNumberConsumers+":").append(shopEvery);
		List<consumptionScale> recordList = consumptionScaleIPage.getRecords();
		consumptionScale co = new consumptionScale();
		co.setTop1(sbLive.toString());
		co.setTop2(sbEat.toString());
		co.setTop3(sbAmu.toString());
		co.setTop4(sbShop.toString());

		/*co.setTop1LiveCountNumber(liveCountNumber+"");
		co.setTop1LiveMoney(liveMoney+"");
		co.setTop1LiveCountNumberConsumers(liveCountNumberConsumers+"");
		co.setTop1LiveEvery(liveEvery+"");

		co.setTop2EatCountNumber(eatCountNumber+"");
		co.setTop2EatMoney(eatMoney+"");
		co.setTop2EatCountNumberConsumers(eatCountNumberConsumers+"");
		co.setTop2EatEvery(eatEvery+"");

		co.setTop3AmuCountNumber(amuCountNumber+"");
		co.setTop3AmuMoney(amuMoney+"");
		co.setTop3AmuCountNumberConsumers(amuCountNumberConsumers+"");
		co.setTop3AmuEvery(amuEvery+"");

		co.setTop4ShopCountNumber(shopCountNumber+"");
		co.setTop4ShopMoney(shopMoney+"");
		co.setTop4ShopCountNumberConsumers(shopCountNumberConsumers+"");
		co.setTop4ShopEvery(shopEvery+"");*/
		if (recordList.size()>0){
			recordList.add(co);
		}
		TimeVO time = travelDayIndex2Mapper.getConsumptionScaleATime();
		Map<String, Object> map = new HashMap<>();
		map.put("option", consumptionScaleIPage);
		map.put("maxTime",time.getMaxTime());
		map.put("minTime",time.getMinTime());
		return map;
	}
}
