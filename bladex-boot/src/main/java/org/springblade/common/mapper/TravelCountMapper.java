
package org.springblade.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springblade.common.entity.TravelCount;

import java.util.List;


public interface TravelCountMapper extends BaseMapper<TravelCount> {

	@Select("SELECT * FROM `travel_count` order by months desc limit 12")
	List<TravelCount> selectTraCount(int year);

}
