package org.springblade.modules.backstage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.modules.backstage.service.TxBackstageTencentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author yq
 * @Date 2020/9/24 15:46
 */
@RestController
@RequestMapping("/backstageTencent")
@Api(value = "腾讯", tags = "后台腾讯接口")
public class TxBackstageController {

	@Resource
	private TxBackstageTencentService backstageTencentService;

	@ApiOperation(value = "画像分析")
	@GetMapping("/touristImage")
	@CrossOrigin
	public Map<String, Object> touristImage(@RequestParam("typeCode") String typeCode, @RequestParam(value = "startTime", required = false) String startTime, @RequestParam(value = "endTime", required = false) String endTime) {
		return backstageTencentService.touristImage(typeCode,startTime,endTime);
	}


	@ApiOperation(value = "游客量查询")
	@GetMapping("/touristVolume")
	@CrossOrigin
	public Map<String,Object> touristVolume(@RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
											@RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
											@RequestParam(value = "typeCode", required = false, defaultValue = "1") String typeCode,
											@RequestParam(value = "startTime",required = false) String startTime,
											@RequestParam(value = "endTime",required = false) String endTime) {
		return backstageTencentService.touristVolume(new Page<>(current,size),typeCode,startTime,endTime);
	}


	@ApiOperation(value = "旅游距离分析")
	@GetMapping("/travelDistance")
	@CrossOrigin
	public Map<String,Object> travelDistance(@RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
												@RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
												@RequestParam(value = "typeCode", required = false, defaultValue = "1") String typeCode,
												@RequestParam(value = "startTime",required = false) String startTime,
												@RequestParam(value = "endTime",required = false) String endTime
								) {
		return backstageTencentService.travelDistance(new Page<>(current,size),typeCode,startTime,endTime);
	}


	@ApiOperation(value = "游客接待分析")
	@GetMapping("/touristReception")
	@CrossOrigin
	public Map<String, Object> touristReception(@RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
													@RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
													@RequestParam(value = "startTime",required = false) String startTime,
													@RequestParam(value = "endTime",required = false) String endTime,
													@RequestParam(value = "typeCode") String typeCode) {
		return backstageTencentService.touristReception(new Page<>(current,size),startTime,endTime,typeCode);
	}


	@ApiOperation(value = "游客出访分析 只查三亚市的数据")
	@GetMapping("/touristVisiting")
	@CrossOrigin
	public Map<String,Object> touristVisiting(@RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
											  @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
											  @RequestParam(value = "typeCode", required = false, defaultValue = "1") String typeCode,
											  @RequestParam(value = "startTime",required = false) String startTime,
											  @RequestParam(value = "endTime",required = false) String endTime) {
		return backstageTencentService.touristVisiting(new Page<>(current,size),typeCode,startTime,endTime);
	}


	@ApiOperation(value = "游客停留分析")
	@GetMapping("/touristStop")
	@CrossOrigin
	public Map<String,Object> touristStop(@RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
										  @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
										  @RequestParam(value = "typeCode",required = false) String typeCode,
										  @RequestParam(value = "startTime",required = false) String startTime,
										  @RequestParam(value = "endTime",required = false) String endTime) {

		return backstageTencentService.touristStop(new Page<>(current,size),typeCode,startTime,endTime);
	}


	@ApiOperation(value = "迁入分析")
	@GetMapping("/getMigration")
	@CrossOrigin
	public Map<String,Object> getMigration(@RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
										   @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
										   @RequestParam(value = "startTime",required = false) String startTime,
										   @RequestParam(value = "endTime",required = false) String endTime) {
		return backstageTencentService.getMigration(new Page<>(current,size),startTime,endTime);
	}

	@ApiOperation(value = "客源地省份百分比排名")
	@GetMapping("/sourcePlace")
	public Map<String,Object> sourcePlace(@RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
										  @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
										  @RequestParam(value = "typeCode",required = false) String typeCode,
										  @RequestParam(value = "startTime",required = false) String startTime,
										  @RequestParam(value = "endTime",required = false) String endTime) {

		return backstageTencentService.sourcePlace(new Page<>(current,size),typeCode,startTime,endTime);
	}

	@ApiOperation(value = "客源地省份百分比排名")
	@GetMapping("/zoneSourcePlace")
	public Map<String,Object> zoneSourcePlace(@RequestParam(value = "startTime",required = false) String startTime,
												   @RequestParam(value = "endTime",required = false) String endTime) {
		return backstageTencentService.zoneSourcePlace(startTime,endTime);
	}

	@ApiOperation(value = "获取水印图片")
	@GetMapping("/getImage")
	public void getImage(HttpServletResponse response, String username,
						 @RequestParam(value = "width", required = false, defaultValue = "0") Integer width,
						 @RequestParam(value = "height", required = false, defaultValue = "0") Integer height,
						 @RequestParam(value = "color", required = false, defaultValue = "000000") String color,
						 @RequestParam(value = "alpha", required = false, defaultValue = "0.8f") String alpha) {
		backstageTencentService.getImage(response, username,width,height,color,alpha);
	}

}
