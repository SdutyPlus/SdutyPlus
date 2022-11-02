package com.d205.sdutyplus.di

import com.d205.data.api.StudyApi
import com.d205.data.dao.TimerSharedPreference
import com.d205.data.repository.study.StudyRepositoryImpl
import com.d205.data.repository.study.local.StudyMockDataSource
import com.d205.data.repository.study.remote.StudyRemoteDataSource
import com.d205.data.repository.study.remote.StudyRemoteDataSourceImpl
import com.d205.data.repository.timer.TimerRepositoryImpl
import com.d205.data.repository.timer.local.TimerLocalDataSource
import com.d205.data.repository.timer.local.TimerLocalDataSourceImpl
import com.d205.domain.repository.StudyRepository
import com.d205.domain.repository.TimerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TimerModule {

    @Provides
    @Singleton
    fun provideTimerRepository(
        timerLocalDataSource: TimerLocalDataSource
    ) : TimerRepository = TimerRepositoryImpl(timerLocalDataSource)

    @Provides
    @Singleton
    fun provideTimerLocalDataSource(
        timerSharedPreference: TimerSharedPreference
    ) : TimerLocalDataSource =
        TimerLocalDataSourceImpl(timerSharedPreference)
}