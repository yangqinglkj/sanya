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
package org.springblade.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springblade.core.log.model.LogUsual;
import org.springblade.modules.system.entity.LogUsualEntity;

/**
 * 服务类
 *
 * @author Chill
 */
public interface ILogUsualEntityService extends IService<LogUsualEntity> {

	/**
	 * 日志列表
	 * @param page 分页对象
	 * @param isSuccess 是否成功
	 * @param url 接口名称
	 * @return 分页列表
	 */
	IPage<LogUsualEntity> getLog(IPage<LogUsualEntity> page, String isSuccess, String url);
}
