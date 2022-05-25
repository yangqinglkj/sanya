package org.springblade.common.entity.yl;

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
 * @Date 2020/9/17 15:19
 *  游客来源省份实体
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("yl_tourism_source")
public class TourismSourceEntity implements Serializable {
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 省份
	 */
	private String province;

	/**
	 * 百分比
	 */
	private String percentage;

	/**
	 * 消费人次
	 */
	private Integer consumerVisits;

	/**
     * 排序
	 */
	private Integer sort;

    /**
     * 创建时间
	 */
	private LocalDate createTime;

}
