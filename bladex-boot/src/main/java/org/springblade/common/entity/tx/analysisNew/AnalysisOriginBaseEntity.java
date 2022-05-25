package org.springblade.common.entity.tx.analysisNew;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author yq
 * @Date 2020/9/17 19:53
 * 全国市级别来源地实体
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("tx_area_origin_business_dadonghai")
public class AnalysisOriginBaseEntity implements Serializable {
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@JsonIgnore
	private Integer id;

	/**
	 * 省份
	 */
	private String province;
	/**
	 * 城市
	 */
	private String city;

	/**
	 * 区域编号
	 */
	private String property;

	/**
	 * 百分比
	 */
	private Double percent;

	/**
	 * 区域id
	 */
	private String typeCode;

	/**
	 * 创建时间
	 */
	@JsonIgnore
	private LocalDateTime createTime;

}
