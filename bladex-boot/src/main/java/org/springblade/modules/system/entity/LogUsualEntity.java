package org.springblade.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author yq
 * @Date 2020/10/27 14:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("blade_log_usual")
public class LogUsualEntity implements Serializable {
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@JsonIgnore
	private Integer id;

	/**
	 * 请求url
	 */
	String url;

	/**
	 * 是否成功
	 */
	String isSuccess;

	/**
	 * 区域id
	 */
	String typeCode;

	/**
	 * 创建时间
	 */
	@JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createTime;
}
