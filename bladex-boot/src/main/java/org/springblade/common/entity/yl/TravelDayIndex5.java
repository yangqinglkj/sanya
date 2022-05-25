package org.springblade.common.entity.yl;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *重点商圈消费规模
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("bm_sanya_travel_day_index5")
public class TravelDayIndex5 implements Serializable {

	/**
	 * 主键
	 */
	@JsonIgnore
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 日期
	 */
	private String dealDay;

	/**
	 * 商圈名称
	 */
	private String zone;

	/**
	 * 游客类型（市内:0，跨市:1,跨省:2）
	 */
	private String travellerType;

	/**
	 * 游客消费金额(元)
	 */
	private BigDecimal transAt;

	/**
	 * 游客消费笔数(笔)
	 */
	private Integer transNum;

	/**
	 * 游客消费人次(人)
	 */
	private Integer acctNum;
}
