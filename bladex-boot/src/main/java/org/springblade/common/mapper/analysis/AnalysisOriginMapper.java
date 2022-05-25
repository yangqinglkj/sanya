
package org.springblade.common.mapper.analysis;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springblade.common.entity.TouristReception;
import org.springblade.common.entity.tx.analysis.AnalysisOriginEntity;
import org.springblade.modules.backstage.vo.TimeVO;
import org.springblade.modules.backstage.vo.tx.PeopleVo;

import java.util.List;

/**
 * 全国市级别来源地 Mapper 接口
 *
 * @Author yq
 * @Date 2020/9/16 9:42
 */
public interface AnalysisOriginMapper extends BaseMapper<AnalysisOriginEntity> {

	/**
	 * 游客接待分析
	 * @param page 分页对象
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param typeCode 区域id
	 * @return 列表
	 */
	IPage<TouristReception> touristReception(IPage<TouristReception> page ,@Param("startTime") String startTime, @Param("endTime")String endTime,@Param("typeCode") String typeCode);



	List<TouristReception> seleteCount(String startTime, String endTime, String typeCode);

	/**
	 * 批量插入客源地
	 * @param list 客源地集合对象
	 */
	void saveBatch(List<AnalysisOriginEntity> list);

	/**
	 * 获取最新时间和最老时间
	 *
	 * @param typeCode 区域id
	 * @return 时间对象
	 */
	TimeVO getTime(@Param("typeCode") String typeCode);
}
