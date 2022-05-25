package org.springblade.common.entity.yl;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 三亚市各行业消费情况及贡献度
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("bm_sanya_travel_day_index2")
public class TravelDayIndex2 implements Serializable {

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
	 * 行业（吃:1,住:2,购:5,娱:6）
	 */
	private String mccType;

	/**
	 * 游客消费金额(元)
	 */
	private BigDecimal transAt;

	/**
	 *游客消费笔数(笔)
	 */
	private Integer transNum;

	/**
	 * 游客消费人次(人)
	 */
	private Integer acctNum;

	/**
	 * 行业消费贡献度
	 */
	private BigDecimal transAtCr;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
}
