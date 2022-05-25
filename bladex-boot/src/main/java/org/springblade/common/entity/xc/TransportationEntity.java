package org.springblade.common.entity.xc;

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
 * @Date 2020/9/16 10:56
 * 交通方式人员分布value实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("xc_transportation")
public class TransportationEntity implements Serializable {
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 名称
	 */
	private String data;

	/**
	 * 百分比
	 */
	private String value;

	/**
	 * 创建时间
	 */
	private LocalDate createTime;

}
