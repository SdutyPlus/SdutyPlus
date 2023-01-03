package com.d205.sdutyplus.view.report

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d205.domain.model.report.Task
import com.d205.domain.model.timer.CurrentTaskDto2
import com.d205.domain.usecase.report.*
import com.d205.domain.usecase.timer.AddTaskUsecase
import com.d205.domain.utils.ResultState
import com.d205.sdutyplus.utills.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "ReportViewModel"

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val getReportUseCase: GetReportUseCase,
    private val getTaskListUseCase: GetTaskListUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val addTaskUseCase: AddTaskUsecase,
    private val getGraphUseCase: GetGraphUseCase
) : ViewModel() {

    private val _totalTime = SingleLiveEvent<String?>()
    val totalTime get() = _totalTime

    private val _percentage = SingleLiveEvent<Int>()
    val percentage get() = _percentage

    private val _remoteTask: MutableStateFlow<ResultState<List<Task>>> =
        MutableStateFlow(ResultState.Uninitialized)
    val remoteTask get() = _remoteTask.asStateFlow()

    private var _taskCheck = SingleLiveEvent<Boolean>()
    val taskCheck get() = _taskCheck

    private var _updateTaskSuccess = MutableLiveData(false)
    val updateTaskSuccess: LiveData<Boolean> get() = _updateTaskSuccess

    private var _deleteTaskSuccess = MutableLiveData(false)
    val deleteTaskSuccess: LiveData<Boolean> get() = _deleteTaskSuccess

    private val _addTaskCallBack = MutableLiveData(0)
    val addTaskCallBack: LiveData<Int> get() = _addTaskCallBack

    @RequiresApi(Build.VERSION_CODES.O)
    fun getReportTotalTime(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getReportUseCase(date).collectLatest {
                if (it is ResultState.Success) {
                    _totalTime.postValue(it.data.totalTime)
                    _percentage.postValue(it.data.percentage)
                }
            }
        }
    }
    fun getTaskList(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getTaskListUseCase(date).collectLatest {
                if (it is ResultState.Success) {
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
            addTaskUseCase(task).collect { isSuccess ->
                if(isSuccess) {
                    _addTaskCallBack.postValue(200)
                }else {
                    _addTaskCallBack.postValue(400)
                }
            }
        }
    }

    fun callBackReset() {
        _addTaskCallBack.value = 0
    }


    private val _continuous = SingleLiveEvent<Int>()
    val continuous get() = _continuous

    private val _studyTime = SingleLiveEvent<Int>()
    val studyTime get() = _studyTime

    private val _dailyTime = SingleLiveEvent<List<Int>>()
    val dailyTime get() = _dailyTime

    fun getGraph() {
        viewModelScope.launch(Dispatchers.IO) {
            getGraphUseCase().collect {
                if(it is ResultState.Success){
                    _continuous.postValue(it.data.continuous)
                    _studyTime.postValue(it.data.studyTime)
                    _dailyTime.postValue(it.data.dailyTimeGraphs)
                }
            }
        }

    }


}