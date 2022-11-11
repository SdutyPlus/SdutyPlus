package com.d205.sdutyplus.di

import com.d205.data.api.FeedApi
import com.d205.data.api.UserApi
import com.d205.data.repository.feed.FeedRepositoryImpl
import com.d205.data.repository.feed.local.FeedLocalDataSource
import com.d205.data.repository.feed.local.FeedLocalDataSourceImpl
import com.d205.data.repository.feed.remote.FeedRemoteDataSource
import com.d205.data.repository.feed.remote.FeedRemoteDataSourceImpl
import com.d205.data.repository.user.UserRepositoryImpl
import com.d205.data.repository.user.local.UserLocalDataSource
import com.d205.data.repository.user.local.UserMockDataSource
import com.d205.data.repository.user.remote.UserRemoteDataSource
import com.d205.data.repository.user.remote.UserRemoteDataSourceImpl
import com.d205.domain.repository.UserRepository
import com.d205.domain.repository.paging.FeedRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FeedModule {

    @Provides
    @Singleton
    fun provideFeedApiService(retrofit: Retrofit): FeedApi =
        retrofit.create(FeedApi::class.java)

    @Provides
    @Singleton
    fun provideFeedRepository(
        feedRemoteDataSource: FeedRemoteDataSource,
        feedLocalDataSource: FeedLocalDataSource
    ): FeedRepository =
        FeedRepositoryImpl(feedRemoteDataSource,feedLocalDataSource)

    @Provides
    @Singleton
    fun provideFeedRemoteDataSource(feedApi: FeedApi): FeedRemoteDataSource =
        FeedRemoteDataSourceImpl(feedApi)

    @Provides
    @Singleton
    fun provideFeedLocalDataSource(feedApi: FeedApi): FeedLocalDataSource =
        FeedLocalDataSourceImpl()
}