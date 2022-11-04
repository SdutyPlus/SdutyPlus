package com.d205.sdutyplus.di

import com.d205.data.api.StudyApi
import com.d205.data.repository.study.StudyRepositoryImpl
import com.d205.data.repository.study.local.StudyMockDataSource
import com.d205.data.repository.study.local.StudyMockDataSourceImpl
import com.d205.data.repository.study.remote.StudyRemoteDataSource
import com.d205.data.repository.study.remote.StudyRemoteDataSourceImpl
import com.d205.domain.repository.StudyRepository
import com.d205.sdutyplus.di.ApplicationClass.Companion.userPrefs
import com.d205.sdutyplus.uitls.SERVER_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StudyModule {

    @Provides
    @Singleton
    fun provideRetrofitInstance(gson: Gson, interceptor: Interceptor): Retrofit = Retrofit.Builder()
        .baseUrl(SERVER_URL)
        .client(OkHttpClient.Builder().addInterceptor(interceptor).build())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().disableHtmlEscaping().create()
    }

    @Provides
    @Singleton
    fun provideHeaderInterceptor(): Interceptor = Interceptor {
        chain -> chain.run {
            proceed(
                request()
                    .newBuilder()
                    .addHeader("Authorization Bearer", userPrefs.getString("jwt", "")!!)
                    .build()
            )
        }
    }

    @Provides
    @Singleton
    fun provideStudyApiService(retrofit: Retrofit): StudyApi =
        retrofit.create(StudyApi::class.java)

    @Provides
    @Singleton
    fun provideStudyRepository(studyRemoteDataSource: StudyRemoteDataSource,
        studyMockDataSource: StudyMockDataSource) : StudyRepository =
        StudyRepositoryImpl(studyRemoteDataSource, studyMockDataSource)

    @Provides
    @Singleton
    fun provideStudyRemoteDataSource(studyApi: StudyApi) : StudyRemoteDataSource =
        StudyRemoteDataSourceImpl(studyApi)

    @Provides
    @Singleton
    fun provideStudyMockDataSource() : StudyMockDataSource =
        StudyMockDataSourceImpl()
}