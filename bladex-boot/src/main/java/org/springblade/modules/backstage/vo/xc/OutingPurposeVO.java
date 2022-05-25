package org.springblade.modules.backstage.vo.xc;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author yq
 * @Date 2020/10/22 17:15
 */

@Data
public class OutingPurposeVO implements Serializable {
	/**
	 * 创建使劲按
	 */
	private String createTime;
	/**
	 * 亲子出行
	 */
	private String child;
	/**
	 * 陪老人
	 */
	private String oldPeople;
	/**
	 * 商务
	 */
	private String business;
	/**
	 * 度假
	 */
	private String holiday;
}
