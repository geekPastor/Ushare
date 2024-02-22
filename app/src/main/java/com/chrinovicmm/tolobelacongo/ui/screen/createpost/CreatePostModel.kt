package com.chrinovicmm.tolobelacongo.ui.screen.createpost

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.chrinovicmm.tolobelacongo.data.repository.CategoryRepository
import com.chrinovicmm.tolobelacongo.data.repository.PostRepository
import com.chrinovicmm.tolobelacongo.domain.model.TopicCategory
import com.chrinovicmm.tolobelacongo.domain.model.TopicPost
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CreatePostModel(
    private val categoryRepository: CategoryRepository,
    private val postRepository: PostRepository
) : StateScreenModel<CreatePostModel.State>(State.Loading) {
    //override var mutableState: State.Loading = State.Loading
    sealed class State {
        data class Initial(val categories: List<TopicCategory>) : State()
        data object Loading : State()
        data object Posting : State()
        data object Posted : State()
        data class Error(val message: String) : State()
    }

    fun getData() {
        screenModelScope.launch {
            categoryRepository.getCategories()
                .catch {
                    mutableState.value = State.Error("Une erreur est survernue")
                }.collect {
                    mutableState.value = State.Initial(it)
                }
        }
    }

    fun createPost(topicPost: TopicPost, category: TopicCategory) {
        screenModelScope.launch {
            try {
                mutableState.value = State.Posting
                postRepository.createPost(topicPost, category)
                mutableState.value = State.Posted
            } catch (e: Exception) {
                mutableState.value = State.Error(e.localizedMessage?.toString() ?: "")
            }
        }
    }
}