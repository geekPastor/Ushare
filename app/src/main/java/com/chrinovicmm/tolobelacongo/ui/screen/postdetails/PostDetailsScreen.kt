package com.chrinovicmm.tolobelacongo.ui.screen.postdetails


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.chrinovicmm.tolobelacongo.ui.components.CommentItem
import com.chrinovicmm.tolobelacongo.domain.model.TopicPost
import com.chrinovicmm.tolobelacongo.domain.model.TopicComment
import com.chrinovicmm.tolobelacongo.ui.components.PostItem
import com.chrinovicmm.tolobelacongo.ui.screen.postdetails.PostDetailsModel.State


class PostDetailScreen(private val postItem: TopicPost) : Screen {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
        ExperimentalComposeUiApi::class, ExperimentalLayoutApi::class
    )
    @Composable
    override fun Content() {

        val screenModel = getScreenModel<PostDetailsModel>()

        val state by screenModel.state.collectAsState()

        LaunchedEffect(Unit) {
            screenModel.getComments(postItem.uid)
        }

        val navigator = LocalNavigator.currentOrThrow

        var commentText by remember {
            mutableStateOf("")
        }
        val keyboardController = LocalSoftwareKeyboardController.current

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Post") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navigator.pop()
                            },
                        ) {
                            Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                        }
                    }
                )
            },
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        label = { Text(text = "Message") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    FloatingActionButton(
                        onClick = {
                            if (commentText.isNotBlank()) {
                                val comment = TopicComment(message = commentText)
                                screenModel.createComment(comment, postItem)
                                commentText = ""
                                keyboardController?.hide()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = null
                        )
                    }

                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(paddingValues),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = paddingValues
            ) {
                item(key = "post-item") {
                    PostItem(
                        post = postItem,
                        onClick = {},
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                when (state) {
                    is State.Error -> {}
                    State.Loading -> {
                        item(key = "loader") {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    is State.Success -> {
                        item(key = "comments-header") {
                            Text(
                                text = "Commentaires (${(state as State.Success).comments.count()})",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                        items(
                            (state as State.Success).comments,
                            key = { it.uid }
                        ) { comment ->
                            CommentItem(
                                isMine = comment.userId == (state as PostDetailsModel.State.Success).userUid,
                                comment = comment,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth()
                                    .animateItemPlacement()
                            )
                        }
                    }
                }
            }
        }
    }
}