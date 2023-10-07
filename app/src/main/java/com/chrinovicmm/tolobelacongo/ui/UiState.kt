package com.chrinovicmm.tolobelacongo.ui

import com.chrinovicmm.tolobelacongo.domain.model.User

data class UiState(
    val isSignInSuccessfull : Boolean = false,
    val signInError : String? = null,
    val currentUser: User? = null,
    val isLoading : Boolean = false
)
