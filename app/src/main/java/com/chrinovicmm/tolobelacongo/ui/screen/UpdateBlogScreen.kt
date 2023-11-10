package com.chrinovicmm.tolobelacongo.ui.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.chrinovicmm.tolobelacongo.R
import com.chrinovicmm.tolobelacongo.ui.theme.TolobelaCongoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateBlogScreen(
    blogTitle: String?,
    blogContent: String?,
    thumbnail: String?,
    onBackPressed: ()->Unit,
    UpdateBlog: (String, String, String)->Unit
){
    
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        var title by remember {
            mutableStateOf(blogTitle ?: "")
        }

        var selectedThumbnail by remember {
            mutableStateOf(thumbnail ?: "")
        }

        var content by remember {
            mutableStateOf(blogContent ?: "")
        }

        val pictureLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = {uri->
                selectedThumbnail = uri.toString()
            }
        )


        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable {
                        pictureLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }
            ){
                if (selectedThumbnail.isEmpty()){
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        shape = RectangleShape
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Icon(
                                imageVector = Icons.Filled.AddCircle,
                                contentDescription = null
                            )
                            Text(text = "Cliquez pour ajouter une image")
                        }
                    }
                } else{
                    AsyncImage(
                        model = selectedThumbnail,
                        contentDescription = null,
                        placeholder = painterResource(id = R.drawable.placeholder),
                        error = painterResource(id = R.drawable.placeholder),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopStart)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.background)
                ){
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            }

            TextField(
                value = title,
                onValueChange = {text->
                    title = text
                },
                placeholder = {
                    Text(
                        text = "titre de l'avis",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.titleLarge,
                maxLines = 2

            )

            TextField(
                value = content,
                onValueChange = {text->
                    content = text
                },
                placeholder = {
                    Text(
                        text = "le contenu de l'avis",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.bodyLarge,
                //maxLines = 100
            )
        }

        FloatingActionButton(
            onClick = {
                      UpdateBlog(title, content, selectedThumbnail)
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomStart)
        ) {
            Icon(
                imageVector = Icons.Filled.Done,
                contentDescription = null
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun UpdateBlogScreenPreview(){
    TolobelaCongoTheme {
        UpdateBlogScreen("", "", "", {}, {_, _, _->})
    }
}