package com.d205.data.api

import com.d205.data.model.study.StudyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface StudyApi {
    @GET("/study/{user_seq}")
    suspend fun getStudyList(@Path("user_seq")userSeq: Int): Response<List<StudyResponse>>
}