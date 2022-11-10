package com.d205.data.repository.user.local

interface UserLocalDataSource {
    suspend fun saveJwt(token: String): Boolean
    suspend fun saveSocialType(type: String)
}