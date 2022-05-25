package org.springblade.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author yq
 * @Date 2020/9/21 17:08
 */

@Data
public class EducationPopulationVO implements Serializable {
	/**
	 * 属性名
	 */
	private String property;

	/**
	 * 人数
	 */
	private Integer population;

}
