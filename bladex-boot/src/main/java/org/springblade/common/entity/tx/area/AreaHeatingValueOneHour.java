package org.springblade.common.entity.tx.area;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @author yangqing
 * 区域热力值表 一个小时存一次数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("tx_area_heating_value_one_hour")
public class AreaHeatingValueOneHour implements Serializable {
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@JsonIgnore
	private Integer id;

	/**
     * 定位数
	 */
	private String weight;

	/**
	 * 经度
	 */
	private Double lng;

	/**
	 * 纬度
	 */
	private Double lat;

	/**
	 * 区域id
	 */
	private String areaCode;

	/**
	 * 获取时间
	 */
	private LocalDateTime createTime;
}
