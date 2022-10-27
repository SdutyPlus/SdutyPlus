package com.example.domain.usecase.user

import com.example.domain.repository.UserRepository

class AddUserUseCase(private val userRepository: UserRepository) {
    fun execute(): Boolean{
        userRepository.addUser(1)
        return true
    }
}