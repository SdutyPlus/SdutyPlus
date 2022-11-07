package com.d205.sdutyplus.view.join

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d205.domain.model.user.User
import com.d205.domain.usecase.user.CheckNicknameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val checkNicknameUseCase: CheckNicknameUseCase
): ViewModel() {
    private val _isUsedNickname = MutableLiveData(false)
    val isUsedNickname: LiveData<Boolean>
        get() = _isUsedNickname

    suspend fun checkNickname(nickname: String): Boolean =
        viewModelScope.async {
            _isUsedNickname.value = checkNicknameUseCase.invoke(nickname)
            _isUsedNickname.value!!
        }.await()
}