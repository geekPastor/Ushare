package com.chrinovicmm.tolobelacongo.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.chrinovicmm.tolobelacongo.R
import com.chrinovicmm.tolobelacongo.domain.model.Blog
import com.chrinovicmm.tolobelacongo.ui.theme.TolobelaCongoTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun BlogItemUI(
    blog: Blog,
    blogDetailsScreen: ()->Unit
){
    Row(
      modifier = Modifier
          .padding(8.dp)
          .fillMaxWidth()
          .clickable {
              blogDetailsScreen()
          },
        verticalAlignment = Alignment.CenterVertically
    ){
        Card(
            modifier = Modifier.size(height = 90.dp, width = 120.dp)
        ){

            AsyncImage(
                model = blog.thumbnail,
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.placeholder),
                error = painterResource(id = R.drawable.placeholder),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column{
            Text(
                text = if (blog.createdDate == null) "Synchronisation en cours..." else
                    SimpleDateFormat("dd/MM/YYYY", Locale.FRANCE).format(blog.createdDate)
            )
            Text(
                text = blog .title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
               Text(
                   text = "${(1..999).random()}",
                   style = MaterialTheme.typography.labelMedium
               )
                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BlogItemUIPreview(){
    TolobelaCongoTheme {
        BlogItemUI(blog = Blog("", "Un titre exemple", createdDate = Calendar.getInstance().time)){}
    }
}