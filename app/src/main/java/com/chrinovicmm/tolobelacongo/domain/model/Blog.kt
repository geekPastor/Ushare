package com.chrinovicmm.tolobelacongo.domain.model

data class Blog(
    val id: String = "",
    val title : String = "",
    val content : String = "",
    val thumbnail: String = "",
    val isFavorite: Boolean = false,
    val user: User? = null,
)
