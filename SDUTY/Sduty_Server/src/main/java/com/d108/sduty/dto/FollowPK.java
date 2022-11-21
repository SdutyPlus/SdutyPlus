package com.d108.sduty.dto;

import java.io.Serializable;

import lombok.Data;

@Data 
public class FollowPK implements Serializable{
	private int followerSeq;
	private int followeeSeq;
}
