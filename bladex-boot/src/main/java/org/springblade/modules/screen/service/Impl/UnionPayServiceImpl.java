package org.springblade.modules.screen.service.Impl;

import org.springblade.common.entity.yl.TravelDayIndex2;
import org.springblade.common.entity.yl.TravelDayIndex4;
import org.springblade.common.vo.Index5VO;
import org.springblade.common.vo.TravelDayIndex2VO;
import org.springblade.modules.screen.mapper.UnionPayMapper;
import org.springblade.modules.screen.service.UnionPayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
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
public class UnionPayServiceImpl implements UnionPayService {


	@Resource
	private UnionPayMapper unionPayMapper;


	@Override
	public List<Index5VO> findAll(Integer type) {
		String typeStr = null;
		if (type == 1) {
			typeStr = "跨市游客";
		}
		if (type == 2) {
			typeStr = "跨省游客";
		}
		List<Index5VO> index5VOList = unionPayMapper.consumerAres(typeStr);
		List<Index5VO> listVo = new ArrayList<>();
		for (Index5VO entity : index5VOList) {
			if ("解放路步行街".equals(entity.getZone())) {
				Index5VO t5VO = new Index5VO();
				t5VO.setZone("解放路");
				String transAt = entity.getTransAt();
				String format = new DecimalFormat("#").format(Double.parseDouble(transAt) / 10000);
				t5VO.setTransAt(format + "万");
				t5VO.setTransNum(entity.getTransNum());
				t5VO.setAcctNum(entity.getAcctNum());
				listVo.add(t5VO);
			}
			if ("免税城".equals(entity.getZone())) {
				Index5VO t5VO = new Index5VO();
				t5VO.setZone("三亚免税城");
				String transAt = entity.getTransAt();
				String format = new DecimalFormat("#").format(Double.parseDouble(transAt) / 10000);
				t5VO.setTransAt(format + "万");
				t5VO.setTransNum(entity.getTransNum());
				t5VO.setAcctNum(entity.getAcctNum());
				listVo.add(t5VO);
			}
			Index5VO t5VO = new Index5VO();
			t5VO.setZone(entity.getZone());
			t5VO.setTransNum(entity.getTransNum());
			t5VO.setAcctNum(entity.getAcctNum());
			String transAt = entity.getTransAt();
			String format = new DecimalFormat("#").format(Double.parseDouble(transAt) / 10000);
			t5VO.setTransAt(format + "万");
			listVo.add(t5VO);
		}
		List<Index5VO> newList = listVo.stream().filter(x -> !"解放路步行街".equals(x.getZone()) && !"免税城".equals(x.getZone())).collect(Collectors.toList());
		return newList;
	}

	@Override
	public List<Map<String, Object>> getPerCapitalConsumptionList() {
		List<Map<String, Object>> mapList = new ArrayList<>();
		List<TravelDayIndex4> list = unionPayMapper.getPerCapitalConsumptionList();
		int count = 1;
		for (TravelDayIndex4 travelDayIndex4 : list) {
			Map<String, Object> map = new HashMap<>();
			map.put("type1", count++);
			map.put("type2", travelDayIndex4.getSourceProvince());
			map.put("type3", travelDayIndex4.getTransNum());
			map.put("type4", new DecimalFormat("#").format(travelDayIndex4.getTransAt().divide(new BigDecimal("10000"), 2, RoundingMode.HALF_UP)) + "万");
			map.put("type5", travelDayIndex4.getAcctNum());
			mapList.add(map);
		}

		return mapList;
	}

