package org.springblade.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yangqing
 */
@Data
public class TrafficVO implements Serializable {

	private String car;
	private String plane;
	private String train;
}
