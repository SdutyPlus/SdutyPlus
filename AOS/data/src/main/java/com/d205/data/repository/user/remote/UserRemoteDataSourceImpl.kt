package com.d205.data.repository.user.remote

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import com.d205.data.api.UserApi
import com.d205.data.dao.FirebaseDao
import com.d205.data.model.user.UserEntity
import com.d205.data.model.user.UserResponse
import com.d205.domain.model.user.UserDto
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject


private const val TAG = "UserRemoteDataSourceImpl"
class UserRemoteDataSourceImpl @Inject constructor(
    private val userApi: UserApi,
    private val firebaseDao: FirebaseDao
): UserRemoteDataSource {

    @SuppressLint("LongLogTag")
    override fun joinUser(user: UserDto): Flow<UserResponse> = flow {
        Log.d(TAG, "joinUser: $user")

        val result = firebaseDao.uploadProfileImage(user.imgUrl, user.nickname)

        if(result != null) {
            Log.d(TAG, "joinUser: 파이어베이스에 프로필 사진 업로드 성공!")
            user.imgUrl = result
            val response = userApi.updateProfile(user)
            if(response.status == 200 && response.data != null) {
                emit(response.data)
            }
            else {
                emit(UserResponse())
            }
        }
        else {
            Log.d(TAG, "joinUser: uri 주소 null")
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
}