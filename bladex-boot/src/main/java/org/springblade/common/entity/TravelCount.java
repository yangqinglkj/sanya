package org.springblade.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author yq
 * @Date 2020/9/22 17:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("travel_count")
public class TravelCount implements Serializable {

	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 国内
	 */
	private String domestic;
	/**
	 * 国外
	 */
	private String oversea;
	/**
	 * 月份
	 */
	private String months;

}
