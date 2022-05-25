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
package org.springblade.common.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.reactivex.annotations.NonNull;
import org.springblade.common.constant.ServiceConstant;
import org.springblade.common.constant.XcConstant;
import org.springblade.common.entity.mongo.MongoJSON;
import org.springblade.modules.system.entity.LogUsualEntity;
import org.springblade.modules.system.mapper.LogMapper;
import org.springblade.modules.system.mapper.LogUsualMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * 通用工具类
 *
 * @author Chill
 */
@Component
public class CommonUtil {

	private static MongoTemplate mongoTemplate;


	@Autowired
	public CommonUtil(MongoTemplate mongoTemplate) {
		CommonUtil.mongoTemplate = mongoTemplate;
	}

	private static LogMapper logMapper;

	@Resource
	public void setLogMapper(LogMapper logMapper) {
		CommonUtil.logMapper = logMapper;
	}


	public static String post(String postUrl, String postParam) {
		//发送请求
		String result = HttpUtil.post(postUrl, postParam);

//		JSONObject jsonObject = JSON.parseObject(result);
//		String uuid = UUID.randomUUID().toString();
//		MongoJSON mongoJSON = new MongoJSON();
//
//		mongoJSON.setId(uuid);
//		mongoJSON.setJsonObject(jsonObject);
//		mongoJSON.setAddTime(new Date());
//
//		String apiName = postUrl.replace("https://apis.map.qq.com/", "");
//		apiName = apiName.replace("/", "_");
//		apiName = apiName.replace(".", "");
//
//		mongoJSON.setApiName(apiName);
//		mongoJSON.setPostUrl(postUrl);
//		mongoJSON.setPostParam(postParam);
//
//		//保存数据
//		mongoTemplate.insert(mongoJSON, apiName);

//		//腾讯接口
//		if (jsonObject.getString("status") != null) {
//			if ("0".equals(jsonObject.getString("status"))) {
//				//区划接口
//					if (postUrl.equals(ServiceConstant.TX_HEATING_URL) || postUrl.equals(ServiceConstant.TX_ANALYSIS_URL) || postUrl.equals(ServiceConstant.TX_MIGRATION_URL)) {
//						analysisUrlMap.forEach((k,v) ->{
//							if (postUrl.equals(k)){
//								JSONObject json = JSONObject.parseObject(postParam);
//								String adcode = json.getString("adcode");
//								//保存日志
//								LogUsualEntity logUsual = new LogUsualEntity();
//								logUsual.setIsSuccess("成功");
//								logUsual.setUrl(v);
//								logUsual.setTypeCode(adcode);
//								logUsual.setCreateTime(LocalDateTime.now());
//								logMapper.insert(logUsual);
//							}
//						});
//					//区域接口
//					} else if(postUrl.equals(ServiceConstant.TX_ANALYSIS_V2_URL)
//							|| postUrl.equals(ServiceConstant.TX_AREA_PEOPLE_COUNT_URL)
//							|| postUrl.equals(ServiceConstant.TX_AREA_REAL_TIME_PEOPLE_URL)
//					){
//						areaUrlMap.forEach((k,v) ->{
//							if (postUrl.equals(k)){
//								JSONObject json = JSONObject.parseObject(postParam);
//								String id = json.getString("id");
//								//保存日志
//								LogUsualEntity logUsual = new LogUsualEntity();
//								logUsual.setIsSuccess("成功");
//								logUsual.setUrl(v);
//								logUsual.setTypeCode(id);
//								logUsual.setCreateTime(LocalDateTime.now());
//								logMapper.insert(logUsual);
//							}
//						});
//						//1.区域实时人数  分为5分钟和一个小时两个接口
//					}else if(postUrl.equals(ServiceConstant.TX_AREA_PEOPLE_URL)){
//						JSONObject object = JSONObject.parseObject(postParam);
//						String interval = object.getString("interval");
//						if ("5".equals(interval)){
//							JSONObject json = JSONObject.parseObject(postParam);
//							String id = json.getString("id");
//							//保存日志
//							LogUsualEntity logUsual = new LogUsualEntity();
//							logUsual.setIsSuccess("成功");
//							logUsual.setUrl("1.区域实时人数 5分钟");
//							logUsual.setTypeCode(id);
//							logUsual.setCreateTime(LocalDateTime.now());
//							logMapper.insert(logUsual);
//						}
//						if ("60".equals(interval)){
//							JSONObject json = JSONObject.parseObject(postParam);
//							String id = json.getString("id");
//							//保存日志
//							LogUsualEntity logUsual = new LogUsualEntity();
//							logUsual.setIsSuccess("成功");
//							logUsual.setUrl("1.区域实时人数 1小时");
//							logUsual.setTypeCode(id);
//							logUsual.setCreateTime(LocalDateTime.now());
//							logMapper.insert(logUsual);
//						}
//						//区域实时人口热力图  分为5分钟和一个小时两个接口
//					}else if(postUrl.equals(ServiceConstant.TX_AREA_HEATING_VALUE_URL)){
//						JSONObject object = JSONObject.parseObject(postParam);
//						String interval = object.getString("interval");
//						if ("5".equals(interval)){
//							JSONObject json = JSONObject.parseObject(postParam);
//							String id = json.getString("id");
//							//保存日志
//							LogUsualEntity logUsual = new LogUsualEntity();
//							logUsual.setIsSuccess("成功");
//							logUsual.setUrl("区域实时人口热力图 5分钟");
//							logUsual.setTypeCode(id);
//							logUsual.setCreateTime(LocalDateTime.now());
//							logMapper.insert(logUsual);
//						}
//						if ("60".equals(interval)){
//							JSONObject json = JSONObject.parseObject(postParam);
//							String id = json.getString("id");
//							//保存日志
//							LogUsualEntity logUsual = new LogUsualEntity();
//							logUsual.setIsSuccess("成功");
//							logUsual.setUrl("区域实时人口热力图 1小时");
//							logUsual.setTypeCode(id);
//							logUsual.setCreateTime(LocalDateTime.now());
//							logMapper.insert(logUsual);
//						}
//					}
//
//			} else {
//				//区划接口 失败
//				if (postUrl.equals(ServiceConstant.TX_HEATING_URL) || postUrl.equals(ServiceConstant.TX_ANALYSIS_URL) || postUrl.equals(ServiceConstant.TX_MIGRATION_URL)) {
//					analysisUrlMap.forEach((k,v) ->{
//						if (postUrl.equals(k)){
//							JSONObject json = JSONObject.parseObject(postParam);
//							String adcode = json.getString("adcode");
//							//保存日志
//							LogUsualEntity logUsual = new LogUsualEntity();
//							logUsual.setIsSuccess("失败");
//							logUsual.setUrl(v);
//							logUsual.setTypeCode(adcode);
//							logUsual.setCreateTime(LocalDateTime.now());
//							logMapper.insert(logUsual);
//						}
//					});
//					//区域接口 失败
//				} else if(postUrl.equals(ServiceConstant.TX_ANALYSIS_V2_URL)
//					|| postUrl.equals(ServiceConstant.TX_AREA_PEOPLE_COUNT_URL)
//					|| postUrl.equals(ServiceConstant.TX_AREA_REAL_TIME_PEOPLE_URL)
//				){
//					areaUrlMap.forEach((k,v) ->{
//						if (postUrl.equals(k)){
//							JSONObject json = JSONObject.parseObject(postParam);
//							String id = json.getString("id");
//							//保存日志
//							LogUsualEntity logUsual = new LogUsualEntity();
//							logUsual.setIsSuccess("失败");
//							logUsual.setUrl(v);
//							logUsual.setTypeCode(id);
//							logUsual.setCreateTime(LocalDateTime.now());
//							logMapper.insert(logUsual);
//						}
//					});
//				}
//				//1.区域实时人数  分为5分钟和一个小时两个接口
//				else if(postUrl.equals(ServiceConstant.TX_AREA_PEOPLE_URL)){
//					JSONObject object = JSONObject.parseObject(postParam);
//					String interval = object.getString("interval");
//					if ("5".equals(interval)){
//						JSONObject json = JSONObject.parseObject(postParam);
//						String id = json.getString("id");
//						//保存日志
//						LogUsualEntity logUsual = new LogUsualEntity();
//						logUsual.setIsSuccess("失败");
//						logUsual.setUrl("1.区域实时人数 5分钟");
//						logUsual.setTypeCode(id);
//						logUsual.setCreateTime(LocalDateTime.now());
//						logMapper.insert(logUsual);
//					}
//					if ("60".equals(interval)){
//						JSONObject json = JSONObject.parseObject(postParam);
//						String id = json.getString("id");
//						//保存日志
//						LogUsualEntity logUsual = new LogUsualEntity();
//						logUsual.setIsSuccess("失败");
//						logUsual.setUrl("1.区域实时人数 1小时");
//						logUsual.setTypeCode(id);
//						logUsual.setCreateTime(LocalDateTime.now());
//						logMapper.insert(logUsual);
//					}
//					//区域实时人口热力图  分为5分钟和一个小时两个接口
//				}else if(postUrl.equals(ServiceConstant.TX_AREA_HEATING_VALUE_URL)){
//					JSONObject object = JSONObject.parseObject(postParam);
//					String interval = object.getString("interval");
//					if ("5".equals(interval)){
//						JSONObject json = JSONObject.parseObject(postParam);
//						String id = json.getString("id");
//						//保存日志
//						LogUsualEntity logUsual = new LogUsualEntity();
//						logUsual.setIsSuccess("失败");
//						logUsual.setUrl("区域实时人口热力图 5分钟");
//						logUsual.setTypeCode(id);
//						logUsual.setCreateTime(LocalDateTime.now());
//						logMapper.insert(logUsual);
//					}
//					if ("60".equals(interval)){
//						JSONObject json = JSONObject.parseObject(postParam);
//						String id = json.getString("id");
//						//保存日志
//						LogUsualEntity logUsual = new LogUsualEntity();
//						logUsual.setIsSuccess("失败");
//						logUsual.setUrl("区域实时人口热力图 1小时");
//						logUsual.setTypeCode(id);
//						logUsual.setCreateTime(LocalDateTime.now());
//						logMapper.insert(logUsual);
//					}
//				}
//			}
//		}
//
//		//携程接口
//		String responseStatus = jsonObject.getString("ResponseStatus");
//		if (!StringUtils.isEmpty(responseStatus)){
//			JSONObject json = JSON.parseObject(responseStatus);
//			if ("Success".equals(json.getString("Ack"))){
//				JSONArray objects = json.getJSONArray("value");
//				if (objects.size() > 0){
//					xcMap.forEach((k,v) -> {
//						if (postUrl.equals(k)){
//							//保存日志
//							LogUsualEntity logUsual = new LogUsualEntity();
//							logUsual.setIsSuccess("成功");
//							logUsual.setUrl(v);
//							logUsual.setTypeCode(null);
//							logUsual.setCreateTime(LocalDateTime.now());
//							logMapper.insert(logUsual);
//						}
//					});
//				}else {
//					xcMap.forEach((k,v) -> {
//						if (postUrl.equals(k)){
//							//保存日志
//							LogUsualEntity logUsual = new LogUsualEntity();
//							logUsual.setIsSuccess("失败");
//							logUsual.setUrl(v);
//							logUsual.setTypeCode(null);
//							logUsual.setCreateTime(LocalDateTime.now());
//							logMapper.insert(logUsual);
//						}
//					});
//				}
//			}else {
//				xcMap.forEach((k,v) -> {
//					if (postUrl.equals(k)){
//						//保存日志
//						LogUsualEntity logUsual = new LogUsualEntity();
//						logUsual.setIsSuccess("失败");
//						logUsual.setUrl(v);
//						logUsual.setTypeCode(null);
//						logUsual.setCreateTime(LocalDateTime.now());
//						logMapper.insert(logUsual);
//					}
//				});
//			}
//		}
		return result;
	}


