package com.d205.data.repository.user.remote

import android.annotation.SuppressLint
import android.util.Log
import com.d205.data.api.UserApi
import com.d205.data.dao.FirebaseDao
import com.d205.data.model.user.UserResponse
import com.d205.domain.model.user.UserDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


private const val TAG = "UserRemoteDataSourceImpl"
class UserRemoteDataSourceImpl @Inject constructor(
    private val userApi: UserApi,
    private val firebaseDao: FirebaseDao
): UserRemoteDataSource {

    @SuppressLint("LongLogTag")
    override fun joinUser(user: UserDto): Flow<UserResponse> = flow {
        Log.d(TAG, "joinUser: $user")
        var result : String? = null

        if(user.imgUrl != null) {
            result = firebaseDao.uploadProfileImage(user.imgUrl!!, user.nickname)
        }
        user.imgUrl = result
        val response = userApi.joinUser(user)
        if(response.status == 200 && response.data != null) {
            emit(response.data)
        }
        else {
            emit(UserResponse())
        }
    }


    @SuppressLint("LongLogTag")
    override fun checkNickname(nickname: String): Flow<Boolean> = flow {
        Log.d(TAG, "checkNickname: $nickname")
        val response = userApi.checkNickname(nickname)
        Log.d(TAG, "checkNickname api response : $response")

        if(response.status == 200 && response.data != null) {
            emit(response.data)
        }
        else {
            emit(false)
        }
    }

    @SuppressLint("LongLogTag")
    override fun loginKakaoUser(token: String): Flow<UserResponse> = flow {
        Log.d(TAG, "loginKakaoUser: $TAG token : $token")

        Log.d(TAG, "loginKakaoUser: start api!")
        val response = userApi.loginKakaoUser(token)
        Log.d(TAG, "loginKakaoUser: response : ${response.data}")
        if (response.status == 200 && response.data != null) {
            emit(response.data)
        } else {
            emit(UserResponse())
        }
    }


    @SuppressLint("LongLogTag")
    override fun loginNaverUser(token: String): Flow<UserResponse> = flow {
        Log.d(TAG, "loginNaverUser: $TAG token : $token")

        Log.d(TAG, "loginNaverUser: start api!")
        val response = userApi.loginNaverUser(token)
        Log.d(TAG, "loginNaverUser: response code : ")
        if (response.status == 200 && response.data != null) {
            emit(response.data)
        } else {
            emit(UserResponse())
        }
    }

    @SuppressLint("LongLogTag")
    override fun getUser(): Flow<UserResponse>  = flow {
        val response = userApi.getUser()
        if(response.status == 200 && response.data != null) {
            emit(response.data)
        }
        else {
            emit(UserResponse())
        }
    }

    override fun deleteUser(): Flow<Boolean> = flow {
        val response = userApi.deleteUser()
        if(response.status == 200) {
            Log.d("exit","탈퇴 성공")
            emit(true)
        }
        else {
            Log.d("exit","탈퇴 실패")
            emit(false)
        }
    }

    override fun updateUser(user: UserDto): Flow<UserResponse> = flow {

        var result : String? = null

        if(user.imgUrl != null) {
            result = firebaseDao.uploadProfileImage(user.imgUrl!!, user.nickname)
        }
        user.imgUrl = result
        Log.d(TAG, "updateUser: $user")
        val response = userApi.updateUser(user)
        if(response.status == 200 && response.data != null) {
            emit(response.data)
        }
        else {
            emit(UserResponse())
        }
    }
}