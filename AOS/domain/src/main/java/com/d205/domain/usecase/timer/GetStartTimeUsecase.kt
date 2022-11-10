package com.d205.domain.usecase.timer

import com.d205.domain.repository.TimerRepository
import javax.inject.Inject

class GetStartTimeUsecase @Inject constructor(
    private val timerRepository: TimerRepository
) {
    operator fun invoke(): String {
        return timerRepository.getStartTime()
    }
}