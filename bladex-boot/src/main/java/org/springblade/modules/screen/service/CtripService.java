package org.springblade.modules.screen.service;


import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @author yangqing
 */
public interface CtripService {

	/**
	 * 旅游方式
	 * @param type 1自由行  2跟团
	 * @return 旅游方式
	 */
	JSONObject travelMode(Integer type);


	/**
	 * 旅游目的
	 * @return 旅游目的对象
	 */
	List<Map<String,Object>> travelPurpose();

	/**
	 * 提前预定天数
	 * @return 天数对象
	 */
	List<Map<String,Object>>  reserveDay(String classType);

	/**
	 * 预定渠道
	 * @return 预定渠道对象
	 */
	List<Map<String,Object>> reserveChannel();


	/**
	 * 预定酒店TOP10
	 * @return 预定酒店对象
	 */
	List<Map<String,Object>> reserveHotel();
}
