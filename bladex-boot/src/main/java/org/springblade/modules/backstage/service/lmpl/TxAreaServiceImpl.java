package org.springblade.modules.backstage.service.lmpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.common.entity.tx.area.AreaRealTimePeople;
import org.springblade.common.utils.SpringContextUtil;
import org.springblade.common.vo.AreaVO;
import org.springblade.modules.backstage.mapper.TxAreaMapper;
import org.springblade.modules.backstage.service.TouristImageDataHandler;
import org.springblade.modules.backstage.service.TxAreaService;
import org.springblade.modules.backstage.vo.TimeVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author yq
 * @Date 2020/9/17 15:40
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TxAreaServiceImpl implements TxAreaService {

	@Resource
	private TxAreaMapper txAreaMapper;

	private static final Map<String, TouristImageDataHandler> HANDLER_MAP = new HashMap<>();

	private static final List<String> PROPERTY_LIST = Arrays.asList("gender", "car", "age", "education", "consumer", "shopping","life","finance");


	@Override
	public Map<String, Object> touristImage(String typeCode, String startTime, String endTime) {
		Map<String, Object> map = new HashMap<>(16);
		if(startTime.equals(endTime) && !StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)){
			LocalDateTime dateTime = LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			LocalDateTime time = dateTime.plusDays(1);
			endTime = time.getYear()+"-"+time.getMonthValue()+"-"+time.getDayOfMonth();
		}

		//总人数
		List<Integer> totalPeople = txAreaMapper.getTotalPeople(typeCode, startTime, endTime);
		if (!CollectionUtils.isEmpty(totalPeople)){
			int sum = totalPeople.stream().mapToInt(x -> x).sum();
			//获取父接口所有子类
			Map<String, TouristImageDataHandler> beanListOfType = (Map<String, TouristImageDataHandler>) SpringContextUtil.getBeanListOfType(TouristImageDataHandler.class);
			beanListOfType.forEach((k, v) -> {
				HANDLER_MAP.put(v.getProperty(), v);
			});
			String finalEndTime = endTime;
			PROPERTY_LIST.forEach(property -> {
				List<AreaVO> ageList = txAreaMapper.areaImage(property, typeCode, startTime, finalEndTime, totalPeople.size());
				if (!CollectionUtils.isEmpty(ageList)){
					TouristImageDataHandler handler = HANDLER_MAP.get(property);
					if (null != handler) {
						if ("shopping".equals(property)){
							map.put(property, this.handlerDataShopping(ageList, sum,totalPeople.size()));
						}else {
							map.put(property, handler.handlerData(ageList, sum));
						}
					}
				}
			});
		}
		return map;
	}


	/**
	 * 区域画像购物
	 * @param areaList
	 * @param totalPeople
	 * @param size
	 * @return
	 */
	public List<AreaVO> handlerDataShopping(List<AreaVO> areaList, Integer totalPeople,int size) {
		if (size==1){//代表一天 返回原样数据
			for (AreaVO areaVO : areaList) {
				//百分比乘以人数  得到每一项所占的人数
				BigDecimal multiply = areaVO.getValue().multiply(new BigDecimal(totalPeople.toString()));
				//百分比乘以100 四舍五入保留小数点后两位
				areaVO.setValue(areaVO.getValue().multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_UP));
				areaVO.setPeople(multiply.intValue());
				if ("家具家居建材".equals(areaVO.getName())){
					areaVO.setName("其它类型");
				}
			}
		}else {
			BigDecimal reduceTotal = areaList.stream().map(AreaVO::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
			for (AreaVO areaVO : areaList) {
				//每一项除以总百分比
				BigDecimal divide = areaVO.getValue().divide(reduceTotal,4, BigDecimal.ROUND_HALF_UP);
				//百分比乘以100 四舍五入保留小数点后两位
				areaVO.setValue(divide.multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_UP));
				areaVO.setPeople(divide.multiply(new BigDecimal(totalPeople.toString())).intValue());
				if ("家具家居建材".equals(areaVO.getName())){
					areaVO.setName("其它类型");
				}
			}
		}
		return areaList;
	}


	@Override
	public IPage<AreaRealTimePeople> visitorsCount(IPage<AreaRealTimePeople> page, String startTime, String endTime, String typeCode) {
		IPage<AreaRealTimePeople> peoplePage = txAreaMapper.visitorsCount(page, startTime, endTime, typeCode);
		AreaRealTimePeople count = txAreaMapper.getCount(startTime, endTime, typeCode);
		List<AreaRealTimePeople> recordList = peoplePage.getRecords();
		if (!StringUtils.isEmpty(count)) {
			String inPeople = count.getInPeople();
			String outPeople = count.getOutPeople();
			DecimalFormat de = new DecimalFormat("00");
			count.setInPeople(de.format(Double.parseDouble(inPeople)));
			count.setOutPeople(de.format(Double.parseDouble(outPeople)));
			recordList.add(count);
		}
		return peoplePage;
	}

	@Override
	public Map<String, Object> visitorsCountByDay(IPage<AreaRealTimePeople> page, String startTime, String endTime, String typeCode) {
		IPage<AreaRealTimePeople> peoplePage = txAreaMapper.visitorsCountByDay(page, startTime, endTime, typeCode);
		AreaRealTimePeople count = txAreaMapper.getCountByDay(startTime, endTime, typeCode);
		List<AreaRealTimePeople> recordList = peoplePage.getRecords();
		if (!StringUtils.isEmpty(count)) {
			String inPeople = count.getInPeople();
			String outPeople = count.getOutPeople();
			DecimalFormat de = new DecimalFormat("00");
			count.setInPeople(de.format(Double.parseDouble(inPeople)));
			count.setOutPeople(de.format(Double.parseDouble(outPeople)));
			recordList.add(count);
		}
		for (AreaRealTimePeople people : recordList) {
			people.setInPeople(new DecimalFormat("#").format(Double.parseDouble(people.getInPeople())));
			people.setOutPeople(new DecimalFormat("#").format(Double.parseDouble(people.getOutPeople())));
		}
		TimeVO time = txAreaMapper.getTime(typeCode);
		Map<String, Object> map = new HashMap<>(16);
		map.put("option", peoplePage);
		map.put("maxTime",time.getMaxTime());
		map.put("minTime",time.getMinTime());
		return map;
	}


}
