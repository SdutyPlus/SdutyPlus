package com.d108.sduty.model.dto

data class Alarm(
    var seq: Int,
    var time: String,
    var mon: Boolean,
    var tue: Boolean,
    var wed: Boolean,
    var thu: Boolean,
    var fri: Boolean,
    var sat: Boolean,
    var sun: Boolean
) {
    // constructor(): this
}