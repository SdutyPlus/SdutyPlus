package com.d205.data.repository.user.local

import com.d205.data.dao.UserSharedPreference
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor(
    private val userSharedPreference: UserSharedPreference
): UserLocalDataSource {
    override suspend fun saveJwt(token: String): Boolean =
        userSharedPreference.setStringFromPreference("jwt", token)

    override suspend fun saveSocialType(type: String) {
        userSharedPreference.setStringFromPreference("socialType", type)
    }
}