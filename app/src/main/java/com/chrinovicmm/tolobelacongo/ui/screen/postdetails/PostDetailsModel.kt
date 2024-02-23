package com.chrinovicmm.tolobelacongo.ui.screen.postdetails

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.chrinovicmm.tolobelacongo.data.repository.CommentRepository
import com.chrinovicmm.tolobelacongo.domain.model.TopicComment
import com.chrinovicmm.tolobelacongo.domain.model.TopicPost
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PostDetailsModel(
    private val commentRepository: CommentRepository,
    private val firebaseAuth: FirebaseAuth
) : StateScreenModel<PostDetailsModel.State>(State.Loading){
    sealed class State{
        data object Loading : State()
        data class Success(
            val comments: List<TopicComment>,
            val userUid: String
        ) : State()

        data class Error(val message: String): State()
    }

    fun getComments(postId: String){
        screenModelScope.launch {
            commentRepository.getComments(postId)
                .catch {
                    mutableState.value = State.Error(
                        it.localizedMessage?.toString() ?: "Une erreur s'est produite"
                    )
                }.collect{
                    mutableState.value = State.Success(comments = it, firebaseAuth.currentUser?.uid.toString())
                }
        }
    }

    fun createComment(topicComment: TopicComment, topicPost: TopicPost){
        screenModelScope.launch {
            try {
                commentRepository.createComment(topicComment, topicPost)
            }catch (_: Exception){}
        }
    }
}