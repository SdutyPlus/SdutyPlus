package com.d108.sduty.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagingResult<T> {
	private int page;
	private int totalPage;
	private List<T> result;
}
