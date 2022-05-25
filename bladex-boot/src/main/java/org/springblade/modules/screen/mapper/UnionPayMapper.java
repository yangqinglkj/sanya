package org.springblade.modules.screen.mapper;

import org.springblade.common.entity.yl.TravelDayIndex2;
import org.springblade.common.entity.yl.TravelDayIndex4;
import org.springblade.common.vo.Index5VO;

import java.util.List;

/**
 * @author yangqing
 */
public interface UnionPayMapper {

	/**
	 * 主要消费区域和场所
	 * @param typeStr 1省内 2省外
	 * @return 列表
	 */
	List<Index5VO> consumerAres(String typeStr);

	/**
	 * 行业消费规模及贡献度
	 * @return 列表
	 */
	List<TravelDayIndex2> getIndustry();

	/**
	 * 人均消费省份排名
	 * @return 列表
	 */
	List<TravelDayIndex4> getPerCapitalConsumptionList();

	/**
	 * 三亚市人均消费
	 * @return 人均消费
	 */
	String average();
}
