package com.d108.sduty.model.dto

import java.util.*

data class User(
    var seq: Int,
    var id: String,
    var pass: String,
    var name: String,
    var tel: String,
    var email: String,
    var fcmToken: String,
    var regtime: Date?,
    var userPublic: Int
){
    constructor(): this(0,"","","","","","", null,1)
    constructor(id: String, pass: String):
            this(id, pass, "", "", "")
    constructor(id: String, pass: String, name: String, tel: String, email: String):
            this(0, id, pass, name, tel, email, "",null, 1)
    constructor(id: String, pass: String, name: String, tel: String, email: String, fcm_token: String):
            this(0,id, pass, name, tel, email, fcm_token,null, 1)

}
