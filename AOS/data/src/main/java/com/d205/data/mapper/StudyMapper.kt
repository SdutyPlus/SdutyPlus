package com.d205.data.mapper

import com.d205.data.model.study.StudyResponse
import com.example.domain.model.study.Study

fun mapperToStudy(studyResponse: StudyResponse): Study {
    return Study(
        studyResponse.seq,
        studyResponse.masterSeq,
        studyResponse.alarm,
        studyResponse.name,
        studyResponse.introduce,
        studyResponse.category,
        studyResponse.limitNumber,
        studyResponse.joinNumber,
        studyResponse.password,
        studyResponse.studyRegtime,
        studyResponse.notice,
        studyResponse.roomId,
        studyResponse.masterNickname,
        studyResponse.masterJob
    )
}