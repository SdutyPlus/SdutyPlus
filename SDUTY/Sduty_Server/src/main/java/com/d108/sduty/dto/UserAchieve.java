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
public class UserAchieve {
	private int userSeq;
	private int achievementSeq;

	@Override
	public String toString() {
		return "Achievement [userSeq=" + userSeq + ", achievementSeq=" + achievementSeq + "]";
	}
}
