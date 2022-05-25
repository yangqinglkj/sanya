package org.springblade.modules.backstage.service.lmpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springblade.common.entity.FollowAndFree;
import org.springblade.common.entity.TravelCount;
import org.springblade.common.entity.xc.*;
import org.springblade.common.vo.xc.TransportationVO;
import org.springblade.modules.backstage.mapper.XcBackstageMapper;
import org.springblade.modules.backstage.service.XcBackstageService;
import org.springblade.modules.backstage.vo.TimeVO;
import org.springblade.modules.backstage.vo.xc.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
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
public class XcBackstageServiceImpl implements XcBackstageService {

	@Resource
	private XcBackstageMapper xcBackstageMapper;


	@Override
	public Map<String, Object> reserveChannelList(String day) {
		List<ReserveChannelEntity> list = xcBackstageMapper.reserveChannelList(day);
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
		List<Map<String, Object>> listReturn = new ArrayList<>();
		for (ReserveChannelEntity entity : H5) {
			Map<String, Object> map = new HashMap<>(16);
			String data = entity.getData();
			String value = entity.getValue();
			String newValue = value.substring(0, value.length() - 1);
			map.put("name", data);
			map.put("value", newValue);
			listReturn.add(map);
		}
		//APP 百分比
		for (ReserveChannelEntity entity : app) {
			Map<String, Object> map = new HashMap<>(16);
			String data = entity.getData();
			String value = entity.getValue();
			String newValue = value.substring(0, value.length() - 1);
			map.put("name", data);
			map.put("value", newValue);
			listReturn.add(map);
		}
		//小程序 百分比
		for (ReserveChannelEntity entity : mini) {
			Map<String, Object> map = new HashMap<>(16);
			String value = entity.getValue();
			String newValue = value.substring(0, value.length() - 1);
			map.put("name", "小程序");
			map.put("value", newValue);
			listReturn.add(map);
		}
		//分销 百分比
		for (ReserveChannelEntity entity : allianceapi) {
			Map<String, Object> map = new HashMap<>(16);
			String value = entity.getValue();
			String newValue = value.substring(0, value.length() - 1);
			map.put("name", "分销");
			map.put("value", newValue);
			listReturn.add(map);
		}
		//PC 百分比
		for (ReserveChannelEntity entity : pc) {
			Map<String, Object> map = new HashMap<>(16);
			String value = entity.getValue();
			String newValue = value.substring(0, value.length() - 1);
			map.put("name", "PC");
			map.put("value", newValue);
			listReturn.add(map);
		}

		//其他类型百分比总和
		List<Double> doubleList = new ArrayList<>();
		for (ReserveChannelEntity entity : list) {
			doubleList.add(Double.parseDouble(entity.getValue().substring(0, entity.getValue().length() - 1)));
		}
		double sum = doubleList.stream().mapToDouble(x -> x).sum();
		Map<String, Object> map = new HashMap<>();
		map.put("name", "其他");
		map.put("value", String.valueOf(sum));
		listReturn.add(map);
		TimeVO time = xcBackstageMapper.getReserveChannelTime();
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("maxTime",time.getMaxTime());
		returnMap.put("minTime",time.getMinTime());
		returnMap.put("option",listReturn);
		return returnMap;
	}

	@Override
	public Map<String, Object> reserveChannelTable(IPage<ReserveChannelTableVO> page, String startMonth, String endMonth) {
		List<String> allColumn = xcBackstageMapper.getAllColumn();
		IPage<Map<String, Object>> mapIPage = xcBackstageMapper.reserveChannelTable(page, startMonth, endMonth, allColumn);
		List<Map<String, Object>> records = mapIPage.getRecords();
		IPage<ReserveChannelTableCountVO> resultPage = new Page<>();
		BeanUtils.copyProperties(mapIPage, resultPage);
		List<ReserveChannelTableCountVO> list = new ArrayList<>();
		for (Map<String, Object> record : records) {
			double num = 0.00;
			ReserveChannelTableCountVO vo = new ReserveChannelTableCountVO();
			for (Map.Entry<String, Object> map : record.entrySet()) {
				if (StringUtils.isNotEmpty(map.getKey())) {
					if ("TOUTIAO".equals(map.getKey())) {
						num += (Double) map.getValue();
					} else if ("ALIPAY".equals(map.getKey())) {
						num += (Double) map.getValue();
					} else if ("BAIDU".equals(map.getKey())) {
						num += (Double) map.getValue();
					} else if ("其他".equals(map.getKey())) {
						num += (Double) map.getValue();
					} else if ("APP".equals(map.getKey())) {
						vo.setApp(map.getValue() + "%");
					} else if ("ALLIANCEAPI".equals(map.getKey())) {
						vo.setAllianceApi(map.getValue() + "%");
					} else if ("MINI_PROGRAM".equals(map.getKey())) {
						vo.setMiniProgram(map.getValue() + "%");
					} else if ("H5".equals(map.getKey())) {
						vo.setH5(map.getValue() + "%");
					} else if ("ONLINE".equals(map.getKey())) {
						vo.setOnline(map.getValue() + "%");
					} else if ("create_time".equals(map.getKey())) {
						vo.setCreateTime(map.getValue() + "");
					}
				}

			}
			vo.setOther(new DecimalFormat("#.##").format(num) + "%");
			list.add(vo);
		}
		resultPage.setRecords(list);
		TimeVO time = xcBackstageMapper.getReserveChannelTime();
		Map<String, Object> map = new HashMap<>();
		map.put("maxTime",time.getMaxTime());
		map.put("minTime",time.getMinTime());
		map.put("option",resultPage);
		return map;
	}

