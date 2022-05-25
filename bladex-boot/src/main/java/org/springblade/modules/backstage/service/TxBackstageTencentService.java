package org.springblade.modules.backstage.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springblade.common.entity.TouristReception;
import org.springblade.common.entity.TravelDistance;
import org.springblade.common.entity.tx.AreaStayDayEntity;
import org.springblade.common.entity.tx.MigrationEntity;
import org.springblade.common.entity.tx.analysis.AnalysisEntity;
import org.springblade.common.entity.tx.analysis.AnalysisPopulationEntity;
import org.springblade.common.vo.*;
import org.springblade.core.mp.support.Query;
import org.springblade.modules.backstage.vo.tx.SourcePlaceVO;
import org.springblade.modules.backstage.vo.tx.ZoneSourcePlaceVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Author yq
 * @Date 2020/9/17 15:40
 */

public interface TxBackstageTencentService {

	/**
	 * 画像分析
	 *
	 * @param typeCode  0三亚市 1海棠区 2吉阳区 3天涯区 4崖州区
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return vo对象
	 */
	Map<String, Object> touristImage(String typeCode, String startTime, String endTime);

	/**
	 * 游客量查询
	 *
	 * @param page      分页对象
	 * @param typeCode  0三亚市 1海棠区 2吉阳区 3天涯区 4崖州区
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return 列表
	 */
	Map<String, Object> touristVolume(IPage<AnalysisEntity> page, String typeCode, String startTime, String endTime);

	/**
	 * 旅游距离
	 *
	 * @param page      分页对象
	 * @param typeCode  区域id
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return 列表
	 */
	Map<String,Object> travelDistance(IPage<TravelDistance> page, String typeCode, String startTime, String endTime);

	/**
	 * 游客接待分析
	 * @param page 分页对象
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param typeCode 区域id
	 * @return 列表
	 */
	Map<String,Object> touristReception(IPage<TouristReception> page, String startTime, String endTime, String typeCode);


	/**
	 * 游客出访分析 折线图
	 * 只查三亚市的数据
	 *
	 * @param page      分页对象
	 * @param typeCode  区域id
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return 列表
	 */
	Map<String, Object> touristVisiting(IPage<AnalysisPopulationEntity> page, String typeCode, String startTime, String endTime);

	/**
	 * 游客停留分析 (只查三亚市的数据)
	 *
	 * @param page      分页对象
	 * @param typeCode   区划id
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return map对象
	 */
	Map<String, Object> touristStop(IPage<AreaStayDayEntity> page, String typeCode, String startTime, String endTime);

	/**
	 * 客源地省份百分比排名
	 * @param page      分页对象
	 * @param typeCode   区划id
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return 列表
	 */
	Map<String,Object> sourcePlace(IPage<SourcePlaceVO> page,String typeCode,String startTime, String endTime);

	/**
	 * 区划客源地
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return 列表
	 */
	Map<String,Object> zoneSourcePlace(String startTime, String endTime);

	/**
	 * 获取迁徒方式人数百分比
	 *
	 * @param page      分页对象
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return 列表
	 */
	Map<String,Object> getMigration(IPage<MigrationEntity> page, String startTime, String endTime);

	/**
	 * 获取水印图片
	 *
	 * @param response 返回对象
	 * @param username 用户名
	 */
	void getImage(HttpServletResponse response, String username, Integer width, Integer height, String color, String alpha);
}
