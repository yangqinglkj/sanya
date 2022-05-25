package org.springblade.modules.backstage.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springblade.common.entity.FollowAndFree;
import org.springblade.common.entity.TravelCount;
import org.springblade.common.entity.xc.*;
import org.springblade.common.vo.xc.TransportationVO;
import org.springblade.modules.backstage.vo.TimeVO;
import org.springblade.modules.backstage.vo.xc.HotelVO;
import org.springblade.modules.backstage.vo.xc.OutingPurposeVO;
import org.springblade.modules.backstage.vo.xc.ReserveDayTableVO;

import java.util.List;
import java.util.Map;

/**
 * @Author yq
 * @Date 2020/9/24 15:53
 */

public interface XcBackstageMapper {

	/**
	 * 预定方式百分比
	 *
	 * @param day 哪一天
	 * @return 预定方式百分比
	 */
	List<ReserveChannelEntity> reserveChannelList(@Param("day") String day);

	/**
	 * 获取所有列名
	 * @return
	 */
	List<String> getAllColumn();

	/**
	 * 预定方式列表
	 *
	 * @param startMonth 开始月份
	 * @param endMonth   结束月份
	 * @param page       分页对象
	 * @return 预定方式列表
	 */
	IPage<Map<String,Object>> reserveChannelTable(IPage page, @Param("startMonth") String startMonth, @Param("endMonth") String endMonth, @Param("list") List<String> list);


	/**
	 * 提前预定天数百分比（饼状图）
	 *
	 * @param day  哪一天
	 * @param type 类型(0:所有   1：景区  2：酒店  3：机票  4：其他  5：度假  6：汽车 )
	 * @return 提前预定天数百分比
	 */
	List<ReserveDayEntity> reserveDayList(@Param("day") String day, @Param("type") String type);

	/**
	 * 获取所有列名
	 * @return
	 */
	List<String> getAllDayColumn();

	/**
	 * 提前预定天数百分比（列表）
	 *
	 * @param startMonth 开始月份
	 * @param endMonth   结束月份
	 * @param type       类型(0:所有   1：景区  2：酒店  3：机票  4：其他  5：度假  6：汽车 )
	 * @param page       分页对象
	 * @return 提前预定天数列表
	 */
	IPage<Map<String,Object>> reserveDayTable(IPage<ReserveDayTableVO> page, @Param("startMonth") String startMonth, @Param("endMonth") String endMonth, @Param("type") String type,@Param("list") List<String> list);

	/**
	 * 酒店游客出游目的
	 *
	 * @param date 日期
	 * @return 酒店游客出游目的百分比
	 */
	List<OutingPurposeEntity> hotelTourist(@Param("date") String date);

	/**
	 * 酒店游客出游目的（表格）
	 *
	 * @param page       分页对象
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return 酒店游客出游目的百分比
	 */
	IPage<OutingPurposeVO> hotelTouristTable(IPage<OutingPurposeVO> page,@Param("startTime") String startTime,@Param("endTime") String endTime);

	/**
	 * 热门酒店TOP
	 *
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @param page      分页对象
	 * @return 热门酒店列表
	 */
	IPage<HotelVO> topHotelList(IPage<HotelVO> page, @Param("startTime") String startTime, @Param("endTime") String endTime);

	/**
	 * 自由行/团队游占比
	 * @return 自由行/团队游占比
	 */
	List<OutingWayEntity> wayList(String startTime, String endTime);

	/**
	 * 年旅游次数
	 *
	 * @param months 月份
	 * @param page   分页对象
	 * @return 年旅游次数
	 */
	IPage<TravelCount> travelCountTable(IPage<HotelVO> page, String months);

	/**
	 * 各交通方式人次分布（饼状图）
	 * @param date 日期
	 * @return  集合
	 */
	List<TransportationEntity> transportationList(@Param("date") String date);

	/**
	 * 各交通方式人次分布（表格）
	 *
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @param page      分页对象
	 * @return 各交通方式人次分布列表
	 */
	IPage<TransportationVO> transportationTable(IPage<HotelVO> page, @Param("startTime") String startTime, @Param("endTime") String endTime);

	TravelCount allMouthCount(String months);


	IPage<FollowAndFree> wayListPage(String startTime, String endTime, IPage page);

	/**
	 * 获取最新时间和最老时间
	 *
	 * @return 时间对象
	 */
	TimeVO getTime();
	/**
	 * 获取最新时间和最老时间
	 *
	 * @return 时间对象
	 */
	TimeVO getWayListTime();

	/**
	 * 获取最新时间和最老时间
	 *
	 * @return 时间对象
	 */
	TimeVO getHotelTouristTime();

	/**
	 * 获取最新时间和最老时间
	 * @param type       类型(0:所有   1：景区  2：酒店  3：机票  4：其他  5：度假  6：汽车 )
	 * @return 时间对象
	 */
	TimeVO getReserveDayTime(@Param("type")String type);

	/**
	 * 获取最新时间和最老时间
	 *
	 * @return 时间对象
	 */
	TimeVO getReserveChannelTime();

	/**
	 * 获取最新时间和最老时间
	 *
	 * @return 时间对象
	 */
	TimeVO getTopHotelTime();

	/**
	 * 获取最新时间和最老时间
	 *
	 * @return 时间对象
	 */
	TimeVO getTransportationTime();
}
