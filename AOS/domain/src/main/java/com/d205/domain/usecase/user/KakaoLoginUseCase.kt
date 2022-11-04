package com.d205.domain.usecase.user

import android.util.Log
import com.d205.domain.model.user.User
import com.d205.domain.repository.UserRepository
import javax.inject.Inject

private const val TAG = "KakaoLoginUseCase"
class KakaoLoginUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(token: String): User {
        Log.d(TAG, "invoke: $TAG")
        return userRepository.loginKakaoUser(token)
    }

}