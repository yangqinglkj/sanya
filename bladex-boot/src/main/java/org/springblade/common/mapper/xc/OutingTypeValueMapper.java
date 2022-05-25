
package org.springblade.common.mapper.xc;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springblade.common.entity.xc.OutingTypeValueEntity;

import java.util.List;

/**
 * 游客出游类型Mapper 接口
 * @Author yq
 * @Date 2020/9/16 9:42
 */
public interface OutingTypeValueMapper extends BaseMapper<OutingTypeValueEntity> {

	List<OutingTypeValueEntity> typeValue();

}
