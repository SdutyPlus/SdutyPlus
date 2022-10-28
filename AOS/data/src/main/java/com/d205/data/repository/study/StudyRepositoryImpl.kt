package com.d205.data.repository.study

import com.d205.data.mapper.mapperToStudy
import com.d205.data.repository.study.local.StudyMockDataSource
import com.d205.data.repository.study.remote.StudyRemoteDataSource
import com.example.domain.model.study.Study
import com.example.domain.repository.StudyRepository
import javax.inject.Inject

class StudyRepositoryImpl @Inject constructor(
    private val studyRemoteDataSource: StudyRemoteDataSource,
    private val studyMockDataSource: StudyMockDataSource) : StudyRepository {

    override suspend fun getStudyList(userSeq: Int): List<Study> =
        studyRemoteDataSource.getStudyList(userSeq)
            .map {
                mapperToStudy(it)
            }
}