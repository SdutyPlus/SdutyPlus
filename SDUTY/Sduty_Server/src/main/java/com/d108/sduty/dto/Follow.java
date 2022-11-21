package com.d108.sduty.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor	
@IdClass(FollowPK.class)
public class Follow {
	@Id
	@Column(name = "follower_seq")
	private int followerSeq;
	@Id
	@Column(name = "followee_seq")
	private int followeeSeq;
	
	@Transient
	private Profile profile;
	
}
