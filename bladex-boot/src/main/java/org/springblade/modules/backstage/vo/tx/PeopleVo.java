package org.springblade.modules.backstage.vo.tx;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author yq
 * @Date 2020/11/25 21:26
 */
@Data
public class PeopleVo implements Serializable {

	private Integer people;
	private String typeCode;
}
