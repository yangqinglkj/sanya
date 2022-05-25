package org.springblade.common.entity.tx.analysis;

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
@TableName("tx_analysis_population")
public class AnalysisPopulationEntity implements Serializable {
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

}
