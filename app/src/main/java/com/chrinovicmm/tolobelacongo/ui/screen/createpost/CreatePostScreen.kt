package com.chrinovicmm.tolobelacongo.ui.screen.createpost

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.chrinovicmm.tolobelacongo.domain.model.TopicCategory
import com.chrinovicmm.tolobelacongo.domain.model.TopicPost
import com.chrinovicmm.tolobelacongo.ui.screen.createpost.CreatePostModel.State

class CreatePostScreen: Screen{
    @Composable
    override fun Content() {
        CreatePostContent()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun CreatePostContent(){
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = getScreenModel<CreatePostModel>()
        val state by screenModel.state.collectAsState()

        LaunchedEffect(Unit){
            screenModel.getData()
        }

        LaunchedEffect(state){
            if (state is State.Posted){
                navigator.pop()
            }
        }

        val context = LocalContext.current
        val  focusManager = LocalFocusManager.current
        var title by remember { mutableStateOf("") }
        var category: TopicCategory? by remember { mutableStateOf(null) }
        var description by remember { mutableStateOf("") }

        var isDropdownExpanded by remember { mutableStateOf(false) }

        val dropDownIconRotation by animateFloatAsState(targetValue = if (isDropdownExpanded) 180f else 0f,
            label = ""
        )
        var isFormValid by remember { mutableStateOf(false) }

        LaunchedEffect(title, description, category){
            isFormValid = title.isNotBlank() && description.length >= 10 && category != null
        }

        Scaffold (
            topBar = {
                TopAppBar(
                    title = { Text(text = "Nouveau Post") },
                    navigationIcon = {
                        IconButton(
                            onClick = { 
                                navigator.pop()
                            }
                        ) {
                            Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                        }
                    }
                )
            }
        ){contentPadding->
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(contentPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(text = "Le titre")},
                    modifier = Modifier.fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ){
                    DropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { 
                            focusManager.clearFocus()
                            isDropdownExpanded = false
                        }
                    ) {
                        (state as State.Initial)
                            ?.categories
                            ?.forEach{selectedCategory->
                                DropdownMenuItem(
                                    text = {
                                        selectedCategory.title
                                    },
                                    onClick = {
                                        category = selectedCategory
                                        focusManager.clearFocus()
                                        isDropdownExpanded = false
                                    }
                                )
                            }
                    }
                    
                    OutlinedTextField(
                        value = category?.title ?:"",
                        onValueChange = {},
                        enabled = state is State.Initial,
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged {
                                if (it.isFocused) {
                                    isDropdownExpanded = true
                                }
                            },
                        label = { Text(text = "Categories")},
                        singleLine = true,
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.ArrowDropDown, 
                                contentDescription = null,
                                modifier = Modifier.rotate(dropDownIconRotation)
                            )
                        }
                    )
                    
                }
                
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .defaultMinSize(minHeight = 100.dp),
                    value = description,
                    onValueChange = {description = it},
                    label = { Text(text = "Description")}
                )
                Button(
                    enabled = isFormValid && state is State.Initial,
                    onClick = { 
                        val post = TopicPost(
                            title,
                            description
                        )
                        if (category !=null){
                            screenModel.createPost(post, category!!)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        AnimatedVisibility(visible = state is State.Posting) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        }
                        Text(text = "Soumettre")
                    }
                }
            }
        }
    }
}