package org.springblade.common.entity.yl;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 跨省游客来源地分布（按省）
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("bm_sanya_travel_day_index3")
public class TravelDayIndex3 implements Serializable {

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 日期
	 */
	private String dealDay;

	/**
	 * 目的地
	 */
	private String consumeArea;

	/**
	 * 来源省份
	 */
	private String sourceProvince;

	/**
	 * 游客消费人次(人)
	 */
	private Integer acctNum;

	/**
	 * 游客消费人次占比
	 */
	private Integer acctNumRatio;

	/**
	 * 排名
	 */
	private Integer rank;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
}
