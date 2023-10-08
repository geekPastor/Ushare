package com.chrinovicmm.tolobelacongo.ui.screen

import android.app.Notification.Action
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "RDC Mon pays",
                        fontSize = 25.sp,
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
                            .clickable { }
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ){
            Text(text = "Contenu")
        }
    }
}