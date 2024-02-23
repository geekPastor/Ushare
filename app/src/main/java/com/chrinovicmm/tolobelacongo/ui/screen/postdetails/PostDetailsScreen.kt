package com.chrinovicmm.tolobelacongo.ui.screen.postdetails

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.chrinovicmm.tolobelacongo.domain.model.TopicPost

class PostDetailsScreen (private val postItem: TopicPost): Screen{
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<PostDetailsModel>()
    }
}