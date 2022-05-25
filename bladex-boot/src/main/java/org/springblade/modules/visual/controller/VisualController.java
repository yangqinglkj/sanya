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
import lombok.SneakyThrows;
import org.springblade.common.constant.LauncherConstant;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.oss.OssTemplate;
import org.springblade.core.oss.model.BladeFile;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.support.Kv;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.visual.dto.VisualDTO;
import org.springblade.modules.visual.entity.Visual;
import org.springblade.modules.visual.entity.VisualCategory;
import org.springblade.modules.visual.service.IVisualCategoryService;
import org.springblade.modules.visual.service.IVisualService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * 可视化表 控制器
 *
 * @author Chill
 */
@RestController
@AllArgsConstructor
@RequestMapping("/visual")
@Api(value = "可视化表", tags = "可视化数据接口")
public class VisualController extends BladeController {

	private final IVisualService visualService;
	private final IVisualCategoryService categoryService;
	private final OssTemplate ossTemplate;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入visual")
	public R<VisualDTO> detail(@RequestParam Long id) {
		VisualDTO detail = visualService.detail(id);
		return R.data(detail);
	}

	/**
	 * 分页 可视化表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入visual")
	public R<IPage<Visual>> list(Visual visual, Query query) {
		IPage<Visual> pages = visualService.page(Condition.getPage(query), Condition.getQueryWrapper(visual));
		return R.data(pages);
	}

	/**
	 * 新增 可视化表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "新增", notes = "传入visual")
	public R save(@Valid @RequestBody VisualDTO visual) {
		boolean temp = visualService.saveVisual(visual);
		if (temp) {
			Long id = visual.getVisual().getId();
			return R.data(Kv.create().set("id", String.valueOf(id)));
		} else {
			return R.status(false);
		}
	}

	/**
	 * 修改 可视化表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "修改", notes = "传入visual")
	public R update(@Valid @RequestBody VisualDTO visual) {
		return R.status(visualService.updateVisual(visual));
	}


	/**
	 * 删除 可视化表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(visualService.deleteLogic(Func.toLongList(ids)));
	}

	/**
	 * 复制 可视化表
	 */
	@PostMapping("/copy")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "复制", notes = "传入id")
	public R<Long> copy(@ApiParam(value = "主键集合", required = true) @RequestParam Long id) {
		return R.data(visualService.copyVisual(id));
	}

	/**
	 * 获取分类
	 */
	@GetMapping("category")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "获取类型")
	public R category() {
		List<VisualCategory> list = categoryService.list();
		return R.data(list);
	}

	/**
	 * 上传文件
	 */
	@SneakyThrows
	@PostMapping("/put-file")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "上传", notes = "传入文件")
	public R<BladeFile> putFile(@ApiParam(value = "上传文件", required = true) @RequestParam MultipartFile file) {
		BladeFile bladeFile = ossTemplate.putFile(file);
		return R.data(bladeFile);
	}

}
