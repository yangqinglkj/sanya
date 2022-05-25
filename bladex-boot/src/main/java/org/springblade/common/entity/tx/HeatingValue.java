package org.springblade.common.entity.tx;

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
 * 海南省热力值
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("tx_heating_value")
public class HeatingValue implements Serializable {
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 定位数
	 */
	private Integer count;
	/**
	 * 经度
	 */
	private Double lng;

	/**
	 * 纬度
	 */
	private Double lat;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

}
