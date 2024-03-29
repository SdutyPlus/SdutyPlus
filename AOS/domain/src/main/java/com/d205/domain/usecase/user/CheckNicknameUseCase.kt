package com.d205.domain.usecase.user

import com.d205.domain.repository.UserRepository
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckNicknameUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(nickname: String): Flow<ResultState<Boolean>> =
        userRepository.checkNickname(nickname)
}