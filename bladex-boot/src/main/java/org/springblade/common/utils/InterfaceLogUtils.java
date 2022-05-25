package org.springblade.common.utils;

import org.springblade.modules.system.entity.LogUsualEntity;
import org.springblade.modules.system.mapper.LogMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @Author yq
 * @Date 2020/11/19 15:29
 */
@Component
public class InterfaceLogUtils {

	@Resource
	private LogMapper logMapper;

	private static InterfaceLogUtils interfaceLogUtils;

	@PostConstruct
	public void init() {
		interfaceLogUtils = this;
		interfaceLogUtils.logMapper = this.logMapper;
	}

	/**
	 * 记录日志
	 * @param url 接口名称
	 * @param typeCode 区域id
	 * @param isSuccess 是否成功
	 */
	public static void saveLog(String url,String typeCode,String isSuccess){
		LogUsualEntity logUsual = new LogUsualEntity();
		logUsual.setIsSuccess(isSuccess);
		logUsual.setUrl(url);
		logUsual.setTypeCode(typeCode);
		logUsual.setCreateTime(LocalDateTime.now());
		interfaceLogUtils.logMapper.insert(logUsual);
	}
}
