
package org.springblade.common.mapper.xc;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springblade.common.entity.xc.OutingWayEntity;

import java.util.List;

/**
 * 游客出游方式Mapper 接口
 * @Author yq
 * @Date 2020/9/16 9:42
 */
public interface OutingWayMapper extends BaseMapper<OutingWayEntity> {

	@Select("SELECT * from  xc_outing_way  WHERE  create_time = (SELECT MAX(create_time)  FROM  xc_outing_way ) ")
	List<OutingWayEntity> seNewTime();

}
