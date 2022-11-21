package com.d108.sduty.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // getter, setter, toString 등
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Study: 스터디 정보", description = "스터디 이름, 소개, 인원 등의 정보")
@Entity
public class Study {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@JsonProperty(access=JsonProperty.Access.READ_ONLY)
	@Column(name = "study_seq")
	private Integer seq;
	@Column(name = "study_master_seq")
	private int masterSeq;
	@OneToOne(optional = true)
	@JoinColumn(name="study_alarm_seq")
	private Alarm alarm;
	@Column(name = "study_name")
	private String name;
	@Column(name = "study_introduce")
	private String introduce;
	@Column(name = "study_category")
	private String category;
	@Column(name = "study_limit_Number")
	private int limitNumber;
	@JsonProperty(access=JsonProperty.Access.READ_ONLY)
	@Column(name = "study_join_Number")
	private int joinNumber;
	@Column(name = "study_password")
	private String password;
	@Column(name = "study_room_id")
	private String roomId;
	@Column(name = "study_regtime")
	@CreationTimestamp
	private LocalDateTime regtime;
	@Column(name = "study_notice")
	private String notice;
	
	@Transient
	@JsonProperty(access=JsonProperty.Access.READ_ONLY)
	private String masterNickname;
	@Transient
	@JsonProperty(access=JsonProperty.Access.READ_ONLY)
	private String masterJob;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "participation", joinColumns = @JoinColumn(name = "participation_study_seq"), inverseJoinColumns = @JoinColumn(name = "participation_user_seq"))
	@JsonBackReference(value="participants")
	@JsonProperty(access=JsonProperty.Access.READ_ONLY)
	private Set<User> participants = new HashSet<User>();
//	
//	@ManyToMany(fetch = FetchType.EAGER)
//	@JoinTable(name = "category", joinColumns = @JoinColumn(name = "category_study_seq"), inverseJoinColumns = @JoinColumn(name = "category_interest_hashtag_seq"))
//	//@JsonBackReference(value="categories")
//	private Set<InterestHashtag> categories = new HashSet<>();

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Study other = (Study) obj;
		if (masterSeq != other.masterSeq)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (seq == null) {
			if (other.seq != null)
				return false;
		} else if (!seq.equals(other.seq))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + masterSeq;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((seq == null) ? 0 : seq.hashCode());
		return result;
	}

}