	@Override
	public Map<String,Object> reserveDayList(String day, String type) {
		if (StringUtils.isEmpty(type)) {
			type = "1";
		}
		List<ReserveDayEntity> list = xcBackstageMapper.reserveDayList(day, type);
		List<ReserveDayVO> voList = new ArrayList<>();

		List<ReserveDayEntity> zero = list.stream().filter(x -> "0".equals(x.getData())).collect(Collectors.toList());
		List<ReserveDayEntity> one = list.stream().filter(x -> "1天".equals(x.getData())).collect(Collectors.toList());
		List<ReserveDayEntity> two = list.stream().filter(x -> "2天".equals(x.getData())).collect(Collectors.toList());
		List<ReserveDayEntity> three = list.stream().filter(x -> "3天".equals(x.getData())).collect(Collectors.toList());
		List<ReserveDayEntity> four = list.stream().filter(x -> "4天".equals(x.getData())).collect(Collectors.toList());
		List<ReserveDayEntity> five = list.stream().filter(x -> "5天".equals(x.getData())).collect(Collectors.toList());
		List<ReserveDayEntity> six = list.stream().filter(x -> "6天".equals(x.getData())).collect(Collectors.toList());
		List<ReserveDayEntity> seven = list.stream().filter(x -> "7天".equals(x.getData())).collect(Collectors.toList());
		List<ReserveDayEntity> sevenOver = list.stream().filter(x -> "7天以上".equals(x.getData())).collect(Collectors.toList());
		for (ReserveDayEntity entity : zero) {
			ReserveDayVO reserveDayVO = new ReserveDayVO();
			reserveDayVO.setName("0天");
			reserveDayVO.setValue(entity.getValue().substring(0, entity.getValue().length() - 1));
			voList.add(reserveDayVO);

		}
		List<Double> oneToThreeList = new ArrayList<>();
		List<Double> fourToSevenList = new ArrayList<>();

		for (ReserveDayEntity entity : one) {
			oneToThreeList.add(Double.parseDouble(entity.getValue().substring(0, entity.getValue().length() - 1)));
		}
		for (ReserveDayEntity entity : two) {
			oneToThreeList.add(Double.parseDouble(entity.getValue().substring(0, entity.getValue().length() - 1)));
		}
		for (ReserveDayEntity entity : three) {
			oneToThreeList.add(Double.parseDouble(entity.getValue().substring(0, entity.getValue().length() - 1)));
		}
		for (ReserveDayEntity entity : four) {
			fourToSevenList.add(Double.parseDouble(entity.getValue().substring(0, entity.getValue().length() - 1)));
		}
		for (ReserveDayEntity entity : five) {
			fourToSevenList.add(Double.parseDouble(entity.getValue().substring(0, entity.getValue().length() - 1)));
		}
		for (ReserveDayEntity entity : six) {
			fourToSevenList.add(Double.parseDouble(entity.getValue().substring(0, entity.getValue().length() - 1)));
		}
		for (ReserveDayEntity entity : seven) {
			fourToSevenList.add(Double.parseDouble(entity.getValue().substring(0, entity.getValue().length() - 1)));
		}

		double oneToThreeSum = oneToThreeList.stream().mapToDouble(x -> x).sum();
		double fourToSevenSum = fourToSevenList.stream().mapToDouble(x -> x).sum();
		ReserveDayVO oneToThreeVO = new ReserveDayVO();
		oneToThreeVO.setName("1-3天");
		oneToThreeVO.setValue(new DecimalFormat("#.##").format(oneToThreeSum));

		ReserveDayVO fourToSevenVO = new ReserveDayVO();
		fourToSevenVO.setName("4-7天");
		fourToSevenVO.setValue(new DecimalFormat("#.##").format(fourToSevenSum));
		voList.add(oneToThreeVO);
		voList.add(fourToSevenVO);

		for (ReserveDayEntity entity : sevenOver) {
			ReserveDayVO reserveDayVO = new ReserveDayVO();
			reserveDayVO.setName(entity.getData());
			reserveDayVO.setValue(entity.getValue().substring(0, entity.getValue().length() - 1));
			voList.add(reserveDayVO);
		}
		Map<String,Object> map = new HashMap<>(16);
		TimeVO time = xcBackstageMapper.getReserveDayTime(type);
		map.put("maxTime",time.getMaxTime());
		map.put("minTime",time.getMinTime());
		map.put("option",voList);
		return map;
	}

