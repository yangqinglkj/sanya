package org.springblade.common.mapper.yl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springblade.common.entity.capitaConsumptionTen;
import org.springblade.common.entity.capitaConsumptionTopNew;
import org.springblade.common.entity.yl.TravelDayIndex4;
import org.springblade.modules.backstage.vo.TimeVO;

import java.util.List;

public interface TravelDayIndex4Mapper extends BaseMapper<TravelDayIndex4> {

	List<capitaConsumptionTopNew> capitaConsumption(String startTime, String endTime);

	/**
	 * 获取最新时间
	 * @return 最新时间
	 */
	String getNewTime();

	//大屏查询最新
	@Select("SELECT * from  bm_sanya_travel_day_index4  WHERE  deal_day = (SELECT MAX(deal_day)  FROM  bm_sanya_travel_day_index4 ) ORDER BY rank ASC LIMIT 10")
	List<TravelDayIndex4> bigScreen();

	/**
	 * 获取最新时间和最老时间
	 * @return 时间对象
	 */
	TimeVO getCapitaConsumptionTime();

}
