package org.springblade.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author yq
 * @Date 2020/9/22 13:58
 */

@Data
public class HeatingValueVO implements Serializable {
	private List<Double> coord;
	private Integer elevation;
}
