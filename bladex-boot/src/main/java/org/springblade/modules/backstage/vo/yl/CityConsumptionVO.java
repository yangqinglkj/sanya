package org.springblade.modules.backstage.vo.yl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yangqing
 */
@Data
public class CityConsumptionVO implements Serializable {
	/**
	 * 年
	 */
	@JsonIgnore
	private String years;
	/**
	 * 月份
	 */
	@JsonIgnore
	private String months;
	/**
	 * 日
	 */
	@JsonIgnore
	private String days;
	/**
	 * 总消费金额
	 */
	private String totalTransAt;
	/**
	 * 总消费笔数
	 */
	private String totalTransNum;
	/**
	 * 总消费人次
	 */
	private String totalAcctNum;
	/**
	 * 日期
	 */
	private String date;
}
