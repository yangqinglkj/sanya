package org.springblade.modules.backstage.vo.yl;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author yq
 * @Date 2020/9/28 21:40
 */
@Data
public class MonthMoneyAvgVO implements Serializable {
	/**
	 * 日期
	 */
	private String months;
	/**
	 * 省内
	 */
	private BigDecimal inner;
	/**
	 * 省外
	 */
	private BigDecimal outer;


}
