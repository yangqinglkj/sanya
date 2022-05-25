package org.springblade.modules.backstage.vo.tx;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;

/**
 * @Author yq
 * @Date 2020/9/30 2:55
 */
@Data
public class ZoneSourcePlaceVO {

	private String provinceName;
	private LocalDate createTime;

	@JsonIgnore
	private String sanyaSumPercent;
	@JsonIgnore
	private String sanyaAvgPercent;
	private String sanyaPeople;
	private String sanyaPercent;

	@JsonIgnore
	private String haitangSumPercent;
	@JsonIgnore
	private String haitangAvgPercent;
	private String haitangPeople;
	private String haitangPercent;

	@JsonIgnore
	private String jiyangSumPercent;
	@JsonIgnore
	private String jiyangAvgPercent;
	private String jiyangPeople;
	private String jiyangPercent;
	@JsonIgnore
	private String tianyaSumPercent;
	@JsonIgnore
	private String tianyaAvgPercent;
	private String tianyaPeople;
	private String tianyaPercent;

	@JsonIgnore
	private String yazhouSumPercent;
	@JsonIgnore
	private String yazhouAvgPercent;
	private String yazhouPeople;
	private String yazhouPercent;

}
