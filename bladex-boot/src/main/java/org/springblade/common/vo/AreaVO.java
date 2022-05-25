package org.springblade.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author yangqing
 */
@Data
public class AreaVO implements Serializable {
	/**
	 * 属性名
	 */
	private String name;

	/**
	 * 百分比
	 */
	private BigDecimal value;

	/**
	 * 人数
	 */
	private Integer people;
}
