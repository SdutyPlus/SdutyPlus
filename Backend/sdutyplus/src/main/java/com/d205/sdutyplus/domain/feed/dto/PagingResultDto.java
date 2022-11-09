package com.d205.sdutyplus.domain.feed.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagingResultDto<T> {
    private int page;
    private int totalPage;
    private List<T> result;

    @Builder
    public PagingResultDto (int page, int totalPage, List<T> result){
        this.page = page;
        this.totalPage = totalPage;
        this.result = result;
    }
}
