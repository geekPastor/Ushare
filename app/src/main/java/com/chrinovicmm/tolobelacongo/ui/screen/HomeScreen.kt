package com.chrinovicmm.tolobelacongo.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.chrinovicmm.tolobelacongo.R
import com.chrinovicmm.tolobelacongo.domain.model.Blog
import com.chrinovicmm.tolobelacongo.domain.model.User
import com.chrinovicmm.tolobelacongo.ui.theme.TolobelaCongoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    currentUser: User?,
    blogs : List<Blog>,
    isLoading : Boolean,
    signOut : ()->Unit,
    NavigateToBlogDetailsScreen: (Blog)-> Unit,
    navigateToUpdateBogScreen: ()->Unit,
    navigateToSigninScreen: ()->Unit
){

    var isDropdownMenuExpanded by remember {
        mutableStateOf(false)
    }

    var query by remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = currentUser){
        if (currentUser == null){
            navigateToSigninScreen()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tolobela Congo",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                },
                actions = {
                    AsyncImage(
                        model = currentUser?.profilePictureUrl,
                        contentDescription = "User profile picture",
                        placeholder = painterResource(id = R.drawable.default_profile_pic),
                        error = painterResource(id = R.drawable.default_profile_pic),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(10.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .clickable {
                                isDropdownMenuExpanded = !isDropdownMenuExpanded
                            }
                    )

                    DropdownMenu(expanded = isDropdownMenuExpanded, onDismissRequest = { isDropdownMenuExpanded = false }) {
                        DropdownMenuItem(
                            text = { 
                                   Column(
                                       horizontalAlignment = Alignment.CenterHorizontally
                                   ) {
                                       Text(text = "${currentUser?.userName}")
                                       Text(text = "Deconnexion")
                                   }
                            },
                            onClick = {
                                signOut()
                                isDropdownMenuExpanded = false
                            }
                        )
                    }

                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToUpdateBogScreen
            ){
                Icon(
                    imageVector = Icons.Filled.Create,
                    contentDescription = null)
            }
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ){
            if(isLoading){
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            /*OutlinedTextField(
                value = query,
                onValueChange ={text->
                    query = text
                },
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(50),
                placeholder = { Text(text = "Rechercher...") },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "Search icon")
                },
                trailingIcon = {
                    AnimatedVisibility(visible = query.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = null
                        )
                    }
                }
            )*/

            if (blogs.isEmpty()){
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(200.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.empty),
                            contentDescription = null
                        )

                        Text(
                            text = "Pas de ressources disponible",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else{
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp)
                ){
                    items(blogs){blog->
                        BlogItemUI(
                            blog = blog, BlogDetailsScreen = {
                                NavigateToBlogDetailsScreen(blog)
                            }
                        )
                    }

                }

            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPrview(){
    TolobelaCongoTheme{
        HomeScreen(
            currentUser = null,
            blogs = emptyList(),
            isLoading = false,
            {},
            {},
            {}
        ){}

    }
}