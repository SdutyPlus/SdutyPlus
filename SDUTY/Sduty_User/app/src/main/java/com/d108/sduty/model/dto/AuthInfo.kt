package com.d108.sduty.model.dto

import java.util.*

data class AuthInfo(var tel: String, var code: String, var expire: Date?) {
    constructor(tel: String, code:String): this(tel, code, null)
}