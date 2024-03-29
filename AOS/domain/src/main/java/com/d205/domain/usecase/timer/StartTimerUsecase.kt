package com.d205.domain.usecase.timer

import com.d205.domain.repository.TimerRepository
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StartTimerUsecase @Inject constructor(
    private val saveStartTimeUsecase: SaveStartTimeUsecase,
    private val getCurrentTimeUsecase: GetCurrentTimeUsecase
) {
    suspend operator fun invoke(): Flow<ResultState<Boolean>> {
        val startTime = getCurrentTimeUsecase() // 서버 통신 성공 시 서버 시간을, 아니면 로컬 시간을 가져 온다.
        return saveStartTimeUsecase(startTime)
    }
}