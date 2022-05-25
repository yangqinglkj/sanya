
package org.springblade.common.mapper.tx;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springblade.common.entity.tx.AreaStayDayEntity;
import org.springblade.common.entity.tx.analysis.AnalysisPopulationEntity;
import org.springblade.modules.backstage.vo.TimeVO;

import java.util.List;

/**
 * 区划停留天数Mapper 接口
 * @Author yq
 * @Date 2020/9/16 9:42
 */
public interface AreaStayDayMapper extends BaseMapper<AreaStayDayEntity> {

	List<AreaStayDayEntity> stayDay(@Param("typeCode") String typeCode);

	/**
	 * 柱状图数据
	 * @param typeCode 区划id
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param sql sql
	 * @return 集合
	 */
	List<AreaStayDayEntity> stayDaySanYa(@Param("typeCode") String typeCode,@Param("startTime") String startTime, @Param("endTime") String endTime);

	/**
	 *
	 * @param page 分页对象
	 * @param typeCode 区划id
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param sql sql
	 * @return 集合
	 */
	IPage<AreaStayDayEntity> touristStop(IPage<AreaStayDayEntity> page,@Param("typeCode") String typeCode ,@Param("startTime") String startTime, @Param("endTime") String endTime);

	/**
	 * 查询是否有当月数据
	 * @param month 当前月份
	 * @return 数量
	 */
	@Select("SELECT count(*) FROM tx_area_stay_day where create_time LIKE '${month}%'  ")
	int checkMonth(String month);

	/**
	 * 获取最新时间和最老时间
	 *
	 * @return 时间对象
	 */
	TimeVO getTime();

}
