package org.springblade.common.entity.tx;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @Date 2020/9/17 19:53
 * 区划迁徙实体
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("tx_migration_provinces")
public class MigrationProvincesEntity implements Serializable {
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 迁徒id
	 */
	private Integer migrationId;

	/**
	 * 区域编码
	 */
	private String adcode;

	/**
	 * 区域人数
	 */
	private String population;

	/**
	 * 省市类型 city province
	 */
	private String areaType;

	/**
	 * 迁入 in 迁出 out
	 */
	private String type;

	/**
	 * 创建时间
	 */
	private LocalDate createTime;



}
