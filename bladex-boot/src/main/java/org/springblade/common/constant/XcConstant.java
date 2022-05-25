package org.springblade.common.constant;

/**
 * @Author yq
 * @Date 2020/9/2 12:31
 * 携程接口
 */
public interface XcConstant {
	/**
	 * 账号
	 */
	String XC_ACCOUNT = "sanya";
	/**
	 * token
	 */
	String XC_TOKEN = "202fdb9a-de76-4ffc-a5be-c8d061d1e0d5";
	/**
	 * 国家
	 */
	String XC_COUNTRY = "中国";
	/**
	 * 省份
	 */
	String XC_PROVINCE = "海南";
	/**
	 * 城市
	 */
	String XC_CITY = "三亚";
	/**
	 * 2.游客出游方式分布
	 */
	String XC_OUTING_WAY_URL = "http://apiproxy.ctrip.com/apiproxy/soa2/20340/json/getTravelTypePersonRatio";
	/**
	 * 3.游客出游类型人次占比
	 */
	String XC_OUTING_TYPE = "http://apiproxy.ctrip.com/apiproxy/soa2/20340/json/getTravelTypeDist";
	/**
	 * 4.各交通方式人次分布
	 */
	String XC_TRANSPORTATION_URL = "http://apiproxy.ctrip.com/apiproxy/soa2/20340/json/getTripModePersonRatio";
	/**
	 * 5.热门酒店TOP10
	 */
	String XC_POPULAR_HOTEL = "http://apiproxy.ctrip.com/apiproxy/soa2/20340/json/getPopularHotelPersonDist";
	/**
	 * 6.酒店游客出行目的
	 */
	String XC_OUTING_PURPOSE = "http://apiproxy.ctrip.com/apiproxy/soa2/20340/json/getTravelPurposOfHotelTourists";

	/**
	 * 预定渠道
	 */
	String XC_RESERVE_CHANNEL = "http://apiproxy.ctrip.com/apiproxy/soa2/20340/json/getProportionOfBookingChannels";

	/**
	 * 提前预定天数
	 */
	String XC_RESERVE_DAY = "http://apiproxy.ctrip.com/apiproxy/soa2/20340/json/getPrebookDaysDistributionOfBu";
}
