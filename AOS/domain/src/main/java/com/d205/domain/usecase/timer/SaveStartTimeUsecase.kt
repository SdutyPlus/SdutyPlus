package com.d205.domain.usecase.timer

import com.d205.domain.repository.TimerRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveStartTimeUsecase @Inject constructor(
    private val timerRepository: TimerRepository
) {
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default

    operator fun invoke(startTime: String) = timerRepository.saveStartTime(startTime)

}