	@Override
	public Map<String,Object> reserveDayTable(IPage<ReserveDayTableVO> page, String startMonth, String endMonth, String type) {
		//获取所有列名
		List<String> allDayColumn = xcBackstageMapper.getAllDayColumn();
		IPage<Map<String, Object>> mapIPage = xcBackstageMapper.reserveDayTable(page, startMonth, endMonth, type, allDayColumn);
		List<Map<String, Object>> records = mapIPage.getRecords();
		IPage<ReserveDayTableVO> resultPage = new Page<>();
		BeanUtils.copyProperties(mapIPage, resultPage);
		List<ReserveDayTableVO> list = new ArrayList<>();
		for (Map<String, Object> record : records) {
			double zone = 0.00;
			double oneToThree = 0.00;
			double fourToSeven = 0.00;
			double sevenOver = 0.00;
			ReserveDayTableVO vo = new ReserveDayTableVO();
			for (Map.Entry<String, Object> map : record.entrySet()) {
				if (StringUtils.isNotEmpty(map.getKey())) {
					if ("0".equals(map.getKey())) {
						zone += (Double) map.getValue();
					} else if ("1天".equals(map.getKey())) {
						oneToThree += (Double) map.getValue();
					} else if ("2天".equals(map.getKey())) {
						oneToThree += (Double) map.getValue();
					} else if ("3天".equals(map.getKey())) {
						oneToThree += (Double) map.getValue();
					} else if ("4天".equals(map.getKey())) {
						fourToSeven += (Double) map.getValue();
					} else if ("5天".equals(map.getKey())) {
						fourToSeven += (Double) map.getValue();
					} else if ("6天".equals(map.getKey())) {
						fourToSeven += (Double) map.getValue();
					} else if ("7天".equals(map.getKey())) {
						fourToSeven += (Double) map.getValue();
					} else if ("7天以上".equals(map.getKey())) {
						sevenOver += (Double) map.getValue();
					} else if ("createTime".equals(map.getKey())) {
						vo.setCreateTime(map.getValue() + "");
					} else if ("type".equals(map.getKey())) {
						vo.setType(map.getValue() + "");
					}
				}

			}
			vo.setZero(new DecimalFormat("#.##").format(zone) + "%");
			vo.setOneToThree(new DecimalFormat("#.##").format(oneToThree) + "%");
			vo.setFourToSeven(new DecimalFormat("#.##").format(fourToSeven) + "%");
			vo.setSevenOver(new DecimalFormat("#.##").format(sevenOver) + "%");
			list.add(vo);
		}
		resultPage.setRecords(list);
		Map<String,Object> map = new HashMap<>(16);
		TimeVO time = xcBackstageMapper.getReserveDayTime(type);
		map.put("maxTime",time.getMaxTime());
		map.put("minTime",time.getMinTime());
		map.put("option",resultPage);
		return map;
	}


	/**
	 * 亲子出行（休闲-带小孩）
	 * 商务(商务)
	 * 陪老人（休闲-带老人）
	 * 度假(休闲-全家    休闲-其他  回家)
	 */

