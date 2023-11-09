package com.chrinovicmm.tolobelacongo.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.chrinovicmm.tolobelacongo.ui.theme.TolobelaCongoTheme

@Composable
fun UpdateBlogScreen(
    blogTitle: String?,
    blogContent: String?,
    thumbnail: String?
){


}

@Preview(showBackground = true)
@Composable
fun UpdateBlogScreenPreview(){
    TolobelaCongoTheme {
        UpdateBlogScreen("", "", "")
    }
}