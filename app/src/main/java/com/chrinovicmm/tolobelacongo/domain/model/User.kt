package com.chrinovicmm.tolobelacongo.domain.model

data class User(
    val userId: String,
    val userName : String?,
    val profilePictureUrl : String? = null
)
