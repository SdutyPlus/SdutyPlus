package com.d205.domain.usecase.timer

import com.d205.domain.model.timer.CurrentTaskDto
import com.d205.domain.repository.TimerRepository
import javax.inject.Inject

class AddTaskUsecase @Inject constructor(
    private val timerRepository: TimerRepository
) {
    operator fun invoke(currentTask: CurrentTaskDto) {

    }
}