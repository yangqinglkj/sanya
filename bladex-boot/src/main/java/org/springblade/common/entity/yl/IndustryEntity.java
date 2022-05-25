package org.springblade.common.entity.yl;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @Author yq
 * @Date 2020/9/17 15:19
 *  行业消费实体
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("yl_industry")
public class IndustryEntity implements Serializable {
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 交易总笔数
	 */
	private Integer count;

	/**
	 * 交易总金额
	 */
	private BigDecimal totalMoney;

	/**
	 * 消费人次
	 */
	private Integer consumerVisits;

	/**
	 * 行业类型 0餐饮 1酒店 2零售 3娱乐
	 */
	private Integer industryType;

	/**
	 * 创建时间
	 */
	private LocalDate createTime;

}
