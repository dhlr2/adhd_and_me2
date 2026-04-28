package com.example.adhd_and_me2.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LandingScreen(
    modifier: Modifier = Modifier,
    greeting: String = "Welcome",
    contextualMessage: String = "You're in the right place.",
    onCardClick: (String) -> Unit,
    onResetOnboarding: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Surface(
            color    = MaterialTheme.colorScheme.secondaryContainer,
            shape    = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text       = greeting,
                    style      = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color      = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text  = contextualMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LandingCard(
                title    = "Focus Tools",
                emoji    = "🔍",
                modifier = Modifier.weight(1f),
                onClick  = { onCardClick("focus") }
            )
            LandingCard(
                title    = "Learn ADHD",
                emoji    = "🧠",
                modifier = Modifier.weight(1f),
                onClick  = { onCardClick("learn") }
            )
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LandingCard(
                title    = "Daily Planner",
                emoji    = "📆",
                modifier = Modifier.weight(1f),
                onClick  = { onCardClick("planner") }
            )
            LandingCard(
                title    = "Community",
                emoji    = "🧑‍🧑‍🧒‍🧒",
                modifier = Modifier.weight(1f),
                onClick  = { onCardClick("community") }
            )
        }

        // reset shared preferences for testing
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            TextButton(onClick = onResetOnboarding) {
                Text(
                    text           = "Reset onboarding (testing only)",
                    fontSize       = 11.sp,
                    color          = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}

@Composable
fun LandingCard(
    title: String,
    emoji: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier  = modifier.fillMaxHeight(),
        shape     = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors    = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onClick
    ) {
        Box(
            modifier         = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = emoji, style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text       = title,
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color      = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
