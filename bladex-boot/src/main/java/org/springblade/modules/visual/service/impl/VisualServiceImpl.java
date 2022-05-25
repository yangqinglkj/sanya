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
package org.springblade.modules.visual.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.visual.dto.VisualDTO;
import org.springblade.modules.visual.entity.Visual;
import org.springblade.modules.visual.entity.VisualConfig;
import org.springblade.modules.visual.mapper.VisualMapper;
import org.springblade.modules.visual.service.IVisualConfigService;
import org.springblade.modules.visual.service.IVisualService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 可视化表 服务实现类
 *
 * @author Chill
 */
@Service
@AllArgsConstructor
public class VisualServiceImpl extends BaseServiceImpl<VisualMapper, Visual> implements IVisualService {

	private final IVisualConfigService configService;

	@Override
	public VisualDTO detail(Long id) {
		Visual visual = this.getById(id);
		VisualConfig visualConfig = configService.getOne(Wrappers.<VisualConfig>query().lambda().eq(VisualConfig::getVisualId, id));
		VisualDTO dto = new VisualDTO();
		dto.setVisual(visual);
		dto.setConfig(visualConfig);
		return dto;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveVisual(VisualDTO dto) {
		Visual visual = dto.getVisual();
		VisualConfig visualConfig = dto.getConfig();
		boolean tempV = this.save(visual);
		visualConfig.setVisualId(visual.getId());
		boolean tempVc = configService.save(visualConfig);
		return tempV && tempVc;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateVisual(VisualDTO dto) {
		Visual visual = dto.getVisual();
		VisualConfig visualConfig = dto.getConfig();
		if (visual != null && visual.getId() != null) {
			this.updateById(visual);
		}
		if (visualConfig != null && visualConfig.getId() != null) {
			configService.updateById(visualConfig);
		}
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long copyVisual(Long id) {
		Visual visual = this.getById(id);
		VisualConfig visualConfig = configService.getOne(Wrappers.<VisualConfig>query().lambda().eq(VisualConfig::getVisualId, id));
		if (visual != null && visualConfig != null) {
			visual.setId(null);
			visualConfig.setId(null);
			VisualDTO dto = new VisualDTO();
			dto.setVisual(visual);
			dto.setConfig(visualConfig);
			boolean temp = this.saveVisual(dto);
			if (temp) {
				return visual.getId();
			}
		}
		return null;
	}
}
