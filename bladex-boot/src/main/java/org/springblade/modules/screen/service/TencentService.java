package org.springblade.modules.screen.service;

import com.alibaba.fastjson.JSONObject;
import org.springblade.common.entity.tx.AreaStayDayEntity;
import org.springblade.common.entity.tx.MigrationEntity;
import org.springblade.common.entity.tx.analysis.AnalysisOriginEntity;
import org.springblade.common.vo.*;
import org.springblade.common.vo.xc.area.AreaBusinessVO;

import java.util.List;
import java.util.Map;

/**
 * @Author yq
 * @Date 2020/9/17 15:40
 */

public interface TencentService {

	/**
	 * 性别画像
	 * @param type 0男 1女
	 * @param typeCode 0三亚市 1海棠区 2吉阳区 3天涯区 4崖州区
	 * @return vo对象
	 */
	Object getGender(String typeCode,Integer type);

	/**
	 * 持车画像
	 * @param typeCode 0三亚市 1海棠区 2吉阳区 3天涯区 4崖州区
	 * @return vo对象
	 */
	List<AreaVO> getCar(String typeCode);

	/**
	 * 年龄画像
	 * @param typeCode 0三亚市 1海棠区 2吉阳区 3天涯区 4崖州区
	 * @return vo对象
	 */
	List<AreaVO> getAge(String typeCode);

	/**
	 * 学历画像
	 * @param  typeCode 0三亚市 1海棠区 2吉阳区 3天涯区 4崖州区
	 * @return vo对象
	 */
	JSONObject getEducation(String typeCode);

	/**
	 * 消费能力画像
	 * @param typeCode 0三亚市 1海棠区 2吉阳区 3天涯区 4崖州区
	 * @return vo对象
	 */
	List<AreaVO> getConsumer(String typeCode);

	/**
	 * 购物到访画像
	 * @param typeCode 0三亚市 1海棠区 2吉阳区 3天涯区 4崖州区
	 * @return vo对象
	 */
	List<AreaVO> getShopping(String typeCode);

	/**
	 * 理财画像
	 * @param typeCode 0三亚市 1海棠区 2吉阳区 3天涯区 4崖州区
	 * @return vo对象
	 */
	List<AreaVO> getFinance(String typeCode);

	/**
	 * 人生阶段画像
	 * @param typeCode 0三亚市 1海棠区 2吉阳区 3天涯区 4崖州区
	 * @return vo对象
	 */
	List<AreaVO> getLife(String typeCode);

	/**
	 * 停留天数占比
	 * @param typeCode 区域id
	 * @return vo对象
	 */
	JSONObject getAreaStayDay(String typeCode);

	/**
	 * 获取三亚市实时游客数
	 * @return 游客数
	 */
	JSONObject getPopulation();

	/**
	 * 获取区域实时游客数
	 * @return 游客数
	 */
	Map<String,Object> getAreaPopulation();

	/**
	 * 区域游客排行
	 * @return 区域名称
	 */
	List<AreaNameVO> areaRanking();


	/**
	 * 迁途方式分析
	 * @param type 1迁入  2迁出
	 * @param typeId 1航空  2铁路 3公路
	 * @return 对象
	 */
	JSONObject migration(Integer type,Integer typeId);

	/**
	 * 出入境游客分析
	 * @param typeCode 0三亚市 1海棠区 2吉阳区 3天涯区 4崖州区
	 * @param type 1：省内  2省外
	 * @return vo对象
	 */
	JSONObject entryAndExit(Integer typeCode,Integer type);

	/**
	 * 客源地分析
	 * @return list
	 */
	JSONObject sourcePlace();

	/**
	 * 获取区域热力值
	 * @param areaCode 区域id
	 * @return vo对象
	 */
	List<HeatingValueVO> getHeatingValue(String areaCode);

	/**
	 * 旅游距离占比
	 * @param  typeCode 0三亚市 1海棠区 2吉阳区 3天涯区 4崖州区
	 * @param  type 1无旅行 2短途 3中途 4长途
	 * @return vo对象
	 */
	JSONObject getDistance(Integer typeCode,Integer type);


	/**
	 * 游客出访分析  大屏只查三亚市
	 * @return
	 */
	JSONObject tourVisiting();


	/**
	 * 获取景点商圈实时游客数
	 * @param  type 1景点 2商圈
	 * @return 游客数
	 */
	Map<String,Object> getAreaBusiness(String type);
}
