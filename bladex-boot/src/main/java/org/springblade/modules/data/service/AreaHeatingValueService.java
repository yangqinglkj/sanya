package org.springblade.modules.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.common.entity.tx.area.AreaHeatingValue;

import java.util.List;


/**
 * @author yangqing
 */
public interface AreaHeatingValueService extends IService<AreaHeatingValue> {
	boolean saveBatch(List<AreaHeatingValue> list);
}
