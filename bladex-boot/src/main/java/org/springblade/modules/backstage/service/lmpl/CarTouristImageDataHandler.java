package org.springblade.modules.backstage.service.lmpl;


import org.springblade.common.vo.AreaVO;
import org.springblade.modules.backstage.service.TouristImageDataHandler;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

/**
 * @author yangqing
 */
@Component
public class CarTouristImageDataHandler implements TouristImageDataHandler {

	@Override
	public String getProperty() {
		return "car";
	}

	@Override
	public List<AreaVO> handlerData(List<AreaVO> areaList, Integer totalPeople) {
		List<AreaVO> carList = TouristImageDataHandler.commonHandlerData(areaList, totalPeople);
		carList.forEach(car -> {
			if ("是".equals(car.getName())){
				car.setName("有车");
			}
			if ("没车".equals(car.getName())){
				car.setName("无车");
			}
			if ("否".equals(car.getName())){
				car.setName("无车");
			}
		});
		return carList;
	}


}
