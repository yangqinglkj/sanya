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
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("tx_area_people_one")
public class AreaPeopleOne implements Serializable {
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@JsonIgnore
	private Integer id;

	/**
	 * 人数
	 */
	private String people;

	/**
	 * 区域id
	 */
	private String areaCode;

	/**
	 * 获取时间
	 */
	private LocalDateTime createTime;
}
