package com.d205.domain.usecase.timer

import com.d205.domain.repository.TimerRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCurrentTimeUsecase @Inject constructor(
    private val timerRepository: TimerRepository
) {
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default

    suspend operator fun invoke(): String = withContext(defaultDispatcher) {
        timerRepository.getCurrentTime() // 서버 통신 성공 시 서버 시간을, 아니면 로컬 시간을 가져온다.
    }
}