package com.chrinovicmm.tolobelacongo.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.chrinovicmm.tolobelacongo.domain.model.TopicCategory
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class CategoryRepository(private val firestore: FirebaseFirestore) {

    fun getCategories() = callbackFlow {
        val listener = firestore.collection("categories").addSnapshotListener { value, error ->
            if (value == null || error != null) {
                close(error)
                return@addSnapshotListener
            }

            value.toObjects<TopicCategory>().also { categories ->
                trySend(categories.sortedByDescending { it.postCount })
            }

        }

        awaitClose { listener.remove() }
    }
}