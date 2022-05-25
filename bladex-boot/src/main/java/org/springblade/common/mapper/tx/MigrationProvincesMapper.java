
package org.springblade.common.mapper.tx;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.common.entity.tx.MigrationEntity;
import org.springblade.common.entity.tx.MigrationProvincesEntity;

import java.util.List;

/**
 * @Author yq
 * @Date 2020/9/16 9:42
 */
public interface MigrationProvincesMapper extends BaseMapper<MigrationProvincesEntity> {

	/**
	 * 插入区划迁徙省市表数据
	 *
	 * @param list 集合
	 * @return 是否成功
	 */
	boolean saveBatch(@Param("list") List<MigrationProvincesEntity> list);
}
