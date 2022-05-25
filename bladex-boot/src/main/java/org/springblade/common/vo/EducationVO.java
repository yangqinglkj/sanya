package org.springblade.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @Author yq
 * @Date 2020/9/21 17:08
 */

@Data
public class EducationVO implements Serializable {
	private String name;
	private Integer max;
	public Integer people;

}
