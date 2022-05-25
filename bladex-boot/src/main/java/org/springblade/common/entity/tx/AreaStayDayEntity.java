package org.springblade.common.entity.tx;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author yq
 * @Date 2020/9/17 19:53
 * 区划停留天数实体
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("tx_area_stay_day")
public class AreaStayDayEntity implements Serializable {
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@JsonIgnore
	private Integer id;

	/**
	 * 区划id
	 */
	private String typeCode;

	/**
	 * 1天
	 */
	private String value1;
	/**
	 * 2天
	 */
	private String value2;
	/**
	 * 3天
	 */
	private String value3;

	/**
	 * 4天
	 */
	private String value4;
	/**
	 * 5天
	 */
	private String value5;
	/**
	 * 6天
	 */
	private String value6;
	/**
	 * 7天
	 */
	private String value7;
	/**
	 * 8天
	 */
	private String value8;
	/**
	 * 9天
	 */
	private String value9;
	/**
	 * 10天
	 */
	private String value10;

	/**
	 * 10天以上
	 */
	private String value11;

	/**
	 * 创建时间
	 */
	private LocalDate createTime;


}
