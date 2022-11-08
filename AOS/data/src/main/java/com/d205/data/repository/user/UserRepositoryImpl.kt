package com.d205.data.repository.user

import android.util.Log
import com.d205.data.mapper.mapperUserEntityToUser
import com.d205.data.mapper.mapperUserResponseToUser
import com.d205.data.repository.user.local.UserLocalDataSource
import com.d205.data.repository.user.local.UserMockDataSource
import com.d205.data.repository.user.remote.UserRemoteDataSource
import com.d205.domain.model.user.User
import com.d205.domain.model.user.UserDto
import com.d205.domain.repository.UserRepository
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

private const val TAG = "UserRepositoryImpl"
class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userMockDataSource: UserMockDataSource,
    private val userLocalDataSource: UserLocalDataSource
    ): UserRepository {

    override fun joinUser(user: UserDto): Flow<ResultState<User>> = flow {
        Log.d(TAG, "joinUser: $TAG: Loading : $user")
        emit(ResultState.Loading)

        userRemoteDataSource.joinUser(user).collect {
            Log.d(TAG, "joinUser $TAG: collect ${it.body()!!}")
            emit(ResultState.Success(mapperUserEntityToUser(it.body()!!)))
        }
    }

    override fun checkNickname(nickname: String): Flow<ResultState<Boolean>> = flow {
        Log.d(TAG, "checkNickname: Loading")
        emit(ResultState.Loading)
        userRemoteDataSource.checkNickname(nickname).collect {
            Log.d(TAG, "checkNickname: collect : $it")
            emit(ResultState.Success(it))
        }
    }.catch { e ->
        emit(ResultState.Error(e))
    }

    override fun loginKakaoUser(token: String): Flow<ResultState<User>> = flow {

        Log.d(TAG, "loginKakaoUser: $TAG: Loading")
        emit(ResultState.Loading)
        userRemoteDataSource.loginKakaoUser(token).collect {
            Log.d(TAG, "loginKakaoUser $TAG: collect : ${it.body()!!}")
            val accessToken = it.body()!!.jwtDto!!.accessToken
            userLocalDataSource.saveJwt(accessToken!!)
            emit(ResultState.Success(mapperUserResponseToUser(it.body()!!)))
        }
    }.catch { e ->
        emit(ResultState.Error(e))
    }

    override fun loginNaverUser(token: String): Flow<ResultState<User>> = flow {
        Log.d(TAG, "loginNaverUser: $TAG: Loading")
        emit(ResultState.Loading)

        userRemoteDataSource.loginNaverUser(token).collect {
            Log.d(TAG, "loginNaverUser collect: $it")
            val accessToken = it.jwtDto!!.accessToken
            if(accessToken != null) {
                userLocalDataSource.saveJwt(accessToken)
            }
            else {
                userLocalDataSource.saveJwt("")
            }

            Log.d(TAG, "loginNaverUser: ${mapperUserResponseToUser(it)}")
            emit(ResultState.Success(mapperUserResponseToUser(it)))
        }
    }.catch { e ->
        emit(ResultState.Error(e))
    }
}