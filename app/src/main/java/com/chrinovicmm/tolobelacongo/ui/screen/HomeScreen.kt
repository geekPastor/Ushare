package com.chrinovicmm.tolobelacongo.ui.screen

import android.app.Notification.Action
import android.graphics.drawable.Icon
import android.net.wifi.hotspot2.pps.HomeSp
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.chrinovicmm.tolobelacongo.domain.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    currentUser: User?,
    signOut : ()->Unit
){

    var isDropdownMenuExpanded by remember {
        mutableStateOf(false)
    }

    var query by remember {
        mutableStateOf("")
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
                                   Text(text = "Deconnexion")
                            },
                            onClick = {
                                signOut()
                                isDropdownMenuExpanded = false
                            }
                        )
                    }

                }
            )
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ){
            OutlinedTextField(
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
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPrview(){
    //HomeScreen(currentUser = null)
}