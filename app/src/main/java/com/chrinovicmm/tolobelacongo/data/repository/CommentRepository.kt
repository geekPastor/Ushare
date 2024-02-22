package com.chrinovicmm.tolobelacongo.data.repository


import com.chrinovicmm.tolobelacongo.domain.model.TopicComment
import com.chrinovicmm.tolobelacongo.domain.model.TopicPost
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.UUID


class CommentRepository(
    private val firestore: FirebaseFirestore,
    private val fireAuth: FirebaseAuth
) {


    suspend fun createComment(topicComment: TopicComment, topicPost: TopicPost){
        withContext(Dispatchers.IO){
            val currenctUser = fireAuth.currentUser
            val uid = UUID.randomUUID().toString()
            val comment = topicComment.copy(
                uid = uid,
                postId = topicPost.uid,
                userName = currenctUser?.displayName ?: "",
                userProfile = currenctUser?.photoUrl.toString(),
                createdAt = Date()
            )

            firestore.document("comments/$uid").set(comment)
            firestore.document("post/${topicPost.uid}").update(
                topicPost::commentsCount.name,
                topicPost.commentsCount +1
            )
        }
    }

    suspend fun getComments(postUid: String) = callbackFlow {
        val listener = firestore.collection("comments")
            .whereEqualTo(TopicComment::postId.name, postUid)
            .addSnapshotListener{value, error->
                if (value == null || error == null){
                    close(error)
                    return@addSnapshotListener
                }
                value.toObjects<TopicComment>().also{comments->
                    trySend(comments.sortedBy { it.createdAt })
                }
            }
        awaitClose { listener.remove() }
    }
}