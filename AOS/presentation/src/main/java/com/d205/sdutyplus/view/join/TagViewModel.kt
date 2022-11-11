package com.d205.sdutyplus.view.join

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d205.domain.model.common.JobHashtag
import com.d205.sdutyplus.uitls.jobs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG ="TagViewModel"
class TagViewModel: ViewModel() {
    private val _jobList = MutableLiveData<MutableList<JobHashtag>>()
    val jobList: LiveData<MutableList<JobHashtag>>
        get() = _jobList

    fun getJobListValue(){
        _jobList.value = jobs
    }

    private val _jobTagMap = MutableLiveData<HashMap<String, Int>>()
    val jobTagMap: LiveData<HashMap<String, Int>>
        get() = _jobTagMap

    private fun setTagMap(list: MutableList<JobHashtag>){
        val map = hashMapOf<String, Int>()
        for(item in list) {
            map[item.name] = item.seq
        }
        _jobTagMap.postValue(map)
    }

    private val _jobSelectVisible = MutableLiveData<Boolean>(true)
    val jobSelectVisible: LiveData<Boolean>
        get() = _jobSelectVisible
    fun setJobVisible(){
        _jobSelectVisible.postValue(!jobSelectVisible.value!!)
    }

}