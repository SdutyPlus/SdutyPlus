package com.d205.domain.usecase.timer

import com.d205.domain.repository.TimerRepository
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodayTotalStudyTimeUsecase @Inject constructor(
    private val timerRepository: TimerRepository
) {
    fun getTodayTotalStudyTime(): Flow<ResultState<String>> {
        return timerRepository.getTodayTotalStudyTime()
    }
}