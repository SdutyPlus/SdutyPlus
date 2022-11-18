package com.d108.sduty.model.api

import com.d108.sduty.model.dto.Study
import retrofit2.Response
import retrofit2.http.*
import java.util.*

interface StudyApi {
    // 스터디 등록
    @POST("/study")
    suspend fun studyCreate(@Body study: Map<String, Study>): Response<Void>

    // 캠 스터디 등록
    @POST("/study")
    suspend fun camStudyCreate(@Body study: Map<String, Objects>): Response<Void>

    // 스터디명 중복 검사
    @GET("/study/check/{study_name}")
    suspend fun getStudyName(@Path("study_name")studyName: String): Response<Void>

    // 스터디 전체 조회
    @GET("/study")
    suspend fun studyList(): Response<List<Study>>

    // 내 스터디 상세정보
    @GET("/study/{user_seq}/{study_seq}")
    suspend fun getMyStudyInfo(@Path("user_seq")userSeq: Int, @Path("study_seq")studySeq: Int): Response<Map<String, Any>>

    // 내 스터디 목록
    @GET("/study/{user_seq}")
    suspend fun myStudyList(@Path("user_seq")userSeq: Int): Response<List<Study>>

    // 스터디 필터링
    @GET("study/filter/{category}/{emptyfilter}/{camfilter}/{publicfilter}")
    suspend fun studyFilter(@Path("category")category: String, @Path("emptyfilter")emptyFilter: Boolean,
                            @Path("camfilter")camFilter: Boolean, @Path("publicfilter")publicFilter: Boolean): Response<List<Study>>

    // 스터디 검색
    @GET("/study/filter/{keyword}")
    suspend fun studySearch(@Path("keyword")keyword: String) : Response<List<Study>>

    // 스터디 탈퇴
    @DELETE("/study/participation/{study_seq}/{user_seq}")
    suspend fun studyQuit(@Path("study_seq")studySeq: Int, @Path("user_seq")userSeq: Int): Response<Void>

    // 스터디 삭제
    @DELETE("/study/{user_seq}/{study_seq}")
    suspend fun studyDelete(@Path("user_seq")userSeq: Int, @Path("study_seq")studySeq: Int): Response<Void>

    // 스터디 참여
    @POST("study/participation/{study_seq}/{user_seq}")
    suspend fun studyJoin(@Path("study_seq") studySeq: Int, @Path("user_seq") userSeq: Int): Response<Void>

    // 스터디 수정
    @PUT("study/{user_seq}/{study_seq}")
    suspend fun studyUpdate(@Path("user_seq")userSeq: Int, @Path("study_seq")studySeq: Int, @Body study: Study): Response<Study>

    // 스터디 상세조회
    @GET("study/detail/{study_seq}")
    suspend fun studyDetail(@Path("study_seq")studySeq: Int): Response<Study>


}