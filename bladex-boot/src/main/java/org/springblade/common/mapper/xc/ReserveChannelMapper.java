
package org.springblade.common.mapper.xc;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springblade.common.entity.xc.ReserveChannelEntity;

import java.util.List;

/**
 * 预定渠道Mapper 接口
 * @Author yq
 * @Date 2020/9/16 9:42
 */
public interface ReserveChannelMapper extends BaseMapper<ReserveChannelEntity> {

	List<ReserveChannelEntity> channelValue();

	@Select("select create_time from xc_reserve_channel where  type = '0' ORDER BY create_time DESC  LIMIT 1 ")
	String findNewTime();
}
