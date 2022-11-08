package com.d205.sdutyplus.view.join

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d205.domain.usecase.user.CheckNicknameUseCase
import com.d205.domain.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val checkNicknameUseCase: CheckNicknameUseCase
): ViewModel() {
    private val _isUsedNickname : MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val isUsedNickname
        get() = _isUsedNickname.asStateFlow()

    suspend fun checkNickname(nickname: String): Boolean {
        withContext(viewModelScope.coroutineContext) {
            checkNicknameUseCase.invoke(nickname).collect {
                if(it is ResultState.Success) {
                    _isUsedNickname.value = it.data
                }
            }
        }
        return _isUsedNickname.value
    }

}