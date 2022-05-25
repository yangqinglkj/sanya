package org.springblade.modules.backstage.service.lmpl;


import org.springblade.common.vo.AreaVO;
import org.springblade.modules.backstage.service.TouristImageDataHandler;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yangqing
 */
@Component
public class LifeTouristImageDataHandler implements TouristImageDataHandler {

	@Override
	public String getProperty() {
		return "life";
	}

	@Override
	public List<AreaVO> handlerData(List<AreaVO> areaList, Integer totalPeople) {
//		List<AreaVO> collect = areaList.stream().filter(name -> name.getName().contains("学生")).collect(Collectors.toList());
//		//学生总人数
//		int studentTotalPeople = collect.stream().mapToInt(AreaVO::getPeople).sum();
//		BigDecimal studentTotalPercent = collect.stream().map(AreaVO::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
//		areaList.removeIf(item -> item.getName().contains("学生"));
//		AreaVO areaVO = new AreaVO();
//		areaVO.setName("学生");
//		areaVO.setValue(studentTotalPercent);
//		areaVO.setPeople(studentTotalPeople);
//		areaList.add(areaVO);
		List<BigDecimal> studentPercent = new ArrayList<>();
		List<AreaVO> list = new ArrayList<>();
		areaList.forEach(areaVO -> {
			if (areaVO.getName().contains("学生")){
				studentPercent.add(areaVO.getValue());
			}else {
				list.add(areaVO);
			}
		});
		AreaVO vo = new AreaVO();
		vo.setName("学生");
		vo.setValue(studentPercent.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
		list.add(vo);
		return TouristImageDataHandler.commonHandlerData(list,totalPeople);
	}
}
