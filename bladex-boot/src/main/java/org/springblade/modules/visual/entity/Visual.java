/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package org.springblade.modules.visual.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

/**
 * 可视化表实体类
 *
 * @author Chill
 */
@Data
@TableName("blade_visual")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Visual对象", description = "可视化表")
public class Visual extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 大屏标题
	 */
	@ApiModelProperty(value = "大屏标题")
	private String title;
	/**
	 * 大屏背景
	 */
	@ApiModelProperty(value = "大屏背景")
	private String backgroundUrl;
	/**
	 * 大屏类型
	 */
	@ApiModelProperty(value = "大屏类型")
	private Integer category;
	/**
	 * 发布密码
	 */
	@ApiModelProperty(value = "发布密码")
	private String password;


}
