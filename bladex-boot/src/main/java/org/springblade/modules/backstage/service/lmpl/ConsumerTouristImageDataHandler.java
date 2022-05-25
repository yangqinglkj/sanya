package org.springblade.modules.backstage.service.lmpl;


import org.springblade.common.vo.AreaVO;
import org.springblade.modules.backstage.service.TouristImageDataHandler;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yangqing
 */
@Component
public class ConsumerTouristImageDataHandler implements TouristImageDataHandler {

	@Override
	public String getProperty() {
		return "consumer";
	}

	@Override
	public List<AreaVO> handlerData(List<AreaVO> areaList, Integer totalPeople) {
		List<AreaVO> consumerList = TouristImageDataHandler.commonHandlerData(areaList, totalPeople);
		consumerList.forEach(consumer -> {
			if ("次低".equals(consumer.getName())){
				consumer.setName("较低");
			}
			if ("次高".equals(consumer.getName())){
				consumer.setName("较高");
			}
		});
		return consumerList;
	}
}
