package com.example.adhd_and_me2

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppScaffold(
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        topBar = {
            AppHeader()
        }
    ) { innerPadding ->
        content(Modifier.padding(innerPadding))
    }
}
