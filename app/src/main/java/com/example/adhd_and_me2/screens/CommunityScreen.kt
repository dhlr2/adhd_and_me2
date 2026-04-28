package com.example.adhd_and_me2.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class CommunityLink(
    val name: String,
    val emoji: String,
    val description: String,
    val url: String
)

private val communities = listOf(
    CommunityLink(
        name        = "ADHD UK",
        emoji       = "🇬🇧",
        description = "A UK-based charity offering support, resources and a welcoming " +
                      "community for adults and young people with ADHD.",
        url         = "https://adhduk.co.uk"
    ),
    CommunityLink(
        name        = "r/ADHD",
        emoji       = "💬",
        description = "One of the largest ADHD communities online with over 1 million members. " +
                      "A safe space to share experiences, ask questions and feel understood.",
        url         = "https://www.reddit.com/r/ADHD"
    ),
    CommunityLink(
        name        = "ADHD Foundation",
        emoji       = "🧡",
        description = "An ADHD charity providing support, education and " +
                      "help for people with ADHD and their families.",
        url         = "https://www.snapcharity.org/business-directory/22151/the-adhd-foundation-neurodiversity-charity/"
    ),
    CommunityLink(
        name        = "How to ADHD",
        emoji       = "▶️",
        description = "A YouTube channel by Jessica McCabe with practical, " +
                      "evidence-based advice for living with ADHD — friendly and relatable.",
        url         = "https://www.youtube.com/@HowtoADHD"
    )
)

@Composable
fun CommunityScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Text(
            text       = "Community",
            style      = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color      = MaterialTheme.colorScheme.onBackground,
            modifier   = Modifier.padding(vertical = 12.dp)
        )
        Text(
            text     = "You are not alone. Here are some trusted communities and " +
                       "resources where you can connect with others who get it.",
            style    = MaterialTheme.typography.bodyMedium,
            color    = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(communities) { community ->
                CommunityCard(community)
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun CommunityCard(community: CommunityLink) {
    val context = LocalContext.current

    Card(
        shape     = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors    = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = community.emoji, fontSize = 22.sp)
                Text(
                    text       = community.name,
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color      = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text  = community.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(community.url))
                    context.startActivity(intent)
                },
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text  = "Visit ->",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
