package com.d205.domain.usecase.user

import com.d205.domain.repository.UserRepository

class AddUserUseCase(private val userRepository: UserRepository) {
    fun execute(): Boolean{
        userRepository.addUser(1)
        return true
    }
}