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
package org.springblade.modules.visual.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.common.constant.LauncherConstant;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.jackson.JsonUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.visual.dto.VisualMapDTO;
import org.springblade.modules.visual.entity.VisualMap;
import org.springblade.modules.visual.service.IVisualMapService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 可视化地图配置表 控制器
 *
 * @author BladeX
 */
@RestController
@AllArgsConstructor
@RequestMapping( "/map")
@Api(value = "可视化地图配置表", tags = "可视化地图配置接口")
public class VisualMapController extends BladeController {

	private final IVisualMapService visualMapService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入visualMap")
	public R<VisualMap> detail(VisualMap visualMap) {
		VisualMap detail = visualMapService.getOne(Condition.getQueryWrapper(visualMap));
		return R.data(detail);
	}

	/**
	 * 数据详情
	 */
	@GetMapping("/data")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "数据详情", notes = "传入id")
	public Map<String, Object> data(Long id) {
		VisualMap detail = visualMapService.getById(id);
		return JsonUtil.toMap(detail.getData());
	}

	/**
	 * 自定义分页 可视化地图配置表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入visualMap")
	public R<IPage<VisualMapDTO>> list(VisualMapDTO visualMap, Query query) {
		IPage<VisualMapDTO> pages = visualMapService.selectVisualMapPage(Condition.getPage(query), visualMap);
		return R.data(pages);
	}

	/**
	 * 新增 可视化地图配置表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入visualMap")
	public R save(@Valid @RequestBody VisualMap visualMap) {
		return R.status(visualMapService.save(visualMap));
	}

	/**
	 * 修改 可视化地图配置表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入visualMap")
	public R update(@Valid @RequestBody VisualMap visualMap) {
		return R.status(visualMapService.updateById(visualMap));
	}


	/**
	 * 删除 可视化地图配置表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(visualMapService.removeByIds(Func.toLongList(ids)));
	}


}
