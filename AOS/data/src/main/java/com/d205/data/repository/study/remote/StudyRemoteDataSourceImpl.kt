package com.d205.data.repository.study.remote

import com.d205.data.api.StudyApi
import com.d205.data.mapper.mapperToStudy
import com.d205.data.model.study.StudyResponse
import retrofit2.Response
import javax.inject.Inject

class StudyRemoteDataSourceImpl @Inject constructor(private val studyApi: StudyApi)
    : StudyRemoteDataSource {
    override suspend fun getStudyList(userSeq: Int): List<StudyResponse> {
        val studyResponse = studyApi.getStudyList(userSeq)
        if(checkResponse(studyResponse))
            return studyResponse.body()!!
        return emptyList()
    }

    fun checkResponse(response: Response<List<StudyResponse>>): Boolean {
        if(response.isSuccessful && response.body() != null)
            return true
        return false
    }
}