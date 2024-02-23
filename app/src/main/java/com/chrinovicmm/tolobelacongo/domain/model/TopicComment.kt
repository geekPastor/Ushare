package com.chrinovicmm.tolobelacongo.domain.model

import java.util.Date

data class TopicComment(
    val uid: String = "",
    val postId: String = "",
    val message: String = "",
    val createdAt: Date? = null,
    val userId: String= "",
    val userName: String = "",
    val userProfile: String = ""
)
