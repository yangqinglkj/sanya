package org.springblade.common.mapper.yl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.common.entity.consumptionScale;
import org.springblade.common.entity.yl.TravelDayIndex2;
import org.springblade.modules.backstage.vo.TimeVO;

import java.util.List;

public interface TravelDayIndex2Mapper extends BaseMapper<TravelDayIndex2> {


	IPage<consumptionScale> consumptionScaleA(String startTime, String endTime, IPage page);

	List<consumptionScale> finbByAllCount(String startTime, String endTime);

	/**
	 * 获取最新时间和最老时间
	 * @return 时间对象
	 */
	TimeVO getConsumptionScaleATime();
}
