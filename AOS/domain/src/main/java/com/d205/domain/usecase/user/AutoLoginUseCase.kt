package com.d205.domain.usecase.user

import com.d205.domain.repository.UserRepository
import javax.inject.Inject

class AutoLoginUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(token: String) = userRepository.checkJwt(token)
}