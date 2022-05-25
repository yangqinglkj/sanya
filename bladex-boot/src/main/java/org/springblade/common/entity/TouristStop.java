package org.springblade.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TouristStop implements Serializable {

	/**
	 *日期
	 */
	private String createTime;
	/**
	 * 1-3
	 */
	private String oneToThree;
	/**
	 * 4-7
	 */
	private String fourToSeven;
	/**
	 * 8-10
	 */
	private String eightToTen;
	/**
	 * 10
	 */
	private String ten;

	/**
	 * 区划id
	 */
	private String typeCode;

}
