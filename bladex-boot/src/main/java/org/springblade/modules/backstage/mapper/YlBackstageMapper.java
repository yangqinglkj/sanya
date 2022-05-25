package org.springblade.modules.backstage.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springblade.common.entity.yl.TravelDayIndex5;
import org.springblade.modules.backstage.vo.ConsumerAresGraphVO;
import org.springblade.modules.backstage.vo.TimeVO;
import org.springblade.modules.backstage.vo.yl.CityConsumptionVO;

import java.util.List;

/**
 * @Author yq
 * @Date 2020/9/24 15:53
 */

public interface YlBackstageMapper {

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
	IPage<TravelDayIndex5> consumerAres(IPage<TravelDayIndex5> page, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("type") String type, @Param("business") String business);

	/**
	 * 主要消费区域和场所 折线图
	 *
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @param type      1省内 2省外
	 * @param business  商圈
	 * @return 每月人均消费
	 */
	List<ConsumerAresGraphVO> consumerAresGraph(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("type") String type,@Param("business") String business);

	/**
	 * 三亚市全市消费
	 * @param page      分页对象
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return 列表
	 */
	IPage<CityConsumptionVO> cityConsumption(IPage<CityConsumptionVO> page,@Param("startTime") String startTime, @Param("endTime") String endTime);

	List<CityConsumptionVO> allCountA(@Param("startTime") String startTime, @Param("endTime") String endTime);


	List<TravelDayIndex5> findAllCount(String startTime, String endTime, String type, String business);

	IPage<TravelDayIndex5> allBusinessDistrict(IPage<TravelDayIndex5> page, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("type") String type);

	List<TravelDayIndex5> allBusinessDistrictCount(String startTime, String endTime, String type);

	/**
	 * 获取最新时间和最老时间
	 * @param type      1省内 2省外
	 * @param business  商圈
	 * @return 时间对象
	 */
	TimeVO getConsumerAresTime(@Param("type") String type, @Param("business") String business);

	/**
	 * 获取最新时间和最老时间
	 * @return 时间对象
	 */
	TimeVO getCityConsumptionTime();
}
