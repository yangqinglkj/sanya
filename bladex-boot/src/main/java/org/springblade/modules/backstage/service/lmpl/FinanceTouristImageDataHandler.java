package org.springblade.modules.backstage.service.lmpl;


import org.springblade.common.vo.AreaVO;
import org.springblade.modules.backstage.service.TouristImageDataHandler;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yangqing
 */
@Component
public class FinanceTouristImageDataHandler implements TouristImageDataHandler {

	@Override
	public String getProperty() {
		return "finance";
	}

	@Override
	public List<AreaVO> handlerData(List<AreaVO> areaList, Integer totalPeople) {
		return TouristImageDataHandler.commonHandlerData(areaList,totalPeople);
	}
}
