package com.chrinovicmm.tolobelacongo.ui

import android.net.Uri
import androidx.compose.runtime.MovableContent
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrinovicmm.tolobelacongo.data.repository.BlogRepository
import com.chrinovicmm.tolobelacongo.domain.model.SignInResult
import com.chrinovicmm.tolobelacongo.domain.model.User
import com.chrinovicmm.tolobelacongo.util.Result
import com.google.android.gms.auth.api.identity.SignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: BlogRepository
) : ViewModel(){
    val uiState = mutableStateOf(UiState())

    init {
        getSignedUser()
        viewModelScope.launch {
            repository.getBlogs().collect{result->
                when(result){
                    is Result.Success->{
                        uiState.value = uiState.value.copy(
                            blogs = result.data!!
                        )
                    } else ->{

                    }
                }
            }
        }
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

    fun onAddBlog(
        title: String,
        content: String,
        thumbnails: Uri,
        user: User
    ){
        viewModelScope.launch {
            repository.addBlog(title, content, thumbnails, user).collect{result->
                when(result){
                    is Result.Loading->{
                        uiState.value = uiState.value.copy(isLoading = true)
                    } else->{
                        uiState.value = uiState.value.copy(isLoading = false)
                    }
                }
            }
        }
    }

    fun onUpdateBlog(
        id: String,
        title: String,
        content: String,
        thumbnails: Uri,
    ){
        viewModelScope.launch {
            repository.updateBlog(id, title, content, thumbnails).collect{result->
                when(result){
                    is Result.Loading->{
                        uiState.value = uiState.value.copy(isLoading = true)
                    } else->{
                    uiState.value = uiState.value.copy(isLoading = false)
                }
                }
            }
        }
    }

    fun onDeleteBlog(id: String){
        viewModelScope.launch {
            repository.deleteBlog(id).collect{result->
                when(result){
                    is Result.Loading->{
                        uiState.value = uiState.value.copy(isLoading = true)
                    } else->{
                    uiState.value = uiState.value.copy(isLoading = false)
                    }
                }
            }
        }
    }
}