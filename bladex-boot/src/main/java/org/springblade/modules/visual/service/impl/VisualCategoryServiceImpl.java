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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.visual.entity.VisualCategory;
import org.springblade.modules.visual.mapper.VisualCategoryMapper;
import org.springblade.modules.visual.service.IVisualCategoryService;
import org.springframework.stereotype.Service;

/**
 * 可视化分类表 服务实现类
 *
 * @author BladeX
 */
@Service
public class VisualCategoryServiceImpl extends ServiceImpl<VisualCategoryMapper, VisualCategory> implements IVisualCategoryService {

	@Override
	public boolean submit(VisualCategory visualCategory) {
		LambdaQueryWrapper<VisualCategory> lqw = Wrappers.<VisualCategory>query().lambda().eq(VisualCategory::getCategoryKey, visualCategory.getCategoryKey());
		Integer cnt = baseMapper.selectCount((Func.isEmpty(visualCategory.getId())) ? lqw : lqw.notIn(VisualCategory::getId, visualCategory.getId()));
		if (cnt > 0) {
			throw new ServiceException("当前分类键值已存在!");
		}
		visualCategory.setIsDeleted(BladeConstant.DB_NOT_DELETED);
		return saveOrUpdate(visualCategory);
	}

}
