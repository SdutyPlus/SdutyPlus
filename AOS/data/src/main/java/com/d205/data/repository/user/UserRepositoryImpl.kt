package com.d205.data.repository.user

import android.util.Log
import com.d205.data.repository.user.local.UserMockDataSource
import com.d205.data.repository.user.remote.UserRemoteDataSource
import com.d205.domain.model.user.UserDto
import com.d205.domain.repository.UserRepository
import com.skydoves.sandwich.*
import javax.inject.Inject

private const val TAG = "UserRepositoryImpl"
class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userMockDataSource: UserMockDataSource
    ): UserRepository {

    override suspend fun addKakaoUser(user: UserDto): Boolean {
        var successFlag = false
        val response = userRemoteDataSource.addKakaoUser(user)

        response.onSuccess {
            Log.d(TAG, "addKakaoUser: success!")
            successFlag = true
        }.onError {
            Log.d(TAG, "addKakaoUser: Error! ${message()}")
        }
        return successFlag
    }
}