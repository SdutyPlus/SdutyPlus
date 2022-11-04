package com.d205.domain.usecase.user

import com.d205.domain.model.user.UserDto
import com.d205.domain.repository.UserRepository
import javax.inject.Inject

class AddNaverUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(userDto: UserDto): Boolean =
        userRepository.addNaverUser(user = userDto)
}