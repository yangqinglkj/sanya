package org.springblade.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yangqing
 */
@Data
public class AnalysisVO implements Serializable {
	/**
	 * 属性名
	 */
	private String name;

	/**
	 * 百分比
	 */
	private String value;
}
