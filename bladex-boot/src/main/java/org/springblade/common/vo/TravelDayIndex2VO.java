package org.springblade.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class TravelDayIndex2VO implements Serializable {

	/**
	 * 行业名称
	 */
	private String industryName;

	/**
	 * 交易总笔数
	 */
	private Integer count;

	/**
	 * 交易总金额
	 */
	private String totalMoney;
	/**
	 * 消费人次
	 */
	private Integer consumerVisits;

	/**
	 * 人均消费
	 */
	private BigDecimal consumption;
}
