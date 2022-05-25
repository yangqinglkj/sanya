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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.modules.visual.dto.VisualMapDTO;
import org.springblade.modules.visual.entity.VisualMap;

/**
 * 可视化地图配置表 服务类
 *
 * @author BladeX
 */
public interface IVisualMapService extends IService<VisualMap> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param visualMap
	 * @return
	 */
	IPage<VisualMapDTO> selectVisualMapPage(IPage<VisualMapDTO> page, VisualMapDTO visualMap);

}
