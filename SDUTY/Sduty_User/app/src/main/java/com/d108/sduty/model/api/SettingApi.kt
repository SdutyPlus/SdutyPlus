package com.d108.sduty.model.api

import com.d108.sduty.model.dto.Notice
import com.d108.sduty.model.dto.Qna
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SettingApi {
    @GET("/setting/qna/{user_seq}")
    suspend fun getQnaList(@Path("user_seq") userSeq: Int): Response<MutableList<Qna>>

    @POST("setting/qna")
    suspend fun insertQna(@Body qna: Qna): Response<Void>

    @GET("/setting/notice")
    suspend fun getNoticeList(): Response<List<Notice>>
}