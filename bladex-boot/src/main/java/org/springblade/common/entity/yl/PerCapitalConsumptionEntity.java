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
 * 人均消费省份
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("yl_per_capital_consumption")
public class PerCapitalConsumptionEntity implements Serializable {
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
     * 省份
	 */
	private String province;

	/**
	 * 排序
	 */
	private Integer sort;

    /**
     * 创建时间
	 */
	private LocalDate createTime;

}
