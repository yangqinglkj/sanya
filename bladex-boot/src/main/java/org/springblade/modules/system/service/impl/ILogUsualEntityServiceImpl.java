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
package org.springblade.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.modules.system.entity.LogUsualEntity;
import org.springblade.modules.system.mapper.LogMapper;
import org.springblade.modules.system.mapper.LogUsualEntityMapper;
import org.springblade.modules.system.service.ILogUsualEntityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * 服务类
 *
 * @author Chill
 */
@Service
public class ILogUsualEntityServiceImpl extends ServiceImpl<LogMapper, LogUsualEntity> implements ILogUsualEntityService {

	@Resource
	private LogUsualEntityMapper logUsualEntityMapper;
	@Override
	public IPage<LogUsualEntity> getLog(IPage<LogUsualEntity> page, String isSuccess, String url) {
		return logUsualEntityMapper.getLog(page,isSuccess,url);
	}
}
