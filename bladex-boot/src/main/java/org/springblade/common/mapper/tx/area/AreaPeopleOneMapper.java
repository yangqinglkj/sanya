
package org.springblade.common.mapper.tx.area;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springblade.common.entity.tx.AreaStayDayEntity;
import org.springblade.common.entity.tx.area.AreaPeopleOne;

import java.util.List;

/**
 * 1.区域实时人数 接口
 * @Author yq
 * @Date 2020/10/5 9:42
 */
public interface AreaPeopleOneMapper extends BaseMapper<AreaPeopleOne> {
	/**
	 * 插入区域实时人数集合
	 * @param entityList 集合对象
	 * @return 是否成功
	 */
	boolean saveAreaPeople(@Param("entityList") List<AreaPeopleOne> entityList);
}
