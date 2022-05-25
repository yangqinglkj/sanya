package org.springblade.common.vo.xc;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class TransportationVO implements Serializable {

	/**
	 * 机票
	 */
	private String plane;

	/**
	 * 汽车票
	 */
	private String car;

	/**
	 * 其他
	 */
	private String other;

	/**
	 * 日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate createTime;
}
