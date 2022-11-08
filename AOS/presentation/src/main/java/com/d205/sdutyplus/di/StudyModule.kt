package com.d205.sdutyplus.di


import android.content.Context
import com.d205.data.api.StudyApi
import com.d205.data.common.XAccessTokenInterceptor
import com.d205.data.dao.UserSharedPreference
import com.d205.data.repository.study.StudyRepositoryImpl
import com.d205.data.repository.study.local.StudyMockDataSource
import com.d205.data.repository.study.local.StudyMockDataSourceImpl
import com.d205.data.repository.study.remote.StudyRemoteDataSource
import com.d205.data.repository.study.remote.StudyRemoteDataSourceImpl
import com.d205.domain.repository.StudyRepository
import com.d205.sdutyplus.uitls.SERVER_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StudyModule {

    // HttpLoggingInterceptor DI
    @Provides
    @Singleton
    fun provideInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    // Gson DI
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().setLenient().disableHtmlEscaping().create()
    }

    @Provides
    @Singleton
    fun provideXAccessTokenInterceptor(@ApplicationContext context: Context): XAccessTokenInterceptor {
        return XAccessTokenInterceptor(UserSharedPreference(context))
    }

    //OkHttpClient DI
    @Provides
    @Singleton
    fun provideOkHttpClient(xAccessTokenInterceptor: XAccessTokenInterceptor): OkHttpClient =
         OkHttpClient.Builder().addNetworkInterceptor(xAccessTokenInterceptor).build()

    // Retrofit DI
    @Provides
    @Singleton
    fun provideRetrofitInstance(gson: Gson/*, client: OkHttpClient*/): Retrofit = Retrofit.Builder()
        .baseUrl(SERVER_URL)
        //.client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
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