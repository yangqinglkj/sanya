package org.springblade.modules.backstage.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author yq
 * @Date 2020/12/3 12:00
 */

@Data
public class TimeVO implements Serializable {
	/**
	 * 最新时间
	 */
	private String maxTime;
	/**
	 * 最晚时间
	 */
	private String minTime;
}
