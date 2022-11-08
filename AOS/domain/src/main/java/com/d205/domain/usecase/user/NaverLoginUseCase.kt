package com.d205.domain.usecase.user

import android.util.Log
import com.d205.domain.model.user.User
import com.d205.domain.repository.UserRepository
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val TAG = "NaverLoginUseCase"
class NaverLoginUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(token: String): Flow<ResultState<User>> {
        Log.d(TAG, "invoke: ")
        return userRepository.loginNaverUser(token)
    }
}