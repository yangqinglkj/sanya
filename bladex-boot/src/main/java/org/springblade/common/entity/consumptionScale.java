package org.springblade.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class consumptionScale implements Serializable {

	/**
	 *日期
	 */
	private String dealDay;

	private String top1;
	private String top2;
	private String top3;
	private String top4;
/*	//住
	private String top1LiveCountNumber;
	private String top1LiveMoney;
	private String top1LiveCountNumberConsumers;
	private String top1LiveEvery;
	//吃
	private String top2EatCountNumber;
	private String top2EatMoney;
	private String top2EatCountNumberConsumers;
	private String top2EatEvery;
	//娱
	private String top3AmuCountNumber;
	private String top3AmuMoney;
	private String top3AmuCountNumberConsumers;
	private String top3AmuEvery;
	//购
	private String top4ShopCountNumber;
	private String top4ShopMoney;
	private String top4ShopCountNumberConsumers;
	private String top4ShopEvery;*/

}
