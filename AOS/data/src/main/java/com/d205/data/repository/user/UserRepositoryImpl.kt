package com.d205.data.repository.user

import android.util.Log
import com.d205.data.mapper.mapperToUser
import com.d205.data.repository.user.local.UserMockDataSource
import com.d205.data.repository.user.remote.UserRemoteDataSource
import com.d205.domain.model.user.User
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

    override suspend fun addNaverUser(user: UserDto): Boolean {
        var successAddedFlag = false
        val response = userRemoteDataSource.addNaverUser(user)

        response.onSuccess {
            Log.d(TAG, "addNaverUser: success!")
            successAddedFlag = true
        }.onError {
            Log.d(TAG, "addNaverUser: Error! ${message()}")
        }
        return successAddedFlag
    }

    override suspend fun checkNickname(nickname: String): Boolean {
        var canUseFlag = false
        val response = userRemoteDataSource.checkNickname(nickname)

        response.onSuccess {
            Log.d(TAG, "checkNickname: success!")
            canUseFlag = true
        }.onError {
            Log.d(TAG, "checkNickname: ${message()}")
        }
        return canUseFlag
    }

    override suspend fun loginKakaoUser(token: String): User {
        var user = User()
        val response = userRemoteDataSource.loginKakaoUser(token)

        response.onSuccess {
            Log.d(TAG, "loginKakaoUser: success!")
            user = mapperToUser(data)
        }.onError {
            Log.d(TAG, "loginKakaoUser: ${message()}")
        }
        return user
    }

    override suspend fun loginNaverUser(token: String): User {
        var user = User()
        val response = userRemoteDataSource.loginNaverUser(token)

        response.onSuccess {
            Log.d(TAG, "loginNaverUser: success!")
            user = mapperToUser(data)
        }.onError {
            Log.d(TAG, "loginNaverUser: ${message()}")
        }
        return user
    }
}