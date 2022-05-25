package org.springblade.modules.backstage.mapper;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springblade.common.entity.tx.area.AreaRealTimePeople;
import org.springblade.common.vo.AreaVO;
import org.springblade.modules.backstage.vo.TimeVO;

import java.util.List;

/**
 * @Author yq
 * @Date 2020/10/6 15:53
 */

public interface TxAreaMapper {

	/**
	 * 总人数
	 *
	 * @param typeCode  区域id
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return 性别百分比
	 */
	List<Integer> getTotalPeople(@Param("typeCode") String typeCode, @Param("startTime") String startTime, @Param("endTime") String endTime);


	/**
	 * 区域画像
	 *
	 * @param property  画像类型
	 * @param typeCode  区域id
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @param size      集合长度
	 * @return 性别百分比
	 */
	List<AreaVO> areaImage(@Param("property") String property, @Param("typeCode") String typeCode, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("size") Integer size);


	/**
	 * 区域日累计人流量
	 *
	 * @param page      分页对象
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @param typeCode  区域id
	 * @return 列表
	 */
	IPage<AreaRealTimePeople> visitorsCount(IPage<AreaRealTimePeople> page, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("typeCode") String typeCode);

	/**
	 * 区域日累计人流量（按日筛选）
	 *
	 * @param page      分页对象
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @param typeCode  区域id
	 * @return 列表
	 */
	IPage<AreaRealTimePeople> visitorsCountByDay(IPage<AreaRealTimePeople> page, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("typeCode") String typeCode);

	/**
	 * 获取进入 离开的总人数
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param typeCode 区域id
	 * @return 对象
	 */
	AreaRealTimePeople getCount(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("typeCode") String typeCode);

	/**
	 * 区域日累计人流量 总数
	 *
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @param typeCode  区域id
	 * @return 列表
	 */
	AreaRealTimePeople getCountByDay(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("typeCode") String typeCode);


	/**
	 * 获取最新时间和最老时间
	 *
	 * @return 时间对象
	 */
	TimeVO getTime(@Param("typeCode") String typeCode);
}
