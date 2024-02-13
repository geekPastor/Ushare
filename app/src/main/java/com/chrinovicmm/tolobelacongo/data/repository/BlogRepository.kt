package com.chrinovicmm.tolobelacongo.data.repository

import android.net.Uri
import com.chrinovicmm.tolobelacongo.domain.model.Blog
import com.chrinovicmm.tolobelacongo.domain.model.User
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.chrinovicmm.tolobelacongo.util.Result
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BlogRepository @Inject constructor(
    private var blogsRef : CollectionReference,
    private var storageRef : StorageReference
) {

    //Signout users
    suspend fun signOut(oneTapClient: SignInClient) = flow {
        emit(Result.Loading)
        oneTapClient.signOut().await()
        Firebase.auth.signOut()
        emit(Result.Success(true))
    }.catch { error->
        emit(Result.Error(error))
    }

    //signed in users
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




    //get(read) blog item
    fun getBlogs() = callbackFlow {
        val snapshotListener = blogsRef.orderBy("createdDate")
            .addSnapshotListener{snapshot, error->
                val result = if (snapshot != null){
                    Result.Success(snapshot.toObjects(Blog::class.java))
                } else{
                    Result.Error(error!!)
                }
                trySend(result)
            }
        awaitClose{
            snapshotListener.remove()
        }
    }


    //create blog item
    fun addBlog(
        title: String,
        content: String,
        thumbnail: Uri,
        pdf: Uri,
        user: User
    ) = flow{
        emit(Result.Loading)
        val id = blogsRef.document().id

        val imageStoragRef = storageRef.child("images/$id.jpg")
        val pdfStoragRef = storageRef.child("pdf/$id.pdf")
        val imageDownloadUrl =  imageStoragRef.putFile(thumbnail).await().storage.downloadUrl.await()
        val pdfDownloadUrl =  pdfStoragRef.putFile(pdf).await().storage.downloadUrl.await()

        val blog = Blog(
            id = id,
            title = title,
            content = content,
            thumbnail = imageDownloadUrl.toString(),
            pdf = pdfDownloadUrl.toString(),
            isFavorite = false,
            user = user,
            createdDate = null
        )

        blogsRef.document(id).set(blog).await()

        emit(Result.Success(true))
    }.catch { error->
        emit(Result.Error(error))
    }

    //update bolg item
    fun updateBlog(id: String, title: String, content: String, thumbnail: Uri, pdf: Uri) = flow {
        emit(Result.Loading)

        val imageStoragRef = storageRef.child("images/$id.jpg")
        val pdfStorageRef = storageRef.child("pdf/$id.pdf")
        val imageDownloadUrl =  imageStoragRef.putFile(thumbnail).await().storage.downloadUrl.await()
        val pdfDownloadUrl = pdfStorageRef.putFile(pdf).await().storage.downloadUrl.await()

        blogsRef.document(id).update(
            "title",
            title,
            "content",
            content,
            "thumbnail",
            imageDownloadUrl.toString(),
            pdfDownloadUrl.toString()
        ).await()

        emit(Result.Success(true))
    }.catch { error->
        emit(Result.Error(error))
    }


        //delete Blog item
    fun deleteBlog(id: String) = flow {
        emit(Result.Loading)
        storageRef.child("images/$id.jpg").delete().await()
        storageRef.child("pdf/$id.pdf").delete().await()
        blogsRef.document(id).delete().await()
        emit(Result.Success(true))
    }.catch { error->
        emit(Result.Error(error))
    }
}