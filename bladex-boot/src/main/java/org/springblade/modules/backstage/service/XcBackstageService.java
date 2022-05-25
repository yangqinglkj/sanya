package org.springblade.modules.backstage.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springblade.common.entity.xc.TransportationEntity;
import org.springblade.common.vo.xc.TransportationVO;
import org.springblade.modules.backstage.vo.xc.*;

import java.util.List;
import java.util.Map;

/**
 * @Author yq
 * @Date 2020/9/17 15:40
 */

public interface XcBackstageService {

	/**
	 * 预定方式百分比
	 *
	 * @param day 哪一天
	 * @return 预定方式百分比
	 */
	Map<String, Object> reserveChannelList(String day);


	/**
	 * 预定方式列表
	 *
	 * @param startMonth 开始月份
	 * @param endMonth   结束月份
	 * @param page       分页对象
	 * @return 预定方式列表
	 */
	Map<String, Object> reserveChannelTable(IPage<ReserveChannelTableVO> page, String startMonth, String endMonth);


	/**
	 * 提前预定天数百分比（饼状图）
	 *
	 * @param day  哪一天
	 * @param type 类型(0:所有   1：景区  2：酒店  3：机票  4：其他  5：度假  6：汽车 )
	 * @return 提前预定天数百分比
	 */
	Map<String,Object> reserveDayList(String day, String type);


	/**
	 * 提前预定天数百分比
	 *
	 * @param startMonth 开始月份
	 * @param endMonth   结束月份
	 * @param type       类型(0:所有   1：景区  2：酒店  3：机票  4：其他  5：度假  6：汽车 )
	 * @param page       分页对象
	 * @return 提前预定天数列表
	 */
	Map<String,Object> reserveDayTable(IPage<ReserveDayTableVO> page, String startMonth, String endMonth, String type);


	/**
	 * 酒店游客出游目的
	 *
	 * @param date 日期
	 * @return 酒店游客出游目的百分比
	 */
	Object hotelTourist(String date);

	/**
	 * 酒店游客出游目的
	 *
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return 酒店游客出游目的百分比
	 */
	Map<String, Object> hotelTouristTable(IPage<OutingPurposeVO> page, String startTime, String endTime);

	/**
	 * 热门酒店TOP
	 *
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @param page      分页对象
	 * @return 热门酒店列表
	 */
	Map<String, Object> topHotelList(IPage<HotelVO> page, String startTime, String endTime);

	/**
	 * 自由行/团队游占比
	 *
	 * @return 自由行/团队游占比
	 */
	Map<String, Object> wayList(String startTime, String endTime, IPage page);

	/**
	 * 年旅游次数
	 *
	 * @param months 月份
	 * @param page   分页对象
	 * @return 年旅游次数
	 */
	Map<String, Object> travelCountTable(IPage<HotelVO> page, String months);

	/**
	 * 各交通方式人次分布（饼状图）
	 *
	 * @param date 日期
	 * @return 集合
	 */
	Map<String,Object> transportationList(String date);

	/**
	 * 各交通方式人次分布（表格）
	 *
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @param page      分页对象
	 * @return 各交通方式人次分布列表
	 */
	Map<String,Object> transportationTable(IPage<HotelVO> page, String startTime, String endTime);
}
