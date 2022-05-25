package org.springblade.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class capitaConsumptionTen implements Serializable {

	/**
	 *日期
	 */
	private String dealDay;
	/**
	 * 排名1-10
	 */
	private String top1;
	private String top2;
	private String top3;
	private String top4;
	private String top5;
	private String top6;
	private String top7;
	private String top8;
	private String top9;
	private String top10;


}
