
package org.springblade.common.mapper.tx.area;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springblade.common.entity.tx.area.AreaPeopleCount;
import org.springblade.common.entity.tx.area.AreaRealTimePeople;

/**
 * 3.区域实时人流 接口
 * @Author yq
 * @Date 2020/10/5 9:42
 */
public interface AreaRealTimePeopleMapper extends BaseMapper<AreaRealTimePeople> {
	@Select("select * from tx_area_real_time_people WHERE area_code = #{code} and create_time = #{time} LIMIT 1 ")
	AreaRealTimePeople findAreaCodePeople(String code,String time);

}
