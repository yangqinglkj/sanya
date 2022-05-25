
package org.springblade.common.mapper.tx;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springblade.common.entity.tx.MigrationEntity;
import org.springblade.common.vo.MigrationVO;
import org.springblade.modules.backstage.vo.TimeVO;

/**
 * @Author yq
 * @Date 2020/9/16 9:42
 */
public interface MigrationMapper extends BaseMapper<MigrationEntity> {

	/**
	 * 获取迁徒方式人数百分比
	 * @param page 分页对象
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return 列表
	 */
	IPage<MigrationVO> getMigration(IPage<MigrationEntity> page, String startTime, String endTime);

	/**
	 * 获取最新时间和最老时间
	 *
	 * @return 时间对象
	 */
	TimeVO getTime();
}
