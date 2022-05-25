
package org.springblade.common.mapper.analysis;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springblade.common.entity.TravelDistance;
import org.springblade.common.entity.tx.analysis.AnalysisEntity;
import org.springblade.modules.backstage.vo.TimeVO;

import java.util.List;
import java.util.Map;

/**
 * 区划停留天数Mapper 接口
 *
 * @Author yq
 * @Date 2020/9/16 9:42
 */
public interface AnalysisMapper extends BaseMapper<AnalysisEntity> {
	/**
	 * 游客量折线图数据
	 *
	 * @param typeCode  区域id
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return 列表
	 */
	List<Map<String, Object>> touristVolume(String typeCode, String startTime, String endTime);

	/**
	 * 游客量表格数据
	 *
	 * @param page      分页对象
	 * @param typeCode  区域id
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return 列表
	 */
	IPage<AnalysisEntity> touristTable(IPage<AnalysisEntity> page, String typeCode, String startTime, String endTime);

	/**
	 * 旅游距离
	 *
	 * @param page      分页对象
	 * @param typeCode  区域id
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return 列表
	 */
	IPage<TravelDistance> travelDistance(IPage<TravelDistance> page, String typeCode, String startTime, String endTime);


	Integer sumPeople(String startTime, String endTime, String typeCode);

	List<TravelDistance> countThree(String startTime, String endTime, String typeCode);

	/**
	 * 批量插入区划画像
	 *
	 * @param list 区划画像集合对象
	 */
	void saveBatch(List<AnalysisEntity> list);

	/**
	 * 获取最新时间和最老时间
	 *
	 * @param typeCode 区域id
	 * @param type     类型
	 * @return 时间对象
	 */
	TimeVO getTime(@Param("typeCode") String typeCode, @Param("type") String type);
}
