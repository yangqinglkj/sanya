package org.springblade.common.dto;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

/**
 * @Author yq
 * @Date 2020/12/23 17:28
 */
@Data
public class Demo {
	@Alias("deal_day")
	private String dealDay;
	@Alias("consume_area")
	private String consumeArea;
	private String traveller_type;
	private String trans_at;
	private String trans_num;
	private String acct_num;
}
