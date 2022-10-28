package com.example.domain.repository

import com.example.domain.model.study.Study

interface StudyRepository {
    suspend fun getStudyList(userSeq: Int): List<Study>
}