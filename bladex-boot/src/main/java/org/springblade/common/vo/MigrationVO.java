package org.springblade.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class MigrationVO implements Serializable {
	/**
	 * 迁入总人数
	 */
	private String inPopulation;

	/**
	 * 迁出总人数
	 */
	private String outPopulation;

	/**
	 * 迁入汽车人数
	 */
	private String inCar;

	/**
	 * 迁出汽车人数
	 */
	private String outCar;

	/**
	 * 迁入汽车百分比
	 */
	private String inCarPercent;

	/**
	 * 迁出汽车百分比
	 */
	private String outCarPercent;

	/**
	 * 迁入飞机人数
	 */
	private String inPlane;

	/**
	 * 迁出飞机人数
	 */
	private String outPlane;

	/**
	 * 迁入飞机百分比
	 */
	private String inPlanePercent;

	/**
	 * 迁出飞机百分比
	 */
	private String outPlanePercent;


	/**
	 * 迁入火车人数
	 */
	private String inTrain;

	/**
	 * 迁出火车人数
	 */
	private String outTrain;

	/**
	 * 迁入火车百分比
	 */
	private String inTrainPercent;

	/**
	 * 迁出火车百分比
	 */
	private String outTrainPercent;

	/**
	 * 创建时间
	 */
	private LocalDate createTime;
}
