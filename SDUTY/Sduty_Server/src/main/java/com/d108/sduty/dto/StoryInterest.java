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
@IdClass(StoryInterestPK.class)
@Table(name="story_interest_hashtag")
public class StoryInterest {
	@Id
	@Column(name="story_seq")
	private int seq;
	@Id
	@Column(name="story_interest_hashtag")
	private int interestSeq;
}
