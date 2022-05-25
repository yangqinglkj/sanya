package org.springblade.modules.backstage.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springblade.common.vo.AnalysisVO;
import org.springblade.common.vo.AreaVO;
import org.springblade.common.vo.ZoneVO;
import org.springblade.modules.backstage.vo.TimeVO;
import org.springblade.modules.backstage.vo.tx.PeopleVo;
import org.springblade.modules.backstage.vo.tx.SourcePlaceVO;
import org.springblade.modules.backstage.vo.tx.ZoneSourcePlaceVO;

import java.util.List;

/**
 * @Author yq
 * @Date 2020/9/24 15:53
 */

public interface TxBackstageTencentMapper {
	/**
	 * 区划画像
	 *
	 * @param property  画像类型
	 * @param typeCode  0三亚市 1海棠区 2吉阳区 3天涯区 4崖州区
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return 性别百分比
	 */
	List<ZoneVO> zoneImage(@Param("property")String property, @Param("typeCode") Integer typeCode, @Param("startTime") String startTime, @Param("endTime") String endTime);



	/**
	 * 客源地省份百分比排名
	 *
	 * @return 列表
	 */
	List<SourcePlaceVO> sourcePlace();

	/**
	 * 区域客源地
	 * @param page 分页对象
	 * @param tableName 表名
	 * @param typeCode 区域id
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return 集合
	 */
	IPage<SourcePlaceVO> sourceFind(IPage<SourcePlaceVO> page,@Param("tableName") String tableName,@Param("typeCode")String typeCode,@Param("startTime") String startTime, @Param("endTime")String endTime);

	//查询三亚市和4个景区
	IPage<SourcePlaceVO> sourceSanyaAndFour(String startTime,String endTime, String typeCode,IPage page);
	//大屏客源地查询三亚市
	SourcePlaceVO sourceSanyaNewTime(String typeCode);

	/**
	 * 区划客源地
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return 列表
	 */
	List<ZoneSourcePlaceVO> zoneSourcePlace(@Param("startTime") String startTime, @Param("endTime")String endTime);


	/**
	 * 获取客源地最新时间
	 * @return 最新时间
	 */
	String getNewTime();

	/**
	 * 获取客源地人数
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return listMap
	 */
	List<PeopleVo> getSourcePeople(@Param("startTime") String startTime, @Param("endTime")String endTime);

	/**
	 * 总人数
	 *
	 * @param typeCode 区域id
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return 性别百分比
	 */
	List<Integer> getTotalPeople(@Param("typeCode") String typeCode, @Param("startTime") String startTime, @Param("endTime") String endTime);



	/**
	 * 区划画像
	 *
	 * @param property 画像类型
	 * @param typeCode 区域id
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @param size   集合长度
	 * @return 性别百分比
	 */
	List<AreaVO> touristImageNew(@Param("property")String property, @Param("typeCode") String typeCode, @Param("startTime") String startTime, @Param("endTime") String endTime,@Param("size") Integer size);

	/**
	 * 获取最新时间和最老时间
	 *
	 * @return 时间对象
	 */
	TimeVO getTime();

}