	public static String getMD5(Object info) {
		return getMD5(JSON.toJSONString(info));
	}
	/**
	 * 获取String的MD5值
	 *
	 * @param info 字符串
	 * @return 该字符串的MD5值
	 */
	public static String getMD5(String info) {
		try {
			//获取 MessageDigest 对象，参数为 MD5 字符串，表示这是一个 MD5 算法（其他还有 SHA1 算法等）：
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			//update(byte[])方法，输入原数据
			//类似StringBuilder对象的append()方法，追加模式，属于一个累计更改的过程
			md5.update(info.getBytes("UTF-8"));
			//digest()被调用后,MessageDigest对象就被重置，即不能连续再次调用该方法计算原数据的MD5值。可以手动调用reset()方法重置输入源。
			//digest()返回值16位长度的哈希值，由byte[]承接
			byte[] md5Array = md5.digest();
			//byte[]通常我们会转化为十六进制的32位长度的字符串来使用,本文会介绍三种常用的转换方法
			return bytesToHex1(md5Array);
		} catch (NoSuchAlgorithmException e) {
			return "";
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	@NonNull
	private static String bytesToHex1(byte[] md5Array) {
		StringBuilder strBuilder = new StringBuilder();
		for (int i = 0; i < md5Array.length; i++) {
			int temp = 0xff & md5Array[i];//TODO:此处为什么添加 0xff & ？
			String hexString = Integer.toHexString(temp);
			if (hexString.length() == 1) {//如果是十六进制的0f，默认只显示f，此时要补上0
				strBuilder.append("0").append(hexString);
			} else {
				strBuilder.append(hexString);
			}
		}
		return strBuilder.toString();
	}

	//通过java提供的BigInteger 完成byte->HexString
	private static String bytesToHex2(byte[] md5Array) {
		BigInteger bigInt = new BigInteger(1, md5Array);
		return bigInt.toString(16);
	}

	//通过位运算 将字节数组到十六进制的转换
	private static String bytesToHex3(byte[] byteArray) {
		char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
		char[] resultCharArray = new char[byteArray.length * 2];
		int index = 0;
		for (byte b : byteArray) {
			resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
			resultCharArray[index++] = hexDigits[b & 0xf];
		}
		return new String(resultCharArray);
	}
}
