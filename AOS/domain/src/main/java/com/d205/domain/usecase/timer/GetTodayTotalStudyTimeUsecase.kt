package com.d205.domain.usecase.timer

import com.d205.domain.repository.TimerRepository
import javax.inject.Inject

class GetTodayTotalStudyTimeUsecase @Inject constructor(
    private val timerRepository: TimerRepository
) {
    suspend fun getTodayTotalStudyTime(): String {
        return timerRepository.getTodayTotalStudyTime()
    }
}