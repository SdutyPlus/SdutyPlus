package com.d108.sduty.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

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
@IdClass(DislikePK.class)
public class Dislike {
	@Id
	@Column(name = "dislike_user_seq")
	private int userSeq;
	@Id
	@Column(name = "dislike_story_seq")
	private int storySeq;

}
