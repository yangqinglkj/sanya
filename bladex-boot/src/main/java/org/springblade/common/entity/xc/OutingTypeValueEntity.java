package org.springblade.common.entity.xc;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author yq
 * @Date 2020/9/16 10:56
 * 游客出游类型value实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("xc_outing_type_value")
public class OutingTypeValueEntity implements Serializable {
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@JsonIgnore
	private Integer id;

	/**
	 * 出游类型id
	 */
	@JsonIgnore
	private Integer outingTypeId;

	/**
	 * 类型名称
	 */
	private String dimValue;

	/**
	 * 百分比
	 */
	private String ratePercentage;




}
