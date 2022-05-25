package org.springblade.modules.screen.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springblade.common.vo.Index5VO;
import org.springblade.common.vo.TravelDayIndex2VO;
import org.springblade.modules.screen.service.UnionPayService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author yq
 * @Date 2020/9/17 15:49
 */
@Slf4j
@RestController
@RequestMapping("/unionPay")
@Api(value = "银联", tags = "银联接口")
public class UnionPayController {
	@Resource
	private UnionPayService unionPayService;

	@ApiOperation(value = "主要消费区域和场所")
	@GetMapping("/getTourismConsumption")
	@CrossOrigin
	public List<Index5VO> findAll(@RequestParam("type") Integer type) {
		return unionPayService.findAll(type);
	}

	@ApiOperation(value = "人均消费省份")
	@GetMapping("/getPerCapitalConsumptionList")
	@CrossOrigin
	public List<Map<String, Object>> getPerCapitalConsumptionList() {
		return unionPayService.getPerCapitalConsumptionList();
	}

	@ApiOperation(value = "行业消费规模及贡献度")
	@GetMapping("/getIndustry")
	@CrossOrigin
	public List<TravelDayIndex2VO> getIndustry() {
		return unionPayService.getIndustry();
	}

	@ApiOperation(value = "三亚市人均消费")
	@GetMapping("/average")
	@CrossOrigin
	public Map<String, String> average() {
		return unionPayService.average();
	}


}
