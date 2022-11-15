package com.d108.sduty.ui.main.mypage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContributionViewModel: ViewModel() {
    private val _isContributed = MutableLiveData<Boolean>()
    val isContributed: LiveData<Boolean>
        get() = _isContributed
    fun setContributionItem(boolean: Boolean){
        _isContributed.value = boolean
    }
}