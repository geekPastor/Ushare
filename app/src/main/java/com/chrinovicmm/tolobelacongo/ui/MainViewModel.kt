package com.chrinovicmm.tolobelacongo.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrinovicmm.tolobelacongo.data.repository.BlogRepository
import com.chrinovicmm.tolobelacongo.domain.model.SignInResult
import com.chrinovicmm.tolobelacongo.util.Result
import com.google.android.gms.auth.api.identity.SignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: BlogRepository
) : ViewModel(){
    val uiState = mutableStateOf(UiState())

    init {
        getSignedUser()
    }

    fun onSignInResult(result: SignInResult){
        uiState.value = uiState.value.copy(
            isSignInSuccessfull = result.data!= null,
            signInError = result.errorMessage
        )
    }

    private fun getSignedUser(){
        viewModelScope.launch {
            repository.getSignedUser().collect{result->
                when(result){
                    is Result.Loading->{
                        uiState.value = uiState.value.copy(
                            isLoading = true
                        )
                    }

                    is Result.Success->{
                        uiState.value = uiState.value.copy(
                            isLoading = false, currentUser = result.data
                        )
                    }

                    is Result.Error->{
                        uiState.value = uiState.value.copy(
                            isLoading = false,
                            currentUser = null,
                            signInError = result.e?.message
                        )
                    }
                }
            }
        }
    }

    fun signOut(oneTapClient: SignInClient){
        viewModelScope.launch {
            repository.signOut(oneTapClient).collect{result->
                when(result){
                    is Result.Loading->{
                        uiState.value = uiState.value.copy(
                            isLoading = true
                        )
                    }

                    is Result.Success->{
                        uiState.value = uiState.value.copy(
                            isLoading = false, currentUser = null
                        )
                    }

                    is Result.Error->{
                        uiState.value = uiState.value.copy(
                            isLoading = false,
                            currentUser = null,
                            signInError = result.e?.message
                        )
                    }
                }
            }
        }
    }

    fun resetSignInState(){
        uiState.value = uiState.value.copy(
            isSignInSuccessfull = false,
            signInError = null
        )
    }
}