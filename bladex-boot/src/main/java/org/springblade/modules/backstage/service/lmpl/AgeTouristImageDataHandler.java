package org.springblade.modules.backstage.service.lmpl;


import org.springblade.common.vo.AreaVO;
import org.springblade.modules.backstage.service.TouristImageDataHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yangqing
 */
@Component
public class AgeTouristImageDataHandler implements TouristImageDataHandler {

	@Override
	public String getProperty() {
		return "age";
	}

	@Override
	public List<AreaVO> handlerData(List<AreaVO> areaList, Integer totalPeople) {
//		List<BigDecimal> otherList = new ArrayList<>();
//		List<AreaVO> list = new ArrayList<>();
//		for (AreaVO areaVO : areaList) {
//			if ("70以上".equals(areaVO.getName()) || "other".equals(areaVO.getName())) {
//				otherList.add(areaVO.getValue());
//			} else {
//				list.add(areaVO);
//			}
//		}
//		AreaVO vo = new AreaVO();
//		vo.setName("70岁以上");
//		vo.setValue(otherList.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
//		list.add(vo);
		return TouristImageDataHandler.commonHandlerData(areaList,totalPeople);
	}
}
