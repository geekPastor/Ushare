package com.chrinovicmm.tolobelacongo.data.repository

import com.chrinovicmm.tolobelacongo.domain.model.TopicPost
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class CategoryRepository(
    private val firestore: FirebaseFirestore
) {
    fun getCategories() = callbackFlow {
        val listener = firestore.collection("categories").addSnapshotListener{value, error->
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