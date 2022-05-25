package org.springblade.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author yangqing
 */
@Data
public class IndustryVO implements Serializable {

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
	private BigDecimal totalMoney;
	/**
	 * 消费人次
	 */
	private Integer consumerVisits;

	/**
	 * 人均消费
	 */
	private BigDecimal consumption;


}
