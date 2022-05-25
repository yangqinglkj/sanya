package org.springblade.modules.screen.service;

import org.springblade.common.entity.yl.*;
import org.springblade.common.vo.Index5VO;
import org.springblade.common.vo.IndustryVO;
import org.springblade.common.vo.TravelDayIndex2VO;
import org.springblade.common.vo.TravelDayIndex5VO;

import java.util.List;
import java.util.Map;

/**
 * @Author yq
 * @Date 2020/9/17 15:40
 */

public interface UnionPayService {

	/**
	 * 主要消费区域和场所
	 * @return list
	 */
	List<Index5VO> findAll(Integer type);

	/**
	 * 人均消费省份
	 * @return list
	 */
	List<Map<String,Object>> getPerCapitalConsumptionList();

	/**
	 * 行业消费规模及贡献度
	 * @return list
	 */
	List<TravelDayIndex2VO> getIndustry();

	/**
	 * 三亚市人均消费
	 * @return 人均消费
	 */
	Map<String,String> average();


}
