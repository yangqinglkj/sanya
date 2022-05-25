package org.springblade.common.utils;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author yq
 * @Date 2020/11/5 11:24
 */

public class AreaCodeUtils {

	/**
	 * 景点ID列表
	 */
	public  static Map<String,String> areaMap(){
		Map<String,String> map = new HashMap<>(16);
		//175756  -> 176415 -> 4069462959793577915
		map.put("崖州古城", "4069462959793577915");
		//175751 -> 176309 -> 4069461880929738304
		map.put("【5A】大小洞天景区", "4069461880929738304");
		//175729 -> 176390 -> 4069283472388272897
		map.put("【4A】海南玫瑰谷", "4069283472388272897");
		//175727 -> 176389 -> 4069281244018616740
		map.put("【4A】鹿回头风景区", "4069281244018616740");
		//175726 -> 176265 -> 4069846805240994142
		map.put("海昌梦幻海洋不夜城", "4069846805240994142");
		//175725 -> 176396 -> 4069470668819008983
		map.put("【4A】三亚千古情", "4069470668819008983");
		//175724 -> 176286 -> 4069282708340861593
		map.put("【4A】大东海景区", "4069282708340861593");
		//176092 -> 176314 -> 4069283618460972764
		map.put("【4A】热带天堂森林公园", "4069283618460972764");
		//175722 -> 176293 -> 4069280573448888914
//		map.put("【4A】西岛旅游区", "4069280573448888914");
		map.put("【4A】西岛旅游区", "4069280573219172332");
		//175721 -> 176394 -> 4069462018333514283
		map.put("【5A】海南南山景区", "4069462018333514283");
		//175719 -> 176268 -> 4069846942670665018
//		map.put("蜈支洲岛", "4069846942670665018");
		map.put("蜈支洲岛", "4069846942546510768");
		//175714 -> 176393 -> 4069468482687799490
		map.put("【4A】天涯海角", "4069468482687799490");

//		//175756  -> 176415 -> 4069462959793577915
//		map.put("崖州古城", "176415");
//		//175751 -> 176309 -> 4069461880929738304
//		map.put("【5A】大小洞天景区", "176309");
//		//175729 -> 176390 -> 4069283472388272897
//		map.put("【4A】海南玫瑰谷", "176390");
//		//175727 -> 176389 -> 4069281244018616740
//		map.put("【4A】鹿回头风景区", "176389");
//		//175726 -> 176265 -> 4069846805240994142
//		map.put("海昌梦幻海洋不夜城", "176265");
//		//175725 -> 176396 -> 4069470668819008983
//		map.put("【4A】三亚千古情", "176396");
//		//175724 -> 176286 -> 4069282708340861593
//		map.put("【4A】大东海景区", "176286");
//		//176092 -> 176314 -> 4069283618460972764
//		map.put("【4A】热带天堂森林公园", "176314");
//		//175722 -> 176293 -> 4069280573448888914
//		map.put("【4A】西岛旅游区", "176293");
//		//175721 -> 176394 -> 4069462018333514283
//		map.put("【5A】海南南山景区", "176394");
//		//175719 -> 176268 -> 4069846942670665018
//		map.put("蜈支洲岛", "176268");
//		//175714 -> 176393 -> 4069468482687799490
//		map.put("【4A】天涯海角", "176393");
		return map;
	}

	/**
	 * 商圈ID列表
	 */
	public static Map<String,String> businessMap(){
		Map<String,String> map = new HashMap<>(16);
//		map.put("迎宾路商圈", "175755");
//		map.put("海棠湾商圈", "176094");
//		map.put("大东海商圈", "175753");
//		map.put("商品街商圈", "175742");
//		map.put("解放路步行街商圈", "175740");
//		map.put("免税城商圈", "175739");
//		map.put("亚龙湾商圈", "176096");
//		map.put("三亚湾商圈", "176093");

		map.put("迎宾路商圈", "4069283004616630384");
		map.put("海棠湾商圈", "4069847118364191869");
		map.put("大东海商圈", "4069282712636644483");
		map.put("商品街商圈", "4069282931181451953");
		map.put("解放路步行街商圈", "4069281457871158129");
		map.put("免税城商圈", "4069847934674639643");
		map.put("亚龙湾商圈", "4069283497961413502");
		map.put("三亚湾商圈", "4069469015674903436");
		return map;
	}

	public static Map<String, String> originMap(){
		Map<String,String> map = Maps.newHashMap();
		map.put("4069462959793577915", "tx_area_origin_scenic_yazhou");
		map.put("4069461880929738304", "tx_area_origin_scenic_daxiaodongtian");
		map.put("4069283472388272897", "tx_area_origin_scenic_meiguigu");
		map.put("4069281244018616740", "tx_area_origin_scenic_luhuitou");
		map.put("4069846805240994142", "tx_area_origin_scenic_buyecheng");
		map.put("4069470668819008983", "tx_area_origin_scenic_qianguqing");
		map.put("4069282708340861593", "tx_area_origin_scenic_dadonghai");
		map.put("4069283618460972764", "tx_area_origin_scenic_senlin");
		map.put("4069280573219172332", "tx_area_origin_scenic_xidao");
		map.put("4069462018333514283", "tx_area_origin_scenic_nanshan");
		map.put("4069846942546510768", "tx_area_origin_scenic_wuzhizhou");
		map.put("4069468482687799490", "tx_area_origin_scenic_tianya");

		map.put("4069283004616630384", "tx_area_origin_business_yingbinlu");
		map.put("4069847118364191869", "tx_area_origin_business_haitangwan");
		map.put("4069282712636644483", "tx_area_origin_business_dadonghai");
		map.put("4069282931181451953", "tx_area_origin_business_shangpinjie");
		map.put("4069281457871158129", "tx_area_origin_business_jiefanglu");
		map.put("4069847934674639643", "tx_area_origin_business_mianshuicheng");
		map.put("4069283497961413502", "tx_area_origin_business_yalongwan");
		map.put("4069469015674903436", "tx_area_origin_business_sanyawan");
		return map;
	}




}
