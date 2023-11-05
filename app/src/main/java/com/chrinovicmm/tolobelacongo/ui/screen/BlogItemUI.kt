package com.chrinovicmm.tolobelacongo.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chrinovicmm.tolobelacongo.domain.model.Blog

@Composable
fun BlogItemUI(
    blog: Blog,
    BlogDetailsScreen: ()->Unit
){
    Row(
      modifier = Modifier
          .padding(8.dp)
          .fillMaxWidth()
          .clickable {
              BlogDetailsScreen()
          }
    ){

    }
}