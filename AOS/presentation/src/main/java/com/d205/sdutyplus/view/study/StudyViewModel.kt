package com.d205.sdutyplus.view.study

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.study.Study
import com.example.domain.usecase.study.GetStudyListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudyViewModel @Inject constructor(private val getStudyListUseCase : GetStudyListUseCase): ViewModel() {
    private val _studyList = MutableLiveData<List<Study>>()
    val studyList: LiveData<List<Study>>
        get() = _studyList

    fun getStudyList(userSeq: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val studyList = getStudyListUseCase.execute(userSeq)
                _studyList.postValue(studyList)
            }
            catch (_: Exception) {

            }
        }
    }
}