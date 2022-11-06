package com.d205.domain.usecase.timer

import com.d205.domain.repository.TimerRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class StartTimerUsecase @Inject constructor(
    private val timerRepository: TimerRepository,
    private val saveStartTimeUsecase: SaveStartTimeUsecase,
    private val getCurrentTimeUsecase: GetCurrentTimeUsecase
) {
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default

    suspend operator fun invoke(): Boolean {
        val startTime = getCurrentTimeUsecase() // 서버 통신 성공 시 서버 시간을, 아니면 로컬 시간을 가져 온다.
        return saveStartTimeUsecase(startTime)
    }
}