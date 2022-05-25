package org.springblade.modules.backstage.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springblade.common.entity.capitaConsumptionTopNew;
import org.springblade.common.entity.yl.TravelDayIndex5;
import org.springblade.modules.backstage.vo.ConsumerAresGraphVO;
import org.springblade.modules.backstage.vo.yl.CityConsumptionVO;
import org.springblade.modules.backstage.vo.yl.ConsumerAresVO;

import java.util.List;
import java.util.Map;

/**
 * @Author yq
 * @Date 2020/9/17 15:40
 */

public interface YlBackstageService {

	/**
	 * 主要消费区域和场所
	 *
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @param page      分页对象
	 * @param type      1省内 2省外
	 * @param business  商圈
	 * @return 列表
	 */
	Map<String,Object> consumerAres(IPage<TravelDayIndex5> page, String startTime, String endTime, String type, String business);

	/**
	 * 主要消费区域和场所 折线图
	 *
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @param type      1省内 2省外
	 * @param business  商圈
	 * @return 每月人均消费
	 */
	Map<String, Object> consumerAresGraph(String startTime, String endTime,  String type,String business);


	/**
	 * 三亚市全市消费
	 * @param page      分页对象
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return 列表
	 */
	Map<String, Object> cityConsumption(IPage<CityConsumptionVO> page,String startTime, String endTime);

	/**
	 * 人均消费省份TOP10
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	Map<String,Object> capitaConsumption(String startTime, String endTime);

	/**
	 * 行业消费规模及贡献度
	 * @param startTime
	 * @param endTime
	 * @param page
	 * @return
	 */
	Map<String, Object> consumptionScaleA(String startTime, String endTime,IPage page);
}
