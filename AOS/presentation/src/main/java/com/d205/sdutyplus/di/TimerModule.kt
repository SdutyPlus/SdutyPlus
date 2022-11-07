package com.d205.sdutyplus.di

import com.d205.data.api.StudyApi
import com.d205.data.api.TimerApi
import com.d205.data.dao.TimerSharedPreference
import com.d205.data.repository.study.StudyRepositoryImpl
import com.d205.data.repository.study.local.StudyMockDataSource
import com.d205.data.repository.study.remote.StudyRemoteDataSource
import com.d205.data.repository.study.remote.StudyRemoteDataSourceImpl
import com.d205.data.repository.timer.TimerRepositoryImpl
import com.d205.data.repository.timer.local.TimerLocalDataSource
import com.d205.data.repository.timer.local.TimerLocalDataSourceImpl
import com.d205.data.repository.timer.remote.TimerRemoteDataSource
import com.d205.data.repository.timer.remote.TimerRemoteDataSourceImpl
import com.d205.domain.repository.StudyRepository
import com.d205.domain.repository.TimerRepository
import com.d205.sdutyplus.uitls.SERVER_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Timer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TimerModule {



    @Provides
    @Singleton
    fun provideTimerRepository(
        timerLocalDataSource: TimerLocalDataSource,
        timerRemoteDataSource: TimerRemoteDataSource
    ): TimerRepository =
        TimerRepositoryImpl(timerLocalDataSource, timerRemoteDataSource)

    @Provides
    @Singleton
    fun provideTimerLocalDataSource(
        timerSharedPreference: TimerSharedPreference
    ): TimerLocalDataSource =
        TimerLocalDataSourceImpl(timerSharedPreference)

    @Provides
    @Singleton
    fun provideTimerRemoteDataSource(
        timerApi: TimerApi
    ): TimerRemoteDataSource =
        TimerRemoteDataSourceImpl(timerApi)

    @Provides
    @Singleton
    fun provideTimerApiService(retrofit: Retrofit): TimerApi =
        retrofit.create(TimerApi::class.java)

}