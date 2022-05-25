package org.springblade.modules.backstage.service.lmpl;


import org.springblade.common.vo.AreaVO;
import org.springblade.modules.backstage.service.TouristImageDataHandler;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yangqing
 */
@Component
public class ShoppingTouristImageDataHandler implements TouristImageDataHandler {

	@Override
	public String getProperty() {
		return "shopping";
	}

	@Override
	public List<AreaVO> handlerData(List<AreaVO> areaList, Integer totalPeople) {
		List<AreaVO> shoppingList = TouristImageDataHandler.commonHandlerData(areaList, totalPeople);
		shoppingList.forEach(shopping -> {
			if ("家具家居建材".equals(shopping.getName())){
				shopping.setName("其他类型");
			}
		});
		return shoppingList;
	}
}
