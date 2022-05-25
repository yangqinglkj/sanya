package org.springblade.modules.data.service.lmpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.common.entity.tx.area.AreaHeatingValue;
import org.springblade.common.entity.tx.area.AreaHeatingValueOneHour;
import org.springblade.common.mapper.tx.area.AreaHeatingValueMapper;
import org.springblade.common.mapper.tx.area.AreaHeatingValueOneHourMapper;
import org.springblade.modules.data.service.AreaHeatingValueOneHourService;
import org.springblade.modules.data.service.AreaHeatingValueService;
import org.springframework.stereotype.Service;

/**
 * @author yangqing
 */
@Service
public class AreaHeatingValueOneHourServiceImpl extends ServiceImpl<AreaHeatingValueOneHourMapper, AreaHeatingValueOneHour> implements AreaHeatingValueOneHourService {
}
