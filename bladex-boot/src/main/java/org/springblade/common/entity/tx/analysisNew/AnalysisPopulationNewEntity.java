package org.springblade.common.entity.tx.analysisNew;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author yq
 * @Date 2020/9/17 19:53
 * 人数分析实体
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("tx_analysis_origin_new_copy1")
public class AnalysisPopulationNewEntity implements Serializable {
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 人数
	 */
	private String property;



	/**
	 *
	 */
	private String province;

	/**
	 *
	 */
	private String city;
	/**
	 *
	 */
	private String district;

	private Integer typeCode2;

	/**
	 * 人数值
	 */
	private String number;
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
	/**
	 * 市区编码：0三亚市 1海棠区 2吉阳区 3天涯区 4崖州区
	 */
	private Integer typeCode;

	/**
	 * 类型
	 */
	private String md5;

}
