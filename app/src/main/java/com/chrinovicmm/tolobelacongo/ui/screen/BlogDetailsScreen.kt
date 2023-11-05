package com.chrinovicmm.tolobelacongo.ui.screen

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.chrinovicmm.tolobelacongo.domain.model.Blog
import com.chrinovicmm.tolobelacongo.ui.theme.TolobelaCongoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogDetailsScreen(
    blogTitle: String?,
    blogContent: String?,
    blogThumbnail: String?,
    blogUser: String,
    onBackPressed: ()-> Unit,
    onEditClicked: ()->Unit,
    onDeleteClicked: ()-> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ){
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            actions = {
                IconButton(onClick = onEditClicked) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = null
                    )
                }
                IconButton(onClick = onDeleteClicked) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null
                    )
                }
            }
        )
    }

}

@Preview(showBackground = true)
@Composable
fun BlogDetailsScreenPreview(){
    TolobelaCongoTheme {
        BlogDetailsScreen(
            "Exemple de titre",
            "Exemple de contenu",
            null,
            "Chrinovic MM",
            onBackPressed = {},
            onEditClicked = {},
            onDeleteClicked = {}
        )
    }
}