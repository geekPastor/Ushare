package com.chrinovicmm.tolobelacongo.ui.screen.forum

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.chrinovicmm.tolobelacongo.ui.components.CategoryItem
import com.chrinovicmm.tolobelacongo.ui.components.PostItem
import com.chrinovicmm.tolobelacongo.ui.screen.createpost.CreatePostScreen
import com.chrinovicmm.tolobelacongo.ui.screen.postdetails.PostDetailsScreen
import java.util.Locale.Category

class ForumScreen : Screen{
    @Composable
    override fun Content(){
        ForumContent()
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
    @Composable
    private fun ForumContent(){
        val screenModel = getScreenModel<ForumScreenModel>()
        val state by screenModel.state.collectAsState()
        
        LaunchedEffect(Unit){
            screenModel.getData()
        }
        val navigator = LocalNavigator.currentOrThrow
        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { Text(text = "Creer un Post")},
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription =null
                        )
                    },
                    onClick = {
                        navigator.push(CreatePostScreen())
                    }
                )
            },
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
                    title = { Text(text = "Forum de discussion") },
                    actions = {
                        IconButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    },

                )
            }
        ) {contentPadding ->
           when(state){
               is ForumScreenModel.State.Error->{
                   Text(text = (state as ForumScreenModel.State.Error).message)
               }

               ForumScreenModel.State.Loading->{
                   Box(
                       modifier = Modifier
                           .fillMaxWidth()
                           .consumeWindowInsets(contentPadding),
                       //contentAlignment = Alignment.Center
                   ){
                       CircularProgressIndicator()
                   }
               }

               is ForumScreenModel.State.Success -> {
                   LazyColumn(
                       modifier = Modifier
                           .fillMaxSize()
                           .consumeWindowInsets(contentPadding),
                       verticalArrangement = Arrangement.spacedBy(16.dp),
                       contentPadding = contentPadding
                   ){
                       item{
                           LazyRow(
                               horizontalArrangement = Arrangement.spacedBy(8.dp),
                               contentPadding = PaddingValues(16.dp)
                           ){
                               items(
                                   (state as ForumScreenModel.State.Success).categories,
                                   key = {it.uid}
                               ){item->
                                   CategoryItem(topicCategory = item)
                               }
                           }
                       }

                       items(
                           (state as ForumScreenModel.State.Success).post,
                           key = {it.uid}
                       ){item->
                           PostItem(
                               post = item,
                               onClick = {
                                   navigator.push(PostDetailsScreen(item))
                               },
                               modifier = Modifier.padding(horizontal = 16.dp)
                           )
                       }

                   }
               }
           }

            
        }
    }
}