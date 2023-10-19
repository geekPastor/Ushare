package com.chrinovicmm.tolobelacongo.data.repository

import com.chrinovicmm.tolobelacongo.domain.model.User
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.chrinovicmm.tolobelacongo.util.Result
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BlogRepository @Inject constructor(
    private var blogsRef : CollectionReference,
    private var storageRef : StorageReference
) {
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
    }.catch { error->
        emit(Result.Error(error))
    }

    /*fun getBlogs() = callbackFlow {
        val snapshotListener = blogsRef.orderBy()
    }*/
}