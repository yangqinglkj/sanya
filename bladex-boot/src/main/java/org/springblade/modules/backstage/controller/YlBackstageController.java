package org.springblade.modules.backstage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.common.entity.capitaConsumptionTopNew;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.modules.backstage.service.XcBackstageService;
import org.springblade.modules.backstage.service.YlBackstageService;
import org.springblade.modules.backstage.vo.xc.ReserveChannelTableCountVO;
import org.springblade.modules.backstage.vo.xc.ReserveDayTableVO;
import org.springblade.modules.backstage.vo.xc.ReserveDayVO;
import org.springblade.modules.backstage.vo.yl.CityConsumptionVO;
import org.springblade.modules.backstage.vo.yl.ConsumerAresVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author yq
 * @Date 2020/9/24 15:46
 */
@RestController
@RequestMapping("/backstageUnionPay")
@Api(value = "银联", tags = "银联后台接口")
public class YlBackstageController {

	@Resource
	private YlBackstageService ylBackstageService;

	@ApiOperation(value = "主要消费区域和场所")
	@GetMapping("/consumerAres")
	public Map<String,Object> consumerAres(@RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
											  @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
											  @RequestParam(value = "startTime", required = false) String startTime,
											  @RequestParam(value = "endTime", required = false) String endTime,
											  @RequestParam(value = "type", required = false) String type,
											  @RequestParam(value = "business", required = false) String business) {
		return ylBackstageService.consumerAres(new Page<>(current, size), startTime, endTime, type,business);
	}


	@ApiOperation(value = "主要消费区域和场所折线图")
	@GetMapping("/consumerAresGraph")
	public Map<String, Object> consumerAresGraph(@RequestParam(value = "startTime", required = false) String startTime,
												 @RequestParam(value = "endTime", required = false) String endTime,
												 @RequestParam(value = "type", required = false) String type,
												 @RequestParam(value = "business", required = false) String business) {
		return ylBackstageService.consumerAresGraph(startTime, endTime, type,business);
	}


	@ApiOperation(value = "三亚市全市消费")
	@GetMapping("/cityConsumption")
	public Map<String, Object> cityConsumption(@RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
												   @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
												   @RequestParam(value = "startTime", required = false) String startTime,
												   @RequestParam(value = "endTime", required = false) String endTime) {
		return ylBackstageService.cityConsumption(new Page<>(current,size),startTime, endTime);
	}

	@ApiOperation(value = "人均消费省份TOP10")
	@GetMapping("/capitaConsumption")
	public Map<String,Object> capitaConsumption(@RequestParam(value = "startTime", required = false) String startTime,
														   @RequestParam(value = "endTime", required = false) String endTime) {
		return ylBackstageService.capitaConsumption(startTime,endTime);
	}

	@ApiOperation(value = "行业消费规模及贡献度")
	@GetMapping("/consumptionScaleA")
	public Map<String, Object> consumptionScaleA(@RequestParam(value = "startTime", required = false) String startTime,
								   @RequestParam(value = "endTime", required = false) String endTime,
								   @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
								   @RequestParam(value = "current", required = false, defaultValue = "1") Integer current) {
		return ylBackstageService.consumptionScaleA(startTime,endTime,new Page<>(current,size));
	}

}
