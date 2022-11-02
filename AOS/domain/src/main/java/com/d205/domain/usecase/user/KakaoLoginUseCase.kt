package com.d205.domain.usecase.user

import com.d205.domain.model.user.User
import com.d205.domain.repository.UserRepository
import javax.inject.Inject

class KakaoLoginUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(token: String): User =
        userRepository.loginKakaoUser(token)
}