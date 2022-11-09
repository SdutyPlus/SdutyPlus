package com.d205.domain.usecase.timer

import com.d205.domain.repository.TimerRepository
import javax.inject.Inject

class UpdateStudyElapsedTimeUsecase @Inject constructor(
    private val timerRepository: TimerRepository
){
    suspend operator fun invoke(studyTime: Int) {
        timerRepository.updateStudyElapsedTime(studyTime)

    }
}