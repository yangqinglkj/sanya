package org.springblade.common.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yangqing
 */
@Data
public class AreaPopulationVO implements Serializable {

	/**
	 * 区名
	 */
	private String property;

	/**
	 * 区人数
	 */
	private String percent;

	/**
	 * 0三亚市 1海棠区 2吉阳区 3天涯区 4崖州区
	 */
	@JsonIgnore
	private Integer typeCode;

}