	@Override
	public Object hotelTourist(String date) {
		Map<String, Object> map = new HashMap<>(16);
		List<OutingPurposeEntity> entityList = xcBackstageMapper.hotelTourist(date);
		if (CollectionUtils.isNotEmpty(entityList)) {
			List<OutingPurposeEntity> child = entityList.stream().filter(x -> "休闲-带小孩".equals(x.getData())).collect(Collectors.toList());
			List<OutingPurposeEntity> business = entityList.stream().filter(x -> "商务".equals(x.getData())).collect(Collectors.toList());
			List<OutingPurposeEntity> oldPeople = entityList.stream().filter(x -> "休闲-带老人".equals(x.getData())).collect(Collectors.toList());
			List<PurposeVO> list = new ArrayList<>();
			//亲子出行（休闲-带小孩）
			PurposeVO childVO = new PurposeVO();
			childVO.setName("亲子出行");
			String format = new DecimalFormat("#.##").format(Double.parseDouble(child.get(0).getValue().substring(0, child.get(0).getValue().length() - 1)));
			childVO.setValue(format);
			list.add(childVO);
			//商务
			PurposeVO businessVO = new PurposeVO();
			businessVO.setName("商务");
			String format2 = new DecimalFormat("#.##").format(Double.parseDouble(business.get(0).getValue().substring(0, business.get(0).getValue().length() - 1)));
			businessVO.setValue(format2);
			list.add(businessVO);
			//陪老人（休闲-带老人）
			PurposeVO oldPeopleVO = new PurposeVO();
			oldPeopleVO.setName("陪老人");
			String format3 = new DecimalFormat("#.##").format(Double.parseDouble(oldPeople.get(0).getValue().substring(0, oldPeople.get(0).getValue().length() - 1)));
			oldPeopleVO.setValue(format3);
			list.add(oldPeopleVO);
			//删除不需要分组的
			entityList.removeIf(next -> "休闲-带小孩".equals(next.getData()) || "商务".equals(next.getData()) || "休闲-带老人".equals(next.getData()));
			//度假(休闲-全家    休闲-其他  回家)
			List<String> collect = entityList.stream().map(OutingPurposeEntity::getValue).collect(Collectors.toList());
			List<Double> doubleList = new ArrayList<>();
			for (String s : collect) {
				doubleList.add(Double.parseDouble(s.substring(0, s.length() - 1)));
			}
			double sum = doubleList.stream().mapToDouble(x -> x).sum();
			PurposeVO vacationVO = new PurposeVO();
			vacationVO.setName("度假");
			String format1 = new DecimalFormat("#.##").format(sum);
			vacationVO.setValue(format1);
			list.add(vacationVO);
			TimeVO time = xcBackstageMapper.getHotelTouristTime();
			map.put("maxTime",time.getMaxTime());
			map.put("minTime",time.getMinTime());
			map.put("list", list);
			return map;
		}
		return "该日期无数据";

	}


	@Override
	public Map<String, Object> hotelTouristTable(IPage<OutingPurposeVO> page, String startTime, String endTime) {
		IPage<OutingPurposeVO> vos = xcBackstageMapper.hotelTouristTable(page, startTime, endTime);
		List<OutingPurposeVO> records = vos.getRecords();
		List<OutingPurposeVO> voList = new ArrayList<>();
		for (OutingPurposeVO vo : records) {
			OutingPurposeVO outingPurposeVO = new OutingPurposeVO();
			BeanUtils.copyProperties(vo, outingPurposeVO);
			outingPurposeVO.setHoliday(vo.getHoliday() + "%");
			voList.add(outingPurposeVO);
		}
		TimeVO time = xcBackstageMapper.getHotelTouristTime();
		Map<String, Object> map = new HashMap<>(16);
		map.put("maxTime",time.getMaxTime());
		map.put("minTime",time.getMinTime());
		map.put("option",vos.setRecords(voList));
		return map;
	}


