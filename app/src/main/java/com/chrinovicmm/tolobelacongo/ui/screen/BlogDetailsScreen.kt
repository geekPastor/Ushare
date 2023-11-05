package com.chrinovicmm.tolobelacongo.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.chrinovicmm.tolobelacongo.domain.model.Blog
import com.chrinovicmm.tolobelacongo.ui.theme.TolobelaCongoTheme

@Composable
fun BlogDetailsScreen(
    blogTitle: String?,
    blogContent: String?,
    blogThumbnail: String?,
    blogUser: String
){

}

@Preview(showBackground = true)
@Composable
fun BlogDetailsScreenPreview(){
    TolobelaCongoTheme {
        BlogDetailsScreen(
            "Exemple de titre",
            "Exemple de contenu",
            null,
            "Chrinovic MM"
        )
    }
}