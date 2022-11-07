package com.d205.data.repository.user.remote

import android.util.Log
import com.d205.data.api.UserApi
import com.d205.data.model.BaseResponse
import com.d205.data.model.user.UserEntity
import com.d205.data.model.user.UserResponse
import com.d205.domain.model.user.UserDto
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject


private const val TAG = "UserRemoteDataSourceImpl"
class UserRemoteDataSourceImpl @Inject constructor(
    private val userApi: UserApi
): UserRemoteDataSource {

    override fun joinUser(user: UserDto): Flow<Response<UserEntity>> = flow {
        emit(userApi.updateProfile(user))
    }


    override suspend fun checkNickname(nickname: String): ApiResponse<String> =
        userApi.checkNickname(nickname)

    override fun loginKakaoUser(token: String): Flow<Response<UserResponse>> = flow {
        Log.d(TAG, "loginKakaoUser: $TAG token : $token")
        emit(userApi.loginKakaoUser(token))
    }


    override fun loginNaverUser(token: String): Flow<UserResponse> = flow {
        Log.d(TAG, "loginNaverUser: $TAG token : $token")
        //Log.d(TAG, "loginNaverUser: $TAG api result : ${userApi.loginNaverUser(token).body()}")

        val response = userApi.loginNaverUser(token)
        if(response.status == 200 && response.data != null) {
            emit(response.data)
        }
        else {
            emit(UserResponse())
        }
    }
}