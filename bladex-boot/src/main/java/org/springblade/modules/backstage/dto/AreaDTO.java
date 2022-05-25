package org.springblade.modules.backstage.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yangqing
 */
@Data
public class AreaDTO implements Serializable {
//	/**
//	 * 属性名
//	 */
//	private String name;
//
//	/**
//	 * 百分比
//	 */
//	private Double value;

    /**
     * 总百分比
	 */
	private double count;
}
