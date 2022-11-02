package com.d205.sdutyplus.di

import com.d205.data.api.UserApi
import com.d205.data.repository.user.UserRepositoryImpl
import com.d205.data.repository.user.local.UserMockDataSource
import com.d205.data.repository.user.local.UserMockDataSourceImpl
import com.d205.data.repository.user.remote.UserRemoteDataSource
import com.d205.data.repository.user.remote.UserRemoteDataSourceImpl
import com.d205.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApi =
        retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun provideUserRepository(userRemoteDataSource: UserRemoteDataSource,
                              userMockDataSource: UserMockDataSource): UserRepository =
        UserRepositoryImpl(userRemoteDataSource, userMockDataSource)

    @Provides
    @Singleton
    fun provideUserRemoteDataSource(userApi: UserApi) =
        UserRemoteDataSourceImpl(userApi)

    @Provides
    @Singleton
    fun provideUserMockDataSource() = UserMockDataSourceImpl()
}