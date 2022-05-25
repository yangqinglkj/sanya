package org.springblade.common.vo.xc.area;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yangqing
 */
@Data
public class AreaBusinessVO implements Serializable {

	/**
	 * 人数
	 */
	private Integer people;

	/**
	 * 区域id
	 */
//	@JsonIgnore
	private String areaCode;

	/**
	 * 区域名称
	 */
	private String areaName;
}
