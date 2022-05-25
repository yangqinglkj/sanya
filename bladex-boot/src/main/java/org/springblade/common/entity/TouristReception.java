package org.springblade.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TouristReception implements Serializable {

	/**
	 *时间
	 */
	private LocalDateTime createTime;
	/**
	 * 0三亚市 1海棠区 2吉阳区 3天涯区 4崖州区
	 */
	private String typeCode;
	/**
	 * 省内（海南省）
	 */
	private String prov;
	//省内百分比
	private String provValue;
	/**
	 * 省外
	 */
	private String outprov;
	//省外百分比
	private String outprovValue;
	/**
	 * 当天对应的总人数
	 */
	private Integer totalPeople;
}
