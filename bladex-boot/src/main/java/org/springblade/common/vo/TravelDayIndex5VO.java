package org.springblade.common.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TravelDayIndex5VO {

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
	private String transAt;

	/**
	 * 游客消费笔数(笔)
	 */
	private Integer transNum;

	/**
	 * 游客消费人次(人)
	 */
	private Integer acctNum;

	/**
	 * 创建时间
	 */
	private String createTime;

}
