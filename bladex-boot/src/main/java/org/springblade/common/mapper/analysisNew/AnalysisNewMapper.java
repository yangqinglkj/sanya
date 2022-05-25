
package org.springblade.common.mapper.analysisNew;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Select;
import org.springblade.common.entity.TravelDistance;
import org.springblade.common.entity.tx.analysis.AnalysisEntity;
import org.springblade.common.entity.tx.analysisNew.AnalysisNewEntity;
import org.springblade.common.entity.tx.analysisNew.AnalysisPopulationNewEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 区划停留天数Mapper 接口
 * @Author yq
 * @Date 2020/9/16 9:42
 */
public interface AnalysisNewMapper extends BaseMapper<AnalysisNewEntity> {

	List<Map<String,Object>> touristVolume(String startTime, String endTime, String typeCode);

	IPage<AnalysisNewEntity> dayPageTourist(String startTime, String endTime, IPage page, String typeCode);

	IPage<TravelDistance> travelDistance(String startTime, String endTime, IPage page, String typeCode);

	@Select("select percent from tx_analysis_new where type = 'population' and  type_code = #{typeCode} and create_time = #{time} ")
	String selectPopulationToday(String typeCode, LocalDateTime time);

	/**
	 * 批量插入
	 * @param list 集合对象
	 */
	void saveBatch(List<AnalysisNewEntity> list);
}
