package com.d108.sduty.dto;

import java.io.Serializable;

import lombok.Data;

@Data 
public class LikesPK implements Serializable {
	private int userSeq;
	private int storySeq;
}