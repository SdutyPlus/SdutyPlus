package com.d205.data.dao

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await

private const val TAG = "FirebaseDaoImpl"

class FirebaseDaoImpl: FirebaseDao {
    override suspend fun uploadProfileImage(imgUrl: String, nickname: String): String {
        Log.d(TAG, "uploadProfileImage: Start!!")

        val storageRef = FirebaseStorage.getInstance().reference
        val profileRef = storageRef.child("profile").child(nickname)
        Log.d(TAG, "uploadProfileImage: 1")
        profileRef.putFile(Uri.parse("file://$imgUrl")).await()
        Log.d(TAG, "uploadProfileImage: 2")
        val result = profileRef.downloadUrl.await()
        Log.d(TAG, "uploadProfileImage: 3")
        return result.toString()
    }
}