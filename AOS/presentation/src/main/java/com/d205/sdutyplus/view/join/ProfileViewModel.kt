package com.d205.sdutyplus.view.join

import android.util.Log
import androidx.lifecycle.ViewModel
import com.d205.domain.usecase.user.CheckNicknameUseCase
import com.d205.domain.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

private const val TAG = "ProfileViewModel"

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val checkNicknameUseCase: CheckNicknameUseCase
): ViewModel() {
    private val _canUseNickname : MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val canUseNickname
        get() = _canUseNickname.asStateFlow()

    // 닉네임 중복 체크
    suspend fun checkNickname(nickname: String): Boolean {
        checkNicknameUseCase.invoke(nickname).collect {
            if(it is ResultState.Success) {
                _canUseNickname.value = it.data
            }
        }
        return _canUseNickname.value
    }

}