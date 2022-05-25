
package org.springblade.common.mapper.tx.area;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.common.entity.tx.area.AreaHeatingValue;
import org.springblade.common.entity.tx.area.AreaHeatingValueOneHour;

import java.util.List;

/**
 * 区域热力值表 一个小时存一次数据 接口
 * @Author yq
 * @Date 2020/10/5 9:42
 */
public interface AreaHeatingValueOneHourMapper extends BaseMapper<AreaHeatingValueOneHour> {
	/**
	 * 批量插入
	 * @param list 集合
	 * @return 是否成功
	 */
	boolean saveBatch(@Param("list") List<AreaHeatingValueOneHour> list);
}
