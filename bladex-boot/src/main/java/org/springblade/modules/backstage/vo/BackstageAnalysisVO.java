package org.springblade.modules.backstage.vo;

import lombok.Data;

/**
 * @Author yq
 * @Date 2020/9/25 10:36
 */

@Data
public class BackstageAnalysisVO {
	/**
	 * 属性名
	 */
	private String name;

	/**
	 * 百分比
	 */
	private String value;
	/**
	 * 人数
	 */
	private Integer population;
}
