
package org.springblade.common.mapper.tx.area;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springblade.common.entity.tx.area.AreaHeatingValue;
import org.springblade.common.entity.tx.area.AreaPeopleOne;

import java.util.List;

/**
 * 区域热力值表 5分钟更新一次数据 接口
 * @Author yq
 * @Date 2020/10/5 9:42
 */
public interface AreaHeatingValueMapper extends BaseMapper<AreaHeatingValue> {

	/**
	 * 批量插入
	 * @param lists 集合
	 * @return 是否成功
	 */
	boolean saveBatch(@Param("lists") List<AreaHeatingValue> lists);


	@Select("select * from tx_area_heating_value where area_code = #{areaCode} ")
	List<AreaHeatingValue>  seByAreaCode(String areaCode);
}
