package com.chrinovicmm.tolobelacongo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chrinovicmm.tolobelacongo.domain.model.TopicCategory
import com.chrinovicmm.tolobelacongo.util.fromHex


@Composable
fun CategoryItem(
    topicCategory: TopicCategory,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .size(180.dp)
            .then(modifier),
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.fromHex(topicCategory.color)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(0.dp, 0.dp, 8.dp, 0.dp))
                    .background(color = Color.White.copy(alpha = 0.2f))
                    .size(40.dp, 30.dp)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .clip(RoundedCornerShape(16.dp, 0.dp, 0.dp, 0.dp))
                    .background(color = Color.White.copy(alpha = 0.2f))
                    .size(80.dp)

            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                Text(
                    text = topicCategory.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Text(
                    text = "${topicCategory.postCount} posts",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White
                )
            }
        }
    }
}