package com.d205.domain.usecase.user

import com.d205.domain.repository.UserRepository
import javax.inject.Inject

class CheckNicknameUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(nickname: String): Boolean =
        userRepository.checkNickname(nickname)
}