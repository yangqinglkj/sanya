
package org.springblade.common.mapper.xc;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springblade.common.entity.xc.OutingPurposeEntity;

import java.util.List;

/**
 * 酒店游客出行目的Mapper 接口
 * @Author yq
 * @Date 2020/9/16 9:42
 */
public interface OutingPurposeMapper extends BaseMapper<OutingPurposeEntity> {

	@Select("SELECT create_time FROM xc_outing_purpose  ORDER BY create_time DESC LIMIT 1 ")
	String newestDate();

	@Select("SELECT * FROM xc_outing_purpose  WHERE create_time >= #{date}")
	List<OutingPurposeEntity> newSeleteList(String date);

}
