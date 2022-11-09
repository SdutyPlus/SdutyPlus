package com.d205.domain.usecase.timer

import com.d205.domain.repository.TimerRepository
import javax.inject.Inject

class GetElapsedTimeUsecase @Inject constructor(
    private val timerRepository: TimerRepository
) {
    operator fun invoke(): Int {
        return timerRepository.getElapsedTime()
    }
}