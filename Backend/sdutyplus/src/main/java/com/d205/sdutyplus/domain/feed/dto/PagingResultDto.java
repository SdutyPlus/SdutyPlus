package com.d205.sdutyplus.domain.feed.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class PagingResultDto<T> {
    private int page;
    private int totalPage;
    private List<T> result;
}
