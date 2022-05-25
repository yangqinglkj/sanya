package org.springblade.common.constant;

/**
 * @Author yq
 * @Date 2020/9/2 12:31
 * 服务名常量
 */
public interface ServiceConstant {

	String APPLICATION_DATA_NAME = "sanya-data";
	String APPLICATION_SCREEN_NAME = "sanya-screen";

	/**
	 * 腾讯接口key
	 */
	String TX_KEY = "4WABZ-QNQC5-EQCIC-QVK4Z-SN4EQ-IXFVE";
	              //"4WABZ-QNQC5-EQCIC-QVK4Z-SN4EQ-IXFVE"；
	/**
	 * 四川省
	 */
	String SC_CODE = "510000";
	/**
	 * 广东省
	 */
	String GD_CODE = "440000";
	/**
	 * 三亚市 code
	 */
	String TX_SAN_YA_CODE = "460200";
	/**
	 * 海棠区 code
	 */
	String TX_HAI_TANG_AREA_CODE = "460202";
	/**
	 * 吉阳区 code
	 */
	String TX_JI_YANG_AREA_CODE = "460203";
	/**
	 * 天涯区 code
	 */
	String TX_TIAN_YA_AREA_CODE = "460204";
	/**
	 * 崖州区 code
	 */
	String TX_YA_ZHOU_AREA_CODE = "460205";

	/**
	 * 出发城市code
	 */
	String START_CODE = "460200";

	/**
	 * 到达城市code
	 */
	String END_CODE = "460200";

	/**
	 * 上半月，202001_1；
	 * 下半月，202001_2；
	 * 整月，202001_3；
	 */
	String MONTH = "";

	/**
	 * 腾讯热力值接口
	 */
	String TX_HEATING_URL = "https://apis.map.qq.com/bigdata/realtime/v1/citylocationpoint";

	/**
	 * 区域迁徙分析接口
	 */
	String TX_REGIONAL_MIGRATION_URL = "https://apis.map.qq.com/bigdata/realtime/v1/migrationuserprofile";

	/**
	 *区划停留天数接口
	 */
	String TX_AREA_STAY_DAY_URL = "https://apis.map.qq.com/bigdata/popinsight/v1/arstay";

	/**
	 * 区划访客/出访分析
	 * 根据提供的省市区 adcode、日期查询访客/出访人群，人数，年龄、性别、来源地/到访地等画像数据
	 */
	String TX_ANALYSIS_URL = "https://apis.map.qq.com/bigdata/realtime/v1.1/userprofileday";

	/**
	 * 6.区域实时人口画像
	 */
	String TX_AREA_IMAGE = "https://apis.map.qq.com/bigdata/realtime/v1.1/userprofile";

	/**
	 * 7.区域实时人口画像接口（V2 beta）
	 */
	String TX_ANALYSIS_V2_URL = "https://apis.map.qq.com/bigdata/realtime/v1.1/userprofilenew";









	/**
	 * 1.区域实时人数
	 */
	String TX_AREA_PEOPLE_URL = "https://apis.map.qq.com/bigdata/realtime/v1.1/population";

	/**
	 * 区域实时人口热力图
	 */
	String TX_AREA_HEATING_VALUE_URL = "https://apis.map.qq.com/bigdata/realtime/v1.1/locationpoint";

	/**
	 * 4.区域实时人流累计
	 */
	String TX_AREA_PEOPLE_COUNT_URL = "https://apis.map.qq.com/bigdata/realtime/v1.1/flowsum";
	/**
	 * 区划迁徙总览
	 */
	String TX_MIGRATION_URL = "https://apis.map.qq.com/bigdata/realtime/v1/adcodemigration";
	/**
	 * 3.区域实时人流
	 */
	String TX_AREA_REAL_TIME_PEOPLE_URL = "https://apis.map.qq.com/bigdata/realtime/v1.1/flow";
}
