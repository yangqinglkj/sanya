package org.springblade.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SanYaAnalysisOrigin implements Serializable {

	/**
	 * 客源地名称
	 */
	private String name;
	/**
	 * 访客人数
	 */
	private String peopleNum;
	/**
	 * 百分比
	 */
	private String percent;
}
