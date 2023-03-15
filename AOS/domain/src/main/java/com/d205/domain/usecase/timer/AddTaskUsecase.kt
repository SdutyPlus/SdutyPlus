package com.d205.domain.usecase.timer

import com.d205.domain.model.timer.CurrentTaskDto
import com.d205.domain.model.timer.CurrentTaskDto2
import com.d205.domain.repository.TimerRepository
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddTaskUsecase @Inject constructor(
    private val timerRepository: TimerRepository
) {
    operator fun invoke(currentTask: CurrentTaskDto2): Flow<ResultState<Boolean>> = flow {
        timerRepository.addTask(currentTask).collect{
            if(it is ResultState.Success) {
                emit(it)
            } else if (it is ResultState.Loading){
                emit(it)
            }
            else {
                emit(it)
            }
        }

    }

}