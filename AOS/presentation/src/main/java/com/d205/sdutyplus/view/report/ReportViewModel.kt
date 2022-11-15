package com.d205.sdutyplus.view.report

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d205.domain.model.report.Task
import com.d205.domain.model.timer.CurrentTaskDto2
import com.d205.domain.usecase.report.DeleteTaskUseCase
import com.d205.domain.usecase.report.GetReportUseCase
import com.d205.domain.usecase.report.GetTaskListUseCase
import com.d205.domain.usecase.report.UpdateTaskUseCase
import com.d205.domain.usecase.timer.AddTaskUsecase
import com.d205.domain.utils.ResultState
import com.d205.sdutyplus.uitls.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

const val TAG = "ReportViewModel"

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val getReportUseCase: GetReportUseCase,
    private val getTaskListUseCase: GetTaskListUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val addTaskUseCase: AddTaskUsecase
) : ViewModel() {

    private val _totalTime = SingleLiveEvent<String?>()
    val totalTime get() = _totalTime

    private val _percentage = SingleLiveEvent<String?>()
    val percentage get() = _percentage

    private val _remoteTask: MutableStateFlow<ResultState<List<Task>>> =
        MutableStateFlow(ResultState.Uninitialized)
    val remoteTask get() = _remoteTask.asStateFlow()

    private var _taskCheck = SingleLiveEvent<Boolean>()
    val taskCheck get() = _taskCheck

    private var _updateTaskSuccess = MutableLiveData<Boolean>(false)
    val updateTaskSuccess: LiveData<Boolean> get() = _updateTaskSuccess

    private var _deleteTaskSuccess = MutableLiveData<Boolean>(false)
    val deleteTaskSuccess: LiveData<Boolean> get() = _deleteTaskSuccess

    private val _addTaskCallBack = MutableLiveData<Int>(0)
    val addTaskCallBack: LiveData<Int> get() = _addTaskCallBack

    @RequiresApi(Build.VERSION_CODES.O)
    fun getReportTotalTime(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getReportUseCase(date).collectLatest {
                if (it is ResultState.Success) {
                    _totalTime.postValue(it.data)
                    //Log.d(TAG, "getReportTotalTime: ${it.data}")
                    //Log.d(TAG, "getReportTotalTime: ${LocalDateTime.parse(it.data, DateTimeFormatter.ofPattern("HH:mm:ss"))}")
                    //Log.d(TAG, "getReportTotalTime: ${LocalDateTime.parse("02:00:00", DateTimeFormatter.ofPattern("HH:mm:ss"))}")
                }
            }
        }
    }

    fun getTaskList(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getTaskListUseCase(date).collectLatest {
                if (it is ResultState.Success) {
                    Log.d(TAG, "getTaskList: $it")
                    _remoteTask.value = it
                    if (it.data.isEmpty()) {
                        _taskCheck.postValue(false)
                    } else {
                        _taskCheck.postValue(true)
                    }
                }
            }
        }
    }

    fun updateTask(task_seq: Long, task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            updateTaskUseCase(task_seq, task).collect {
                if(it){
                    _updateTaskSuccess.postValue(true)
                } else {
                    _updateTaskSuccess.postValue(false)
                }
            }
        }
    }

    fun deleteTask(task_seq: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteTaskUseCase(task_seq).collectLatest {
                if(it is ResultState.Success){
                    if(it.data){
                        _deleteTaskSuccess.postValue(true)
                    } else{
                        _deleteTaskSuccess.postValue(false)
                    }
                }

            }
        }
    }

    fun addTask(task: CurrentTaskDto2) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "addTask: ${task}")
            addTaskUseCase(task).collect { isSuccess ->
                Log.d(TAG, "addTask: ${isSuccess}")
                if(isSuccess) {
                    _addTaskCallBack.postValue(200)
                }else {
                    _addTaskCallBack.postValue(400)
                }
            }
        }
    }


}