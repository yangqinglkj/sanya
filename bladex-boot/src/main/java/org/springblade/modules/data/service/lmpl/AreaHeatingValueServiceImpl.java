package org.springblade.modules.data.service.lmpl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.common.entity.tx.area.AreaHeatingValue;
import org.springblade.common.mapper.tx.area.AreaHeatingValueMapper;
import org.springblade.modules.data.service.AreaHeatingValueService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yangqing
 */
@Service
public class AreaHeatingValueServiceImpl extends ServiceImpl<AreaHeatingValueMapper, AreaHeatingValue> implements AreaHeatingValueService {
	@Override
	public boolean saveBatch(List<AreaHeatingValue> list) {
		return baseMapper.saveBatch(list);
	}
}
