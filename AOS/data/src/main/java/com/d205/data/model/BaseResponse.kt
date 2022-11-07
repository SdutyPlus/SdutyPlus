package com.d205.data.model

import com.google.gson.annotations.SerializedName

data class BaseResponse<out T> (
    @SerializedName("status") val status: Int,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T?
)