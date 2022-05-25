package org.springblade.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowAndFree implements Serializable {

	/**
	 *日期
	 */
	private String months;
	private String top1;
	private String top2;

	/**
	 * 跟团游
	 */
	private String follow;

	/**
	 * 自由行
	 */
	private String free;


}
