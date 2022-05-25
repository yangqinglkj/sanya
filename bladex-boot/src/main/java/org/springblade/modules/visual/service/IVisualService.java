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
package org.springblade.modules.visual.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.visual.dto.VisualDTO;
import org.springblade.modules.visual.entity.Visual;

/**
 * 可视化表 服务类
 *
 * @author Chill
 */
public interface IVisualService extends BaseService<Visual> {

	/**
	 * 获取 可视化信息
	 *
	 * @param id 主键
	 * @return VisualDTO
	 */
	VisualDTO detail(Long id);

	/**
	 * 保存可视化信息
	 *
	 * @param dto 配置信息
	 * @return boolean
	 */
	boolean saveVisual(VisualDTO dto);

	/**
	 * 修改可视化信息
	 *
	 * @param dto 配置信息
	 * @return boolean
	 */
	boolean updateVisual(VisualDTO dto);

	/**
	 * 复制可视化信息
	 *
	 * @param id 主键
	 * @return 复制后主键
	 */
	Long copyVisual(Long id);

}
