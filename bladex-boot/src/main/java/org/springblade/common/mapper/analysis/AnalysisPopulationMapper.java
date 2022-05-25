
package org.springblade.common.mapper.analysis;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.common.entity.tx.analysis.AnalysisPopulationEntity;
import org.springblade.modules.backstage.vo.TimeVO;

import java.util.List;
import java.util.Map;

/**
 * 人数分析 Mapper 接口
 * @Author yq
 * @Date 2020/9/16 9:42
 */
public interface AnalysisPopulationMapper extends BaseMapper<AnalysisPopulationEntity> {

	/**
	 * 大屏出访分析
	 * @return 集合
	 */
	List<Map<String,String>> visitList();

	/**
	 *	游客出访分析 折线图
	 * @param typeCode 区域id
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return 列表
	 */
	List<Map<String,Object>> touristVisiting(String typeCode,String startTime, String endTime);

	IPage<AnalysisPopulationEntity> sanYaAndTimePage(String startTime, String endTime, IPage page, String typeCode);

	List<AnalysisPopulationEntity> saveCount(String startTime, String endTime, String typeCode);

	Integer sumPeople(String startTime, String endTime,String typeCode);

	/**
	 * 获取最新时间和最老时间
	 *
	 * @return 时间对象
	 */
	TimeVO getTime();

}