	@Override
	public List<TravelDayIndex2VO> getIndustry() {
		List<TravelDayIndex2> industryEntities = unionPayMapper.getIndustry();
		List<TravelDayIndex2VO> industryVOList = new ArrayList<>();
		for (TravelDayIndex2 industryEntity : industryEntities) {
			TravelDayIndex2VO travelDayIndex2VO = new TravelDayIndex2VO();
			if ("吃".equals(industryEntity.getMccType())) {
				travelDayIndex2VO.setIndustryName("餐饮行业");
				travelDayIndex2VO.setCount(industryEntity.getTransNum());
				travelDayIndex2VO.setConsumerVisits(industryEntity.getAcctNum());
				BigDecimal transAt = industryEntity.getTransAt();
				travelDayIndex2VO.setTotalMoney(new DecimalFormat("#").format(transAt.divide(new BigDecimal("10000"), 2, RoundingMode.HALF_UP)) + "万");
				//总营业额
				BigDecimal totalMoney = industryEntity.getTransAt();
				//总笔数
				Integer consumerVisits = industryEntity.getTransNum();
				BigDecimal bigDecimal = new BigDecimal(consumerVisits);
				BigDecimal divide = totalMoney.divide(bigDecimal, 2, RoundingMode.HALF_UP);
				//人均消费
				travelDayIndex2VO.setConsumption(divide);
				industryVOList.add(travelDayIndex2VO);
			}
			if ("住".equals(industryEntity.getMccType())) {
				travelDayIndex2VO.setIndustryName("酒店行业");
				travelDayIndex2VO.setCount(industryEntity.getTransNum());
				travelDayIndex2VO.setConsumerVisits(industryEntity.getAcctNum());
				BigDecimal transAt = industryEntity.getTransAt();
				travelDayIndex2VO.setTotalMoney(new DecimalFormat("#").format(transAt.divide(new BigDecimal("10000"), 2, RoundingMode.HALF_UP)) + "万");
				//总营业额
				BigDecimal totalMoney = industryEntity.getTransAt();
				//总笔数
				Integer consumerVisits = industryEntity.getTransNum();
				BigDecimal bigDecimal = new BigDecimal(consumerVisits);
				BigDecimal divide = totalMoney.divide(bigDecimal, 2, RoundingMode.HALF_UP);
				//人均消费
				travelDayIndex2VO.setConsumption(divide);
				industryVOList.add(travelDayIndex2VO);
			}
			if ("购".equals(industryEntity.getMccType())) {
				travelDayIndex2VO.setIndustryName("零售行业");
				travelDayIndex2VO.setCount(industryEntity.getTransNum());
				travelDayIndex2VO.setConsumerVisits(industryEntity.getAcctNum());
				BigDecimal transAt = industryEntity.getTransAt();
				travelDayIndex2VO.setTotalMoney(new DecimalFormat("#").format(transAt.divide(new BigDecimal("10000"), 2, RoundingMode.HALF_UP)) + "万");                //总营业额
				BigDecimal totalMoney = industryEntity.getTransAt();
				//总笔数
				Integer consumerVisits = industryEntity.getTransNum();
				BigDecimal bigDecimal = new BigDecimal(consumerVisits);
				BigDecimal divide = totalMoney.divide(bigDecimal, 2, RoundingMode.HALF_UP);
				//人均消费
				travelDayIndex2VO.setConsumption(divide);
				industryVOList.add(travelDayIndex2VO);
			}
			if ("娱".equals(industryEntity.getMccType())) {
				travelDayIndex2VO.setIndustryName("娱乐行业");
				travelDayIndex2VO.setCount(industryEntity.getTransNum());
				travelDayIndex2VO.setConsumerVisits(industryEntity.getAcctNum());
				BigDecimal transAt = industryEntity.getTransAt();
				travelDayIndex2VO.setTotalMoney(new DecimalFormat("#").format(transAt.divide(new BigDecimal("10000"), 2, RoundingMode.HALF_UP)) + "万");                //总营业额
				BigDecimal totalMoney = industryEntity.getTransAt();
				//总笔数
				Integer consumerVisits = industryEntity.getTransNum();
				BigDecimal bigDecimal = new BigDecimal(consumerVisits);
				BigDecimal divide = totalMoney.divide(bigDecimal, 2, RoundingMode.HALF_UP);
				//人均消费
				travelDayIndex2VO.setConsumption(divide);
				industryVOList.add(travelDayIndex2VO);
			}

		}
		return industryVOList;
	}

	@Override
	public Map<String, String> average() {
		String average = unionPayMapper.average();
		String format = new DecimalFormat("#.##").format(Double.parseDouble(average));
		Map<String, String> map = new HashMap<>(16);
		map.put("value", "三亚市游客每笔消费" + format + "元");
		return map;
	}
}
