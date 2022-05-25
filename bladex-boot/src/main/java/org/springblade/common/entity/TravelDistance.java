package org.springblade.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelDistance implements Serializable {

	/**
	 *时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime createTime;
	/**
	 * 0三亚市 1海棠区 2吉阳区 3天涯区 4崖州区
	 */
	private String typeCode;
	/**
	 * 近途人数
	 */
	private String nearlyWayPeople;
	/**
	 * 近途百分比
	 */
	private String nearlyWayPercent;
	/**
	 * 短途人数
	 */
	private String shortPeople;
	/**
	 * 短途百分比
	 */
	private String shortPercent;
	/**
	 * 中途人数
	 */
	private String halfwayPeople;
	/**
	 * 中途百分比
	 */
	private String halfwayPercent;
	/**
	 * 长途人数
	 */
	private String longPeople;
	/**
	 * 长途百分比
	 */
	private String longPercent;
	/**
	 * 当天对应的总人数
	 */
	private Integer totalPeople;
}
