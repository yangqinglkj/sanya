package org.springblade.common.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Index5VO {

	/**
	 * 商圈名称
	 */
	private String zone;

	/**
	 * 游客消费金额(元)
	 */
	private String transAt;

	/**
	 * 游客消费笔数(笔)
	 */
	private Integer transNum;

	/**
	 * 游客消费人次(人)
	 */
	private Integer acctNum;


}
