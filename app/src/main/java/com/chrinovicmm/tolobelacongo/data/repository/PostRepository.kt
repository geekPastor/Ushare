package com.chrinovicmm.tolobelacongo.data.repository

import com.chrinovicmm.tolobelacongo.domain.model.TopicCategory
import com.chrinovicmm.tolobelacongo.domain.model.TopicPost
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.UUID

class PostRepository(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun createPost(topicPost: TopicPost, topicCategory: TopicCategory){
        withContext(Dispatchers.IO){
            val currentUser = firebaseAuth.currentUser
            val uid = UUID.randomUUID().toString()
            val post = topicPost.copy(
                uid = uid,
                categoryUid = topicCategory.uid,
                categoryName = topicCategory.title,
                userId = currentUser?.uid ?: "",
                userName = currentUser?.displayName ?:"",
                userProfile = currentUser?.photoUrl.toString(),
                createdAt = Date()
            )

            firestore.document("posts/${uid}")
                .set(post).await()

            firestore.document("categories/${topicCategory.uid}")
                .set(topicCategory.copy(postCount = topicCategory.postCount + 1))
        }
    }

    fun getPost() = callbackFlow {
        val listener = firestore.collection("posts").addSnapshotListener{value, error->
            if (value == null || error != null){
                close(error)
                return@addSnapshotListener
            }

            value.toObjects<TopicPost>().also { posts->
                trySend(posts.sortedBy { it.createdAt })
            }
        }

        awaitClose { listener.remove() }
    }
}