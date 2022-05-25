package org.springblade.modules.backstage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.common.vo.xc.TransportationVO;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.modules.backstage.service.XcBackstageService;
import org.springblade.modules.backstage.vo.xc.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author yq
 * @Date 2020/9/24 15:46
 */
@RestController
@RequestMapping("/backstageCtrip")
@Api(value = "携程", tags = "携程后台接口")
public class XcBackstageController {

	@Resource
	private XcBackstageService xcBackstageService;

	@ApiOperation(value = "预定方式百分比（饼状图）")
	@GetMapping("/reserveChannelList")
	public Map<String, Object> reserveChannelList(@RequestParam(value = "day", required = false) String day) {
		return xcBackstageService.reserveChannelList(day);
	}

	@ApiOperation(value = "预定方式列表")
	@GetMapping("/reserveChannelTable")
	public Map<String, Object> reserveChannelTable(Query query, @RequestParam(value = "startMonth", required = false) String startMonth, @RequestParam(value = "endMonth", required = false) String endMonth) {
		return xcBackstageService.reserveChannelTable(Condition.getPage(query), startMonth, endMonth);
	}

	@ApiOperation(value = "提前预定天数百分比（饼状图）")
	@GetMapping("/reserveDayList")
	public Map<String,Object> reserveDayList(@RequestParam(value = "day", required = false) String day, @RequestParam(value = "type", required = false) String type) {
		return xcBackstageService.reserveDayList(day, type);
	}

	@ApiOperation(value = "提前预定天数列表")
	@GetMapping("/reserveDayTable")
	public Map<String,Object> reserveDayTable(@RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
													@RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
													@RequestParam(value = "startMonth", required = false) String startMonth,
													@RequestParam(value = "endMonth", required = false) String endMonth,
													@RequestParam(value = "type", required = false) String type) {
		return xcBackstageService.reserveDayTable(new Page<>(current, size), startMonth, endMonth, type);
	}

	@ApiOperation(value = "酒店游客出游目的百分比(饼状图)")
	@GetMapping("/hotelTourist")
	public Object hotelTourist(@RequestParam(value = "date", required = false) String date) {
		return xcBackstageService.hotelTourist(date);
	}

	@ApiOperation(value = "酒店游客出游目的百分比（表格）")
	@GetMapping("/hotelTouristTable")
	public Object hotelTourist(@RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
							   @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
							   @RequestParam(value = "startTime", required = false) String startTime,
							   @RequestParam(value = "endTime", required = false) String endTime) {
		return xcBackstageService.hotelTouristTable(new Page<>(current, size), startTime, endTime);
	}

	@ApiOperation(value = "热门酒店TOP")
	@GetMapping("/topHotelList")
	public Object topHotelList(@RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
							   @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
							   @RequestParam(value = "startTime", required = false) String startTime,
							   @RequestParam(value = "endTime", required = false) String endTime) {
		return xcBackstageService.topHotelList(new Page<>(current, size), startTime, endTime);
	}

	@ApiOperation(value = "自由行/团队游占比")
	@GetMapping("/wayList")
	public Map<String, Object> wayList(@RequestParam(value = "startTime", required = false) String startTime,
									   @RequestParam(value = "endTime", required = false) String endTime,
									   @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
									   @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
		return xcBackstageService.wayList(startTime, endTime, new Page<>(current, size));
	}

	@ApiOperation(value = "年旅游次数表格")
	@GetMapping("/travelCountTable")
	public Object travelCountTable(@RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
								   @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
								   @RequestParam(value = "months", required = false) String months) {
		return xcBackstageService.travelCountTable(new Page<>(current, size), months);
	}

	@ApiOperation(value = "各交通方式人次分布(饼状图)")
	@GetMapping("/transportationList")
	public Map<String,Object> transportationList(@RequestParam(value = "date", required = false) String date) {
		return xcBackstageService.transportationList(date);
	}

	@ApiOperation(value = "各交通方式人次分布(表格)")
	@GetMapping("/transportationTable")
	public Map<String,Object> transportationTable(@RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
													   @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
													   @RequestParam(value = "startTime", required = false) String startTime,
													   @RequestParam(value = "endTime", required = false) String endTime) {
		return xcBackstageService.transportationTable(new Page<>(current, size), startTime, endTime);
	}


}
