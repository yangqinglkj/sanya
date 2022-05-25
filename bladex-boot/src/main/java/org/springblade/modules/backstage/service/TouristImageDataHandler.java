package org.springblade.modules.backstage.service;


import org.springblade.common.vo.AreaVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

/**
 * @author yangqing
 */

public interface TouristImageDataHandler {
	/**
	 * 获取属性名
	 * @return 属性名
	 */
	String getProperty();

	/**
	 * 公共方法计算画像百分比、人数
	 * @param areaList 画像集合
	 * @param totalPeople 总人数
	 * @return 新画像集合
	 */
	static List<AreaVO> commonHandlerData(List<AreaVO> areaList,Integer totalPeople){
		for (AreaVO areaVO : areaList) {
			//百分比乘以人数  得到每一项所占的人数
			BigDecimal multiply = areaVO.getValue().multiply(new BigDecimal(totalPeople.toString()));
			//百分比乘以100 四舍五入保留小数点后两位
			areaVO.setValue(areaVO.getValue().multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_UP));
			areaVO.setPeople(multiply.intValue());
		}
		//获取集合中百分比总数
		BigDecimal totalPercent = areaList.stream().map(AreaVO::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
		//获取集合中人数总数
		int sum = areaList.stream().mapToInt(AreaVO::getPeople).sum();
		//百分比差值
		BigDecimal percentDifference = totalPercent.subtract(new BigDecimal(100));
		//获取最大百分比对象
		AreaVO areaVO = areaList.stream().max(Comparator.comparing(AreaVO::getValue)).get();
		//如果有差值，在最大百分比对象上加减
		if (percentDifference.compareTo(BigDecimal.ZERO) != 0){
			areaVO.setValue(areaVO.getValue().subtract(percentDifference));
		}
		//人数差值
		int peopleDifference = sum - totalPeople;
		if (sum - totalPeople != 0){
			areaVO.setPeople(areaVO.getPeople() - peopleDifference);
		}
		return areaList;
	}

	/**
	 * 计算画像百分比、人数
	 * @param areaList 画像集合
	 * @param totalPeople 总人数
	 * @return 新画像集合
	 */
	List<AreaVO> handlerData(List<AreaVO> areaList,Integer totalPeople);

}
