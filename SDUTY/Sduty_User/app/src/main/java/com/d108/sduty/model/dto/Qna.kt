package com.d108.sduty.model.dto

data class Qna(
    var seq: Int,
    var userSeq: Int,
    var title: String,
    var content: String,
    var writer: String,
    var answer: String,
    var ansWriter: String) {

    constructor(userSeq: Int, title: String, content: String, writer: String):
            this(0,userSeq, title, content, writer, "", "")
}