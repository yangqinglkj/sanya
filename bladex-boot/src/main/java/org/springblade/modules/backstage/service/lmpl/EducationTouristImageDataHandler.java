package org.springblade.modules.backstage.service.lmpl;


import org.springblade.common.vo.AreaVO;
import org.springblade.modules.backstage.service.TouristImageDataHandler;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yangqing
 */
@Component
public class EducationTouristImageDataHandler implements TouristImageDataHandler {

	@Override
	public String getProperty() {
		return "education";
	}

	@Override
	public List<AreaVO> handlerData(List<AreaVO> areaList, Integer totalPeople) {
		areaList.forEach(vo ->{
			if ("博士".equals(vo.getName())){
				vo.setName("博士及以上");
			}
		});
		return TouristImageDataHandler.commonHandlerData(areaList, totalPeople);
	}


}
