package com.chrinovicmm.tolobelacongo.domain.model

import java.util.Date

data class TopicPost(
    val uid: String = "",
    val categoryUid: String = "",
    val categoryName: String = "",
    val title: String = "",
    val description: String = "",
    val commentsCount: Int = 0,
    val userId: String = "",
    val userName: String = "",
    val userProfile: String = "",
    val createdAt: Date? = null
)