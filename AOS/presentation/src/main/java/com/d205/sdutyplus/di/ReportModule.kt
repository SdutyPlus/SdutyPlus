package com.d205.sdutyplus.di

import com.d205.data.api.ReportApi
import com.d205.data.repository.report.ReportRepositoryImpl
import com.d205.data.repository.report.remote.ReportRemoteDataSource
import com.d205.data.repository.report.remote.ReportRemoteDataSourceImpl
import com.d205.domain.repository.ReportRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReportModule {


    @Provides
    @Singleton
    fun provideReportApiService(retrofit: Retrofit): ReportApi =
        retrofit.create(ReportApi::class.java)

    @Provides
    @Singleton
    fun provideReportRemoteDataSource(reportApi: ReportApi):ReportRemoteDataSource {
        return ReportRemoteDataSourceImpl(reportApi)
    }

    @Provides
    @Singleton
    fun provideReportRepository(
        reportRemoteDataSource: ReportRemoteDataSource
    ): ReportRepository {
        return ReportRepositoryImpl(reportRemoteDataSource)
    }

}