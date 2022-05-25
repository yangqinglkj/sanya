package org.springblade.common.entity.tx;

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
import java.time.LocalDateTime;

/**
 * @Author yq
 * @Date 2020/9/17 19:53
 * 区划迁徙实体
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("tx_migration")
public class MigrationEntity implements Serializable {
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 总人数
	 */
	private String population;

	/**
	 * 汽车
	 */
	private String car;

	@TableField(exist = false)
	private String carPerce;

	/**
	 * 飞机
	 */
	private String plane;

	@TableField(exist = false)
	private String planePerce;

	/**
	 * 火车
	 */
	private String train;

	@TableField(exist = false)
	private String trainPerce;

	/**
	 * 迁入 in 迁出 out
	 */
	private String type;

	/**
	 * 创建时间
	 */
	private LocalDate createTime;



}
