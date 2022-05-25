
package org.springblade.common.mapper.xc;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.common.entity.xc.ReserveDayEntity;

import java.util.List;

/**
 * 预定天数Mapper 接口
 * @Author yq
 * @Date 2020/9/16 9:42
 */
public interface ReserveDayMapper extends BaseMapper<ReserveDayEntity> {


	List<String> dayValue(@Param("classType")String classType,@Param("type")Integer type);

}
