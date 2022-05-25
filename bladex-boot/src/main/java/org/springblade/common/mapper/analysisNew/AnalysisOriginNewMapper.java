
package org.springblade.common.mapper.analysisNew;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springblade.common.entity.TouristReception;
import org.springblade.common.entity.tx.analysisNew.AnalysisOriginNewEntity;

import java.util.List;

/**
 * 全国市级别来源地 Mapper 接口
 *
 * @Author yq
 * @Date 2020/9/16 9:42
 */
public interface AnalysisOriginNewMapper extends BaseMapper<AnalysisOriginNewEntity> {

	IPage<TouristReception> touristReception(String startTime, String endTime, String typeCode, IPage page);

	/**
	 * 批量插入
	 *
	 * @param tableName 表名
	 * @param list 集合对象
	 */
	void saveBatch(@Param("tableName") String tableName, List<AnalysisOriginNewEntity> list);
}
