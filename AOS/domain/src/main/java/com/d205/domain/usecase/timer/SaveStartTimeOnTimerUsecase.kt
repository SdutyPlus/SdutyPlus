package com.d205.domain.usecase.timer

import com.d205.domain.repository.TimerRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveStartTimeOnTimerUsecase @Inject constructor(
    private val timerRepository: TimerRepository
) {
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default

    suspend operator  fun invoke(startTime: String) = withContext(defaultDispatcher) {
            timerRepository.saveStartTimeOnTimer(startTime)
    }
}