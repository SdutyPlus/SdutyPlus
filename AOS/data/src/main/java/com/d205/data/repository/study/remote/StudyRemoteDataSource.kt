package com.d205.data.repository.study.remote

import com.d205.data.model.study.StudyResponse
import retrofit2.Response

interface StudyRemoteDataSource {
    suspend fun getStudyList(userSeq: Int): List<StudyResponse>
}