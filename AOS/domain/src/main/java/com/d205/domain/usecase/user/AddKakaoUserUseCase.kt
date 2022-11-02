package com.d205.domain.usecase.user

import com.d205.domain.model.user.UserDto
import com.d205.domain.repository.UserRepository

class AddKakaoUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(user: UserDto): Boolean = userRepository.addKakaoUser(user)
}