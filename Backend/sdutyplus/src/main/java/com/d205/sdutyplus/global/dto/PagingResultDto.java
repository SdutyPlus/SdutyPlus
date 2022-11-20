package com.d205.sdutyplus.global.dto;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class PagingResultDto<T> {
    private int page;
    private int totalPage;
    private List<T> result;
}
