package org.springblade.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yangqing
 */
@Data
public class ZoneVO implements Serializable {
	/**
	 * 属性名
	 */
	private String property;

	/**
	 * 百分比
	 */
	private Double percent;

    /**
     * 人数
	 */
	private Integer population;
}
