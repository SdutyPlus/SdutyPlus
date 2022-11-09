package com.d205.domain.repository

import com.d205.domain.model.study.Study

interface StudyRepository {
    suspend fun getStudyList(userSeq: Int): List<Study>
}