package com.d108.sduty.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserInterestPK.class)
@Table(name="user_interest_hashtag")
public class UserInterest {
	@Id
	@Column(name="user_seq")
	private int userSeq;
	@Id
	@Column(name="user_interest_hashtag")
	private int interestSeq;
}
