package com.d108.sduty.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Image {
	@Id
	@Column(name="image_id")
	String id;
	@Column(name="image_name")
	String name;
	@Column(name="image_url")
	String fileurl;
}
