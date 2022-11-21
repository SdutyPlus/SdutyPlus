package com.d108.sduty.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class Story {
	@Id
	@Column(name="story_seq", updatable = false)
	private int seq;
	@Column(name="story_writer_seq", updatable = false)
	private int writerSeq;
	@Column(name="story_image_source", updatable = false)
	private String imageSource;
	@Column(name="story_thumbnail", updatable = false)
	private String thumbnail;
	@Column(name="story_job_hashtag")
	private int jobHashtag;
	@Column(name="story_contents")
	private String contents;
	@Column(name="story_regtime", updatable = false)
	private Date regtime;
	@Column(name="story_public")
	private int storyPublic;
	@Column(name="story_warning")
	private int warning;
	@Transient
	private List<Integer> interestHashtag;
	
}
