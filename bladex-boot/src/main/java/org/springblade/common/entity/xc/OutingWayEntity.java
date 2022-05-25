package org.springblade.common.entity.xc;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @Author yq
 * @Date 2020/9/16 10:56
 * 游客出游类型实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("xc_outing_way")
public class OutingWayEntity implements Serializable {
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@JsonIgnore
	private Integer id;

	/**
	 * 方式名称
	 */
	private String data;

	/**
	 * 方式百分比
	 */
	private String value;

	/**
	 * 创建时间
	 */
	private LocalDate createTime;




}
