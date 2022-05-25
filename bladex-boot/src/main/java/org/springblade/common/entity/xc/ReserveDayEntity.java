package org.springblade.common.entity.xc;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @Author yq
 * @Date 2020/9/16 10:56
 * 提前预定天数value实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("xc_reserve_day")
public class ReserveDayEntity implements Serializable {
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 提前预定天数
	 */
	private String data;

	/**
	 * 百分比
	 */
	private String value;

	/**
	 * 分类(1：门票  2：酒店  3：机票  4：汽车票  5：度假  6：其他)
	 */
	private Integer type;

	/**
	 * 创建时间
	 */
	private LocalDate createTime;

}
