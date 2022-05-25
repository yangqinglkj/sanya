
package org.springblade.common.mapper.analysisNew;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.common.entity.tx.analysisNew.AnalysisPopulationNewEntity;

import java.util.List;
import java.util.Map;

/**
 * 人数分析 Mapper 接口
 * @Author yq
 * @Date 2020/9/16 9:42
 */
public interface AnalysisPopulationNewMapper extends BaseMapper<AnalysisPopulationNewEntity> {


	List<Map<String,Object>> selectSanYa();

	List<Map<String,Object>> selectAllSanYaAndTime(String startTime, String endTime, String typeCode);

	IPage<AnalysisPopulationNewEntity> sanYaAndTimePage(String startTime, String endTime, IPage page, String typeCode);

	/**
	 * 批量插入
	 * @param list 集合对象
	 */
	void saveBatch(List<AnalysisPopulationNewEntity> list);

}
