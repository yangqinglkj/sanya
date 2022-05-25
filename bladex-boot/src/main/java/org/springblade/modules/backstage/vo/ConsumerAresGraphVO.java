package org.springblade.modules.backstage.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author yq
 * @Date 2020/9/28 21:40
 */
@Data
public class ConsumerAresGraphVO implements Serializable {
	/**
	 * 日期
	 */
	private String dealDay;
	/**
	 * 游客类型（市内:0，跨市:1,跨省:2）
	 */
	private String travellerType;

	/**
	 * 游客消费金额(元)
	 */
	private BigDecimal transAt;

	/**
	 * 游客消费人次(人)
	 */
	private Integer acctNum;

	/**
	 * 人均消费
	 */
	private BigDecimal perCapita;

}
