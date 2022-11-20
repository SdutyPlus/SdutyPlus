package com.d205.data.dao

import kotlinx.coroutines.flow.Flow

interface FirebaseDao {
    suspend fun uploadProfileImage(imgUrl: String, nickname: String): String
}