package org.springblade.common.vo.xc;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yangqing
 */
@Data
public class ReserveChannelVO implements Serializable {

	/**
	 * 渠道名
	 */
	private String data;

	/**
	 * 百分比
	 */
	private String value;


}
