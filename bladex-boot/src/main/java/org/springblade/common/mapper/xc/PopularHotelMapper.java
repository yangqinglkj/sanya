
package org.springblade.common.mapper.xc;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springblade.common.entity.xc.PopularHotelEntity;

import java.util.List;

/**
 * 热门酒店Mapper 接口
 * @Author yq
 * @Date 2020/9/16 9:42
 */
public interface PopularHotelMapper extends BaseMapper<PopularHotelEntity> {

	List<PopularHotelEntity> channelValue();

}
