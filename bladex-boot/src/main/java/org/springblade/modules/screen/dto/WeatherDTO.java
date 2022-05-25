package org.springblade.modules.screen.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author yq
 * @Date 2020/10/29 18:42
 */
@Data
public class WeatherDTO implements Serializable {
	/**
	 * 省份
	 */
	private String province;
	/**
	 * 城市
	 */
	private String city;
	/**
	 * 城市编码
	 */
	private String adcode;
	/**
	 * 天气
	 */
	private String weather;
	/**
	 * 温度
	 */
	private String temperature;
	/**
	 * 风向
	 */
	private String winddirection;
	/**
	 * 风力
	 */
	private String windpower;
	/**
	 * 湿度
	 */
	private String humidity;
	/**
	 * 获取时间
	 */
	private String reporttime;



}
