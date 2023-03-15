package com.d205.sdutyplus.domain.task.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ReportDateResponseDto {
    private final List<String> dates;
}
