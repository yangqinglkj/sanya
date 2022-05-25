package org.springblade.modules.screen.mapper;

import org.apache.ibatis.annotations.Param;
import org.springblade.common.entity.tx.MigrationEntity;
import org.springblade.common.entity.tx.analysis.AnalysisOriginEntity;
import org.springblade.common.vo.AnalysisBackVO;
import org.springblade.common.vo.AnalysisVO;
import org.springblade.common.vo.AreaPopulationVO;
import org.springblade.common.vo.AreaVO;
import org.springblade.common.vo.xc.area.AreaBusinessVO;

import java.util.List;

public interface TencentMapper {
	/**
	 * 区划画像
	 *
	 * @param property 画像类型
	 * @param typeCode 区划id（0三亚市 1海棠区 2吉阳区 3天涯区 4崖州区）
	 * @return 画像列表
	 */
	List<AreaVO> getZoneImage(@Param("property") String property, @Param("typeCode") String typeCode);

	/**
	 * 区域画像
	 *
	 * @param property 画像类型
	 * @param typeCode 区域id
	 * @return 画像列表
	 */
	List<AreaVO> getAreaImage(@Param("property") String property, @Param("typeCode") String typeCode);

	/**
	 * 区划总人数
	 *
	 * @param typeCode 区划id（0三亚市 1海棠区 2吉阳区 3天涯区 4崖州区）
	 * @return 区划总人数
	 */
	Integer getZonePeople(@Param("typeCode") String typeCode);

	/**
	 * 区域总人数
	 *
	 * @param typeCode 区域code
	 * @return 区域总人数
	 */
	Integer getAreaPeople(@Param("typeCode") String typeCode);


	/**
	 * 景区排名
	 *
	 * @return 景区名
	 */
	List<String> areaRanking();



	/**
	 * 出入境游客分析
	 *
	 * @param typeCode 0三亚市 1海棠区 2吉阳区 3天涯区 4崖州区
	 * @return vo对象
	 */
	String entryAndExit(@Param("typeCode") Integer typeCode);


	/**
	 * 迁途方式分析
	 * @param type in 迁入  out 迁出
	 * @return 对象
	 */
	MigrationEntity migration(String type);

	/**
	 * 旅游距离占比
	 *
	 * @param typeCode 0三亚市 1海棠区 2吉阳区 3天涯区 4崖州区
	 * @return vo对象
	 */
	List<AnalysisVO> getDistance(@Param("typeCode") Integer typeCode);


	/**
	 * 获取三亚市实时游客数
	 *
	 * @return 游客数
	 */
	String getPopulation();

	/**
	 * 获取区域实时游客数
	 *
	 * @return 游客数
	 */
	List<AreaPopulationVO> getAreaPopulation();

	/**
	 * 获取景点商圈实时游客数
	 *
	 * @return 游客数
	 */
	List<AreaBusinessVO> getAreaBusiness();

	/**
	 * 客源地分析
	 *
	 * @return list
	 */
	List<AnalysisOriginEntity> sourcePlace();
}
