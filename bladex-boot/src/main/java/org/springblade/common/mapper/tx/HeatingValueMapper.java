
package org.springblade.common.mapper.tx;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springblade.common.entity.tx.HeatingValue;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 海南省经纬度Mapper 接口
 * @Author yq
 * @Date 2020/9/16 9:42
 */
public interface HeatingValueMapper extends BaseMapper<HeatingValue> {

	@Select("SELECT create_time  FROM tx_heating_value ORDER BY create_time DESC LIMIT 1")
	Date seleteOneHeatingValue();

	List<HeatingValue> newestSelete(String startTime, String endTime);

	@Delete("DELETE from tx_heating_value WHERE create_time < #{startTime}")
	void deleteFiveMinute(LocalDateTime startTime);
}
