package com.chrinovicmm.tolobelacongo.ui.screen.forum

import android.util.Log
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.chrinovicmm.tolobelacongo.data.repository.CategoryRepository
import com.chrinovicmm.tolobelacongo.data.repository.PostRepository
import com.chrinovicmm.tolobelacongo.domain.model.TopicCategory
import com.chrinovicmm.tolobelacongo.domain.model.TopicPost
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ForumScreenModel (
    private val categoryRepository: CategoryRepository,
    private val postRepository: PostRepository
): StateScreenModel<ForumScreenModel.State>(State.Loading){
    sealed class State{
        data object Loading: State()
        data class Success(
            val categories: List<TopicCategory>,
            val post: List<TopicPost>
        ):State()

        data class Error(val message: String): State()
    }

    fun getData(){
        screenModelScope.launch {
            val categoriesFlow = categoryRepository.getCategories()
            val postFlow = postRepository.getPost()
            Log.e("Called", "1")
            combine(categoriesFlow, postFlow){categories, posts->
                State.Success(categories, posts)
            }.catch {
                mutableState.value = State.Error("Une erreur s'est produit")
            }.collect{
                mutableState.value = it
            }
        }
    }
}