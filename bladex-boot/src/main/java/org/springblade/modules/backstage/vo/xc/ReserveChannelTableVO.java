package org.springblade.modules.backstage.vo.xc;

import lombok.Data;

import java.time.LocalDate;

/**
 * @Author yq
 * @Date 2020/9/27 19:10
 */
@Data
public class ReserveChannelTableVO {
	/**
	 * 月份
	 */
	private LocalDate createTime;
	/**
	 * H5
	 */
	private String H5;
	/**
	 * 小程序
	 */
	private String miniProgram;
	/**
	 * 其他
	 */
	private String other;
	/**
	 *分销
	 */
	private String allianceApi;
	/**
	 *PC
	 */
	private String online;
	/**
	 * APP
	 */
	private String app;
	private String aliPay;
	private String baidu;
}
