package com.d108.sduty.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Service
@NoArgsConstructor
@AllArgsConstructor
@Table(name="identification")
public class AuthInfo {
	
	@Id
	private int id;
	private String tel;
	private String code;
	private Date expire;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AuthInfo [id=");
		builder.append(id);
		builder.append(", tel=");
		builder.append(tel);
		builder.append(", code=");
		builder.append(code);
		builder.append(", expire=");
		builder.append(expire);
		builder.append("]");
		return builder.toString();
	}
	
	
}