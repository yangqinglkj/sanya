package org.springblade.modules.screen.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.common.entity.TravelCount;
import org.springblade.common.mapper.TravelCountMapper;
import org.springblade.modules.screen.service.CtripService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yangqing
 */
@RestController
@RequestMapping("/ctrip")
@Api(value = "携程", tags = "携程接口")
public class CtripController {

	@Resource
	private CtripService ctripService;

	@Resource
	private TravelCountMapper travelCountMapper;

	@ApiOperation(value = "旅游方式占比")
	@GetMapping("/travelMode")
	@CrossOrigin
	public JSONObject travelMode(@RequestParam("type")Integer type) {
		return ctripService.travelMode(type);
	}

	@ApiOperation(value = "酒店游客出行目的占比")
	@GetMapping("/travelPurpose")
	@CrossOrigin
	public List<Map<String,Object>> travelPurpose() {
		return ctripService.travelPurpose();
	}

	@ApiOperation(value = "停留天数占比")
	@GetMapping("/reserveDay")
	@CrossOrigin
	public List<Map<String,Object>>  reserveDay(String classType) {
		return ctripService.reserveDay(classType);
	}

	@ApiOperation(value = "预定渠道占比")
	@GetMapping("/reserveChannel")
	@CrossOrigin
	public List<Map<String,Object>> reserveChannel() {
		return ctripService.reserveChannel();
	}

	@ApiOperation(value = "预定酒店占比")
	@GetMapping("/reserveHotel")
	@CrossOrigin
	public List<Map<String,Object>> reserveHotel() {
		return ctripService.reserveHotel();
	}

	@ApiOperation(value = "接待游客人数")
	@GetMapping("/travelNumber")
	@CrossOrigin
	public JSONObject travelNumber() throws Exception{
		int year = LocalDateTime.now().getYear();
		List<TravelCount> travelCounts = travelCountMapper.selectTraCount(year);
		List<TravelCount> sortList = travelCounts.stream().sorted(Comparator.comparing(TravelCount::getMonths)).collect(Collectors.toList());
		List<String> listMonth = new ArrayList<>();//月份
		List<Integer> listDomestic = new ArrayList<>();//国内
		List<Integer> listAbroad = new ArrayList<>();//国外

		for (TravelCount travelCount : sortList) {
			//月份格式：2020-03-01
			String months = travelCount.getMonths();
			LocalDateTime localDateTime = strLocaTime(months);
			int mo = localDateTime.getMonthValue();
			listMonth.add(mo+"月");
			String domestic = travelCount.getDomestic();//国内
			String oversea = travelCount.getOversea();//国外
			listDomestic.add(Integer.parseInt(domestic));
			listAbroad.add(Integer.parseInt(oversea));
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("categories",listMonth);

		JSONObject domesticJson = new JSONObject();
		domesticJson.put("name","过夜游客人数");
		domesticJson.put("data",listDomestic);

		JSONObject overseasJson = new JSONObject();
		overseasJson.put("name","一日游游客人数");
		overseasJson.put("data",listAbroad);

		List<JSONObject> list = new ArrayList<>();
		list.add(domesticJson);
		list.add(overseasJson);

		jsonObject.put("series",list);

		return jsonObject;
	}

	//时间字符串（yyyy-MM-dd）转localDateTime
	public LocalDateTime strLocaTime(String timeStr) throws Exception{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date data=sdf.parse(timeStr);
			Instant instant = data.toInstant();
			ZoneId zone = ZoneId.systemDefault();
			LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
			//int monthValue = localDateTime.getMonthValue();
			return localDateTime;
		}


}
