package com.d108.sduty.model.api

import com.d108.sduty.model.dto.InterestHashtag
import com.d108.sduty.model.dto.JobHashtag
import retrofit2.Response
import retrofit2.http.GET

interface TagApi {
    @GET("/tag/job")
    suspend fun getJobList(): Response<List<JobHashtag>>

    @GET("/tag/interest")
    suspend fun getInterestList(): Response<List<InterestHashtag>>
}