package com.chrinovicmm.tolobelacongo.data.repository

import com.chrinovicmm.tolobelacongo.domain.model.User
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.chrinovicmm.tolobelacongo.util.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class BlogRepository {
    suspend fun signOut(oneTapClient: SignInClient) = flow {
        emit(Result.Loading)
        oneTapClient.signOut().await()
        Firebase.auth.signOut()
        emit(Result.Success(true))
    }.catch { error->
        emit(Result.Error(error))
    }

    fun getSignedUser() = flow {
        emit(Result.Loading)
        val firebaseCreateUser = Firebase.auth.currentUser
        val user = if (firebaseCreateUser != null){
            User(firebaseCreateUser.uid, firebaseCreateUser.displayName, firebaseCreateUser.photoUrl.toString())
        }else null

        emit(Result.Success(user))
    }
}