package com.d205.domain.usecase.timer

import com.d205.domain.repository.TimerRepository
import javax.inject.Inject

class GetStudyTimeInfoUsecase @Inject constructor(
    private val getCurrentTimeUsecase: GetCurrentTimeUsecase,
    private val timerRepository: TimerRepository
){
    operator fun invoke() {

    }
}