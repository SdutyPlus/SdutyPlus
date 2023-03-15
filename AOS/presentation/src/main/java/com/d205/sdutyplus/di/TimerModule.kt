package com.d205.sdutyplus.di

import com.d205.data.api.TimerApi
import com.d205.data.dao.TimerSharedPreference
import com.d205.data.repository.timer.TimerRepositoryImpl
import com.d205.data.repository.timer.local.TimerLocalDataSource
import com.d205.data.repository.timer.local.TimerLocalDataSourceImpl
import com.d205.data.repository.timer.remote.TimerRemoteDataSource
import com.d205.data.repository.timer.remote.TimerRemoteDataSourceImpl
import com.d205.domain.repository.TimerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
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