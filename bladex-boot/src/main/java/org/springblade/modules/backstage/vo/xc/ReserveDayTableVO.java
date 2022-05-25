package org.springblade.modules.backstage.vo.xc;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author yq
 * @Date 2020/9/28 11:05
 */

@Data
public class ReserveDayTableVO implements Serializable {
	private String createTime;
	private String type;
	private String zero;
	private String oneToThree;
	private String fourToSeven;
	private String sevenOver;
}
