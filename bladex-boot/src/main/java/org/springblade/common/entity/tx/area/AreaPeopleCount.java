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
import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * @author yangqing
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("tx_area_people_count")
public class AreaPeopleCount implements Serializable {
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@JsonIgnore
	private Integer id;

	/**
	 * 进入人数
	 */
	private Integer inPeople;

	/**
	 * 进入人数
	 */
	private Integer outPeople;

	/**
	 * 区域id
	 */
	private String areaCode;

	/**
	 * 获取时间
	 */
	private LocalDate createTime;
}
