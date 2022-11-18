package com.d205.domain.usecase.user

import android.util.Log
import com.d205.domain.model.user.User
import com.d205.domain.repository.UserRepository
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

private const val TAG = "KakaoLoginUseCase"
class KakaoLoginUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(token: String): Flow<ResultState<User>> = flow {
        Log.d(TAG, "invoke: $TAG")
        userRepository.loginKakaoUser(token).collect {
            emit(it)
        }
    }
}