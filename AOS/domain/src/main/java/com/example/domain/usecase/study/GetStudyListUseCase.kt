package com.example.domain.usecase.study

import com.example.domain.model.study.Study
import com.example.domain.repository.StudyRepository
import javax.inject.Inject

class GetStudyListUseCase @Inject constructor(private val studyRepository: StudyRepository) {
    suspend fun execute(userSeq: Int): List<Study> = studyRepository.getStudyList(userSeq)
}