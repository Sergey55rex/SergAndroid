package ru.netology.dto

import android.net.wifi.aware.PublishConfig

data class Post(
        val id: Long,
        val author: String,
        val content: String,
        val published: String,
        var liked: Boolean ,
        var likes: Int = 0,
        var toSend: Boolean ,
        var toSends: Int = 0,
        var viewing: Boolean ,
        var viewings: Int = 0,
//        var video: String,

)