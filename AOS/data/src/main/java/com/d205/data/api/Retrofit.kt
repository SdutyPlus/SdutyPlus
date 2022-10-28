package com.d205.data.api

import com.google.gson.GsonBuilder
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit {
    const val SERVER_URL = "https://d108.kro.kr:8090"
    private val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(SERVER_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()

    val studyApi: StudyApi by lazy {
        retrofit.create(StudyApi::class.java)
    }
}