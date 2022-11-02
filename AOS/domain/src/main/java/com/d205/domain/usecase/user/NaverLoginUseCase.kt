package com.d205.domain.usecase.user

import com.d205.domain.repository.UserRepository
import javax.inject.Inject

class NaverLoginUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(token: String) =
        userRepository.loginNaverUser(token)
}