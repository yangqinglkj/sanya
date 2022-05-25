package org.springblade.modules.screen.controller;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.common.vo.*;
import org.springblade.common.vo.xc.area.AreaBusinessVO;
import org.springblade.modules.screen.dto.WeatherDTO;
import org.springblade.modules.screen.service.TencentService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yangqing
 */
@RestController
@RequestMapping("/tencent")
@Api(value = "腾讯", tags = "腾讯接口")

public class TencentController {

	@Resource
	private TencentService tencentService;

	@ApiOperation(value = "性别画像")
	@GetMapping("/getGender")
	@CrossOrigin
	public Object getGender(@RequestParam("typeCode")String typeCode,@RequestParam(value = "type",required = false)Integer type) {
		return tencentService.getGender(typeCode,type);
	}

	@ApiOperation(value = "持车画像")
	@GetMapping("/getCar")
	@CrossOrigin
	public List<AreaVO> getCar(@RequestParam("typeCode")String typeCode) {
		return tencentService.getCar(typeCode);
	}

	@ApiOperation(value = "年龄画像")
	@GetMapping("/getAge")
	@CrossOrigin
	public Object getAge(@RequestParam("typeCode") String typeCode) {
		return tencentService.getAge(typeCode);
	}

	@ApiOperation(value = "学历画像")
	@GetMapping("/getEducation")
	@CrossOrigin
	public JSONObject getEducation(@RequestParam("typeCode")String typeCode) {
		return tencentService.getEducation(typeCode);
	}

	@ApiOperation(value = "消费能力画像")
	@GetMapping("/getConsumer")
	@CrossOrigin
	public List<AreaVO> getConsumer(@RequestParam("typeCode")String typeCode) {
		return tencentService.getConsumer(typeCode);
	}

	@ApiOperation(value = "购物偏好画像")
	@GetMapping("/getShopping")
	@CrossOrigin
	public List<AreaVO> getShopping(@RequestParam("typeCode")String typeCode) {
		return tencentService.getShopping(typeCode);
	}

	@ApiOperation(value = "理财画像")
	@GetMapping("/getFinance")
	@CrossOrigin
	public List<AreaVO> getFinance(@RequestParam("typeCode")String typeCode) {
		return tencentService.getFinance(typeCode);
	}

	@ApiOperation(value = "人生阶段画像")
	@GetMapping("/getLife")
	@CrossOrigin
	public List<AreaVO> getLife(@RequestParam("typeCode")String typeCode) {
		return tencentService.getLife(typeCode);
	}

	@ApiOperation(value = "停留天数分析")
	@GetMapping("/getAreaStayDay")
	@CrossOrigin
	public JSONObject getAreaStayDay(@RequestParam(value = "typeCode",required = false,defaultValue = "0")String typeCode) {
		return tencentService.getAreaStayDay(typeCode);
	}

	@ApiOperation(value = "获取三亚市实时游客数")
	@GetMapping("/getPopulation")
	@CrossOrigin
	public JSONObject getPopulation() {
		return tencentService.getPopulation();
	}

	@ApiOperation(value = "获取区域游客数")
	@GetMapping("/getAreaPopulation")
	@CrossOrigin
	public Map<String,Object> getAreaPopulation() {
		return tencentService.getAreaPopulation();
	}

	@ApiOperation(value = "获取景点商圈游客数")
	@GetMapping("/getAreaBusiness")
	@CrossOrigin
	public Map<String,Object> getAreaBusiness(@RequestParam("type") String type) {
		return tencentService.getAreaBusiness(type);
	}

	@ApiOperation(value = "区域游客排行")
	@GetMapping("/areaRanking")
	@CrossOrigin
	public List<AreaNameVO> areaRanking() {
		return tencentService.areaRanking();
	}

	@ApiOperation(value = "迁途方式百分比")
	@GetMapping("/migration")
	@CrossOrigin
	public JSONObject migration(@RequestParam(value = "type")Integer type,@RequestParam("typeId")Integer typeId) {
		return tencentService.migration(type,typeId);
	}

	@ApiOperation(value = "访客接待分析")
	@GetMapping("/entryAndExit")
	@CrossOrigin
	public JSONObject entryAndExit(@RequestParam("typeCode") Integer typeCode,@RequestParam("type")Integer type) {
		return tencentService.entryAndExit(typeCode,type);
	}

	@ApiOperation(value = "客源地分析")
	@GetMapping("/sourcePlace")
	@CrossOrigin
	public JSONObject sourcePlace() {
		return tencentService.sourcePlace();
	}


	@ApiOperation(value = "获取区域热力值")
	@GetMapping("/getHeatingValue")
	@CrossOrigin
	public List<HeatingValueVO> getHeatingValue(String areaCode) {
		return tencentService.getHeatingValue(areaCode);
	}

	@ApiOperation(value = "旅游距离占比")
	@GetMapping("/getDistance")
	@CrossOrigin
	public JSONObject getDistance(@RequestParam("typeCode") Integer typeCode,@RequestParam("type")Integer type) {
		return tencentService.getDistance(typeCode,type);
	}

	@ApiOperation(value = "天气接口")
	@GetMapping("/weather")
	@CrossOrigin
	public JSONObject weather(@RequestParam("type") Integer type) {
		JSONObject json = new JSONObject();
		WeatherDTO weather = getWeather();
		Map<String, String> map = getHeFengWeather();
		if (weather != null){
			if (type == 1){
				json.put("value", "今天:" + weather.getWeather() + "  " + weather.getTemperature() + "℃" + "  " + weather.getWinddirection() +"风");
			}
			if (type == 2){
				json.put("value", weather.getHumidity()+"%");
			}
			if (type == 3){
				json.put("value", weather.getWindpower());
			}
		}
		if (map != null){
			if (type == 4){
				json.put("value", map.get("precip")+"毫米");
			}
			if (type == 5){
				json.put("value", map.get("pressure")+"kpa");
			}
		}
		return json;
	}

	/**
	 * 高德天气接口
	 * @return 天气对象
	 */
	private WeatherDTO getWeather(){
		String result = HttpUtil.get("https://restapi.amap.com/v3/weather/weatherInfo?key=22203648f6164441c1c42c904826155c&city=460200");
		JSONObject json = JSONObject.parseObject(result);
		String status = json.getString("status");
		String info = json.getString("info");
		if ("1".equals(status) && "OK".equals(info)){
			String lives = json.getString("lives");
			List<WeatherDTO> weatherList = JSONArray.parseArray(lives, WeatherDTO.class);
			if (CollectionUtils.isNotEmpty(weatherList)){
				return weatherList.get(0);
			}
		}
		return null;
	}

	/**
	 * 和风天气接口
	 * @return 天气对象
	 */
	private Map<String,String> getHeFengWeather(){
		Map<String,String> map = new HashMap<>(16);
		String result = HttpUtil.get("https://devapi.qweather.com/v7/weather/now?location=101310201&key=b964002d4aac417486af13e6a982cfcf");
		JSONObject json = JSONObject.parseObject(result);
		if ("200".equals(json.getString("code"))){
			String now = json.getString("now");
			JSONObject jsonObject = JSONObject.parseObject(now);
			//降水量
			String precip = jsonObject.getString("precip");
			//压强
			String pressure = jsonObject.getString("pressure");
			map.put("precip",precip);
			map.put("pressure",pressure);
			return map;
		}
		return null;
	}

	@ApiOperation(value = "游客出访分析")
	@GetMapping("/tourVisiting")
	@CrossOrigin
	public JSONObject tourVisiting() {
		return tencentService.tourVisiting();
	}

}
