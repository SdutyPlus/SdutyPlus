package com.d108.sduty.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

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
public class Profile {
	
	@Id
	@Column(name="profile_user_seq", insertable = false, updatable = false)
	private int userSeq;
	@Column(name="profile_nickname")
	private String nickname;
	@JsonProperty
	@JsonFormat(pattern = "MMM dd, yyyy hh:mm:ss a",locale = "en_US")
	@Column(name="profile_birthday")
	private Date birthday;
	@Column(name="profile_public_birthday")
	private int publicBirthday;
	@Column(name="profile_short_introduce")
	private String shortIntroduce;
	@Column(name="profile_image")
	private String image;
	@Column(name="profile_job")
	private String job;
	@Column(name="profile_public_job")
	private int publicJob;
	@Column(name="profile_interest")
	private int interest;
	@Column(name="profile_public_interest")
	private int publicInterest;
	@Column(name="profile_followers")
	private int followers;
	@Column(name="profile_followees")
	private int followees;
	@Column(name="profile_main_achievement_seq")
	private int mainAchievmentSeq;
	@Column(name="profile_block_action")
	private int blockAction;
	@Column(name="profile_warning")
	private int warning;
	@Column(name="is_prohibited_user")
	private int isProhibitedUser;
	@Column(name="is_studying")
	private int isStudying;
	
	@Transient
	private List<Integer> interestHashtagSeqs;
	@Transient
	private List<InterestHashtag> interestHashtags;
	
	@Transient
	private int cntStory;
	
	@MapKey(name = "followeeSeq")	
	
	@OneToMany(mappedBy = "followerSeq", fetch = FetchType.EAGER)	
	private Map<String, Follow> follows;
}
