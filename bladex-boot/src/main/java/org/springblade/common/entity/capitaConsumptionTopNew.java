package org.springblade.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class capitaConsumptionTopNew  implements Serializable {

	/**
	 * 消费金额
	 */
	private String transAt;
	/**
	 * 消费笔数
	 */
	private String transNum;
	/**
	 * 消费人次
	 */
	private String acctNum;
	/**
	 * 每笔消费
	 */
	private String consume;
	/**
	 * 省份
	 */
	private String sourceProvince;
}
