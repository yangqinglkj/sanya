package org.springblade.modules.backstage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springblade.common.entity.tx.area.AreaPeopleCount;
import org.springblade.common.entity.tx.area.AreaRealTimePeople;
import org.springblade.common.vo.AreaVO;
import org.springblade.modules.backstage.service.TxAreaService;
import org.springblade.modules.backstage.vo.tx.SourcePlaceVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author yq
 * @Date 2020/9/24 15:46
 */
@RestController
@RequestMapping("/area")
@Api(value = "腾讯景点商圈", tags = "后台腾讯景点商圈接口")
public class TxAreaController {

	@Resource
	private TxAreaService txAreaService;

	@ApiOperation(value = "画像分析")
	@GetMapping("/touristImage")
	@CrossOrigin
	public Map<String, Object> touristImage(@RequestParam("typeCode") String typeCode, @RequestParam(value = "startTime", required = false) String startTime, @RequestParam(value = "endTime", required = false)String endTime) {
		return txAreaService.touristImage(typeCode,startTime,endTime);
	}

	@ApiOperation(value = "区域日累计人流量")
	@GetMapping("/visitorsCount")
	@CrossOrigin
	public IPage<AreaRealTimePeople> visitorsCount(@RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
												   @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
												   @RequestParam(value = "startTime",required = false) String startTime,
												   @RequestParam(value = "endTime",required = false) String endTime,
												   @RequestParam(value = "typeCode",required = false)String typeCode) {
		return txAreaService.visitorsCount(new Page<>(current,size),startTime,endTime,typeCode);
	}

	@ApiOperation(value = "区域日累计人流量（按日筛选）")
	@GetMapping("/visitorsCountByDay")
	@CrossOrigin
	public Map<String, Object> visitorsCountByDay(@RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
												   @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
												   @RequestParam(value = "startTime",required = false) String startTime,
												   @RequestParam(value = "endTime",required = false) String endTime,
												   @RequestParam(value = "typeCode",required = false)String typeCode) {
		return txAreaService.visitorsCountByDay(new Page<>(current,size),startTime,endTime,typeCode);
	}

}
