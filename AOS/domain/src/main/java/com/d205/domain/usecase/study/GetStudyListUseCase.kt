package com.d205.domain.usecase.study

import com.d205.domain.model.study.Study
import com.d205.domain.repository.StudyRepository
import javax.inject.Inject

class GetStudyListUseCase @Inject constructor(private val studyRepository: StudyRepository) {
    suspend fun execute(userSeq: Int): List<Study> = studyRepository.getStudyList(userSeq)
}