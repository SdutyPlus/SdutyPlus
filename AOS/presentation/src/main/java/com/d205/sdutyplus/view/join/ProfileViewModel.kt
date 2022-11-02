package com.d205.sdutyplus.view.join

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d205.domain.model.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {
    private val _isUsedNickname = MutableLiveData(false)
    val isUsedNickname: LiveData<Boolean>
        get() = _isUsedNickname

    fun checkNickname(nickname: String){
        viewModelScope.launch(Dispatchers.IO){
            _isUsedNickname.postValue(false)
        }
    }
}