package com.chrinovicmm.tolobelacongo.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.chrinovicmm.tolobelacongo.R
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

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxSize(0.94f)
                    .align(Alignment.Center)
            ) {
                AsyncImage(
                    model = blogThumbnail,
                    contentDescription = null,
                    placeholder = painterResource(id = R.drawable.placeholder),
                    error = painterResource(id = R.drawable.placeholder),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Box(
                modifier = Modifier
                    .padding(end = 32.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .align(Alignment.BottomEnd)
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Gray)){
                    append("Avis de: ")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
                    append(blogUser)
                }
            },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier)


    }

}

//1:54:13

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