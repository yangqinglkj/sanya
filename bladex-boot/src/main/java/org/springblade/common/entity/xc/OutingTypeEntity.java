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
 * 游客出游类型实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("xc_outing_type")
public class OutingTypeEntity implements Serializable {
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 国家
	 */
	private String country;

	/**
	 * 省份
	 */
	private String province;

	/**
	 * 城市
	 */
	private String city;

	/**
	 * 周期
	 */
	private String period;

	/**
	 * 日期
	 */
	private LocalDate date;

	/**
	 * 创建时间
	 */
	private LocalDate createTime;


}
