package com.d205.sdutyplus.di

import com.d205.data.api.StudyApi
import com.d205.data.repository.study.StudyRepositoryImpl
import com.d205.data.repository.study.local.StudyMockDataSource
import com.d205.data.repository.study.local.StudyMockDataSourceImpl
import com.d205.data.repository.study.remote.StudyRemoteDataSource
import com.d205.data.repository.study.remote.StudyRemoteDataSourceImpl
import com.d205.sdutyplus.uitls.BASE_URL
import com.example.domain.repository.StudyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StudyModule {

    @Provides
    @Singleton
    fun provideRetrofitInstance(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

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