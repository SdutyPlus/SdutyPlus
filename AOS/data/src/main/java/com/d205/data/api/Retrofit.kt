package com.d205.data.api

import com.google.gson.GsonBuilder
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit {
    const val SERVER_URL = "https://d108.kro.kr:8090"

    private val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(SERVER_URL)
        .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()

    val studyApi: StudyApi by lazy {
        retrofit.create(StudyApi::class.java)
    }

    val userApi: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }
}