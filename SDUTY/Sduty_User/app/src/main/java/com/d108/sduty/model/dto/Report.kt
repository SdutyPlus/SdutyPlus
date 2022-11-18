package com.d108.sduty.model.dto

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Report(
    val seq: Int,
    val ownerSeq: Int,
    val date: String,
    val totalTime: String,
    var tasks: List<Task>
) : Parcelable {
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {}
}