package org.springblade.modules.backstage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springblade.common.entity.tx.area.AreaRealTimePeople;

import java.util.Map;

/**
 * @Author yq
 * @Date 2020/9/17 15:40
 */

public interface TxAreaService {
	/**
	 * 画像分析
	 *
	 * @param typeCode  区域id
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return vo对象
	 */
	Map<String, Object> touristImage(String typeCode, String startTime, String endTime);

	/**
	 * 区域日累计人流量
	 *
	 * @param page      分页对象
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @param typeCode  区域id
	 * @return 列表
	 */
	IPage<AreaRealTimePeople> visitorsCount(IPage<AreaRealTimePeople> page, String startTime, String endTime, String typeCode);

	/**
	 * 区域日累计人流量（按日筛选）
	 *
	 * @param page      分页对象
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @param typeCode  区域id
	 * @return 列表
	 */
	Map<String, Object> visitorsCountByDay(IPage<AreaRealTimePeople> page, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("typeCode") String typeCode);
}