	@Override
	public Map<String, Object> topHotelList(IPage<HotelVO> page, String startTime, String endTime) {
		IPage<HotelVO> hotelVOIPage = xcBackstageMapper.topHotelList(page, startTime, endTime);
		List<HotelVO> recordList = hotelVOIPage.getRecords();
		for (HotelVO hotelVO : recordList) {
			//数据格式：三亚天域度假酒店-14.26%
			String[] HotelName1 = hotelVO.getHotelName1().split("-");
			hotelVO.setHotelName1(HotelName1[0]);
			hotelVO.setHotelName1Value(HotelName1[1]);

			String[] HotelName2 = hotelVO.getHotelName2().split("-");
			hotelVO.setHotelName2(HotelName2[0]);
			hotelVO.setHotelName2Value(HotelName2[1]);

			String[] HotelName3 = hotelVO.getHotelName3().split("-");
			hotelVO.setHotelName3(HotelName3[0]);
			hotelVO.setHotelName3Value(HotelName3[1]);

			String[] HotelName4 = hotelVO.getHotelName4().split("-");
			hotelVO.setHotelName4(HotelName4[0]);
			hotelVO.setHotelName4Value(HotelName4[1]);

			String[] HotelName5 = hotelVO.getHotelName5().split("-");
			hotelVO.setHotelName5(HotelName5[0]);
			hotelVO.setHotelName5Value(HotelName5[1]);

			String[] HotelName6 = hotelVO.getHotelName6().split("-");
			hotelVO.setHotelName6(HotelName6[0]);
			hotelVO.setHotelName6Value(HotelName6[1]);

			String[] HotelName7 = hotelVO.getHotelName7().split("-");
			hotelVO.setHotelName7(HotelName7[0]);
			hotelVO.setHotelName7Value(HotelName7[1]);

			String[] HotelName8 = hotelVO.getHotelName8().split("-");
			hotelVO.setHotelName8(HotelName8[0]);
			hotelVO.setHotelName8Value(HotelName8[1]);

			String[] HotelName9 = hotelVO.getHotelName9().split("-");
			hotelVO.setHotelName9(HotelName9[0]);
			hotelVO.setHotelName9Value(HotelName9[1]);

			String[] HotelName10 = hotelVO.getHotelName10().split("-");
			hotelVO.setHotelName10(HotelName10[0]);
			hotelVO.setHotelName10Value(HotelName10[1]);
		}
		TimeVO time = xcBackstageMapper.getTopHotelTime();
		Map<String, Object> map = new HashMap<>(16);
		map.put("maxTime",time.getMaxTime());
		map.put("minTime",time.getMinTime());
		map.put("option",hotelVOIPage);
		return map;
	}

	@Override
	public Map<String, Object> wayList(String startTime, String endTime, IPage page) {
		int num = 0;
		if (StringUtils.isEmpty(startTime) && StringUtils.isEmpty(endTime)) {//开始时间  结束时间都为空 查询当前时间往前推30天的数据
			endTime = LocalDate.now().toString();
			startTime = LocalDate.now().minusDays(15).toString();
			num = 1;
		}
		//折线图数据
		List<String> listFollow = new ArrayList<>();//跟团游
		List<String> listFree = new ArrayList<>();//自由行
		List<String> listMonth = new ArrayList<>();//月份时间
		//开始时间以及结束时间  都只查60条数据 一天是2条数据 就是30条数据  折线图只展示30天的数据
		List<OutingWayEntity> outingWay = xcBackstageMapper.wayList(startTime, endTime);
		for (OutingWayEntity entity : outingWay) {
			String data = entity.getData();//跟团游
			String sv = entity.getValue();//48.07%
			String value = sv.substring(0, sv.length() - 1);//去掉百分号
//			String months = entity.getCreateTime()+"";//2020-10-01
			LocalDate createTime = entity.getCreateTime();
			int dayOfMonth = createTime.getDayOfMonth();
			int monthValue = createTime.getMonthValue();
			if ("跟团游".equals(data)) {
				listFollow.add(value);
				listMonth.add(monthValue + "/" + dayOfMonth);
			}
			if ("自由行".equals(data)) {
				listFree.add(value);
			}
		}
		//重新给时间赋值
		if (num == 1) {
			startTime = null;
			endTime = null;
		}
		//列表数据
		IPage<FollowAndFree> followAndFreeIPage = xcBackstageMapper.wayListPage(startTime, endTime, page);
		List<FollowAndFree> recordList = followAndFreeIPage.getRecords();
		List<FollowAndFree> turnList = new ArrayList<>();
		for (FollowAndFree followAndFree : recordList) {
			FollowAndFree fAndf = new FollowAndFree();
			fAndf.setMonths(followAndFree.getMonths());
			String top1 = followAndFree.getTop1();
			String top2 = followAndFree.getTop2();
			String[] split1 = top1.split(":");
			String[] split2 = top2.split(":");
			//跟团游:44.73%   自由行:55.76%
			if ("跟团游".equals(split1[0])) {
				fAndf.setFollow(split1[1]);
			} else {
				fAndf.setFree(split1[1]);
			}
			if ("跟团游".equals(split2[0])) {
				fAndf.setFollow(split2[1]);
			} else {
				fAndf.setFree(split2[1]);
			}
			turnList.add(fAndf);
		}
		followAndFreeIPage.setRecords(turnList);
		TimeVO time = xcBackstageMapper.getWayListTime();

		Map<String, Object> map = new HashMap<>();
		map.put("listMonth", listMonth);
		map.put("listFollow", listFollow);
		map.put("listFree", listFree);
		map.put("page", followAndFreeIPage);
		map.put("maxTime",time.getMaxTime());
		map.put("minTime",time.getMinTime());
		return map;
	}

