package com.d108.sduty.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Achievement {
	private int seq;
	private String name;
	private String content;
	private int isHidden;

	
	@Override
	public String toString() {
		return "Achievement [seq=" + seq + ", name=" + name + ", content=" + content + ", isHidden=" + isHidden + "]";
	}
}
