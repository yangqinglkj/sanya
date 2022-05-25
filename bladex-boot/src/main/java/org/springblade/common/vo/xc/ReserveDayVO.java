package org.springblade.common.vo.xc;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yangqing
 */
@Data
public class ReserveDayVO implements Serializable {

	/**
	 * 0天
	 */
	private Integer zeroDay;

	/**
	 * 1-3天
	 */
	private Integer oneToThree;

	/**
	 * 3-7天
	 */
	private Integer threeToSeven;

	/**
	 * 7天以上
	 */
	private Integer sevenAbove;
}