	@Override
	public Map<String, Object> travelCountTable(IPage<HotelVO> page, String months) {
		Map<String, Object> map = new HashMap<>(16);
		IPage<TravelCount> pages = xcBackstageMapper.travelCountTable(page, months);
		List<Integer> domesticList = new ArrayList<>();
		List<Integer> overseaList = new ArrayList<>();
		List<String> monthsList = new ArrayList<>();
		List<TravelCount> recordList = pages.getRecords();
		List<TravelCount> sortList = recordList.stream().sorted(Comparator.comparing(TravelCount::getMonths).reversed()).limit(12).sorted(Comparator.comparing(TravelCount::getMonths)).collect(Collectors.toList());
//		for (int i = recordList.size() - 1; i >= 0; i--) {
//			domesticList.add(Integer.valueOf(recordList.get(i).getDomestic()));
//			overseaList.add(Integer.valueOf(recordList.get(i).getOversea()));
//			String str = recordList.get(i).getMonths();
//			String substring = str.substring(str.indexOf("-") + 1, str.lastIndexOf("-"));
//			monthsList.add(substring + "月");
//			String months1 = recordList.get(i).getMonths();//2020-01-01
//			String newMouths = months1.substring(0, months1.lastIndexOf("-"));//2020-01
//			recordList.get(i).setMonths(newMouths);
//		}
		sortList.forEach(item -> {
			domesticList.add(Integer.parseInt(item.getDomestic()));
			overseaList.add(Integer.parseInt(item.getOversea()));
			LocalDate localDate = LocalDate.parse(item.getMonths(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			monthsList.add(localDate.getMonthValue()+"月");
		});
		TravelCount travelCount = xcBackstageMapper.allMouthCount(months);
		TravelCount travelCount1 = new TravelCount();
		if (!org.springframework.util.StringUtils.isEmpty(travelCount)) {
			DecimalFormat de = new DecimalFormat("00");
			//对科学计数法的值进行转换
			travelCount1.setDomestic(de.format(Double.parseDouble(travelCount.getDomestic())));
			travelCount1.setOversea(de.format(Double.parseDouble(travelCount.getOversea())));
		}
		if (recordList.size() > 0) {
			recordList.add(travelCount1);
		}
		TimeVO time = xcBackstageMapper.getTime();
		map.put("maxTime",time.getMaxTime());
		map.put("minTime",time.getMinTime());
		map.put("data", pages);
		map.put("domestic", domesticList);
		map.put("oversea", overseaList);
		map.put("months", monthsList);
		return map;
	}


	@Override
	public Map<String,Object> transportationList(String date) {
		List<TransportationEntity> list = xcBackstageMapper.transportationList(date);
		List<Map<String, String>> mapList = new ArrayList<>();
		for (TransportationEntity entity : list) {
			Map<String, String> map = new HashMap<>();
			map.put("name", entity.getData());
			map.put("value", entity.getValue().substring(0, entity.getValue().length() - 1));
			mapList.add(map);
		}
		TimeVO time = xcBackstageMapper.getTransportationTime();
		Map<String,Object> map = new HashMap<>(16);
		map.put("maxTime",time.getMaxTime());
		map.put("minTime",time.getMinTime());
		map.put("option",mapList);
		return map;
	}

	@Override
	public Map<String,Object> transportationTable(IPage<HotelVO> page, String startTime, String endTime) {
		IPage<TransportationVO> pages = xcBackstageMapper.transportationTable(page, startTime, endTime);
		List<TransportationVO> list = new ArrayList<>();
		for (TransportationVO record : pages.getRecords()) {
			record.setCar(record.getCar() + "%");
			record.setPlane(record.getPlane() + "%");
			record.setOther(record.getOther() + "%");
			list.add(record);
		}
		pages.setRecords(list);
		TimeVO time = xcBackstageMapper.getTransportationTime();
		Map<String,Object> map = new HashMap<>(16);
		map.put("maxTime",time.getMaxTime());
		map.put("minTime",time.getMinTime());
		map.put("option",pages);
		return map;
	}


}
