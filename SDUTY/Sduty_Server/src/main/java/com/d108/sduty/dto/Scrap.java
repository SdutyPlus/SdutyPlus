package com.d108.sduty.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ScrapPK.class)
public class Scrap {
	@Id
	@Column(name="scrap_user_seq")
	private int userSeq;
	@Id
	@Column(name="scrap_story_seq")
	private int storySeq;
}
