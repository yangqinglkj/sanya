package org.springblade.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpTemporary implements Serializable {

	/**
	 * 省份名
	 */
	private String name;
	/**
	 * 省份对应的值
	 */
	private BigDecimal nameValue;
}
