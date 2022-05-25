package org.springblade.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author yq
 * @Date 2020/10/14 16:39
 */

public class MapUtils {
	/**
	 * 坐标转换，腾讯地图转换成百度地图坐标
	 * @param lat 腾讯纬度
	 * @param lon 腾讯经度
	 * @return 返回结果：经度,纬度
	 */
	public static Map<String,Object> mapTxToBd(double lat, double lon){
		double bdLat;
		double bdLon;
		double x_pi=3.14159265358979324 * 3000.0 / 180.0;
		double x = lon, y = lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
		bdLon = z * Math.cos(theta) + 0.0065;
		bdLat = z * Math.sin(theta) + 0.006;
		Map<String,Object> map = new HashMap<>(16);
		map.put("lon",bdLon);
		map.put("lat",bdLat);
		return map;
	}
}
