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
 * 年龄分析实体
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("tx_analysis_new")
public class AnalysisNewEntity implements Serializable {
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 属性名
	 */
	private String property;
	/**
	 * 百分比
	 */
	private String percent;
	/**
	 * 类型
	 */
	private String type;

	/**
	 * 市区code
	 */
	private String typeCode;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

}
