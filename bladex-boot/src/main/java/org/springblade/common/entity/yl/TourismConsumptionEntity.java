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
 *  旅游消费实体
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("yl_tourism_consumption")
public class TourismConsumptionEntity implements Serializable {
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
     * 游客省份 0跨市 1跨省
	 */
	private Integer touristType;

	/**
	 * 区域名称
	 */
	private String areaName;

    /**
     * 创建时间
	 */
	private LocalDate createTime;

}
