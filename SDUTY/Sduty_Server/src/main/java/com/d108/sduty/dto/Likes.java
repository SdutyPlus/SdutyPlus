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
@IdClass(LikesPK.class)
public class Likes {
	@Id
	@Column(name="likes_user_seq")
	private int userSeq;
	@Id
	@Column(name="likes_story_seq")
	private int storySeq;
}
