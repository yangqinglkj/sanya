package org.springblade.common.entity.mongo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class MongoJSON {
	private String id;
	private Date addTime;
	private String apiName;
	private String postUrl;
	private String postParam;
	private JSONObject jsonObject;
}
