package com.example.adhd_and_me2.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private data class FocusTask(
    val id: Int,
    val label: String,
    val emoji: String,
    val durationSeconds: Int
)

private val presetTasks = listOf(
    FocusTask(1,  "Have a shower",          "🚿", 10 * 60),
    FocusTask(2,  "Wash the dishes",        "🍽️", 10 * 60),
    FocusTask(3,  "Read a page of a book",  "📖",  5 * 60),
    FocusTask(4,  "Make your bed",          "🛌",  5 * 60),
    FocusTask(5,  "Take out the bins",      "🗑️",  5 * 60)
)

@Composable
fun FocusScreen(modifier: Modifier = Modifier) {
    var activeTaskId by remember { mutableIntStateOf(-1) }
    var timeLeft     by remember { mutableIntStateOf(0) }
    var isRunning    by remember { mutableStateOf(false) }

    //Countdown
    LaunchedEffect(isRunning, activeTaskId) {
        while (isRunning && timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
        if (timeLeft == 0 && isRunning) {
            isRunning    = false
            activeTaskId = -1
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Text(
            text       = "Focus Tools",
            style      = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color      = MaterialTheme.colorScheme.onBackground,
            modifier   = Modifier.padding(vertical = 12.dp)
        )
        Text(
            text     = "Pick a task, start the timer, and just focus on that one thing. You can only do one at a time",
            style    = MaterialTheme.typography.bodyMedium,
            color    = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Active card
        if (activeTaskId != -1) {
            val activeTask = presetTasks.find { it.id == activeTaskId }
            if (activeTask != null) {
                ActiveTimerCard(
                    task      = activeTask,
                    timeLeft  = timeLeft,
                    isRunning = isRunning,
                    onPause   = { isRunning = false },
                    onResume  = { isRunning = true },
                    onCancel  = {
                        isRunning    = false
                        activeTaskId = -1
                        timeLeft     = 0
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // List of tasks
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(presetTasks, key = { it.id }) { task ->
                TaskItem(
                    task     = task,
                    isActive = task.id == activeTaskId,
                    onStart  = {
                        isRunning    = false
                        activeTaskId = task.id
                        timeLeft     = task.durationSeconds
                        isRunning    = true
                    }
                )
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

// Active timer card
@Composable
private fun ActiveTimerCard(
    task: FocusTask,
    timeLeft: Int,
    isRunning: Boolean,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onCancel: () -> Unit
) {
    val progress by animateFloatAsState(
        targetValue = timeLeft.toFloat() / task.durationSeconds.toFloat(),
        label       = "timerProgress"
    )

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60

    Card(
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier            = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text       = "${task.emoji}  ${task.label}",
                style      = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color      = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text       = "%02d:%02d".format(minutes, seconds),
                fontSize   = 48.sp,
                fontWeight = FontWeight.Bold,
                color      = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            //https://m3.material.io/components/progress-indicators/overview
            LinearProgressIndicator(
                progress   = { progress },
                modifier   = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color      = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                strokeCap  = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onCancel,
                    shape   = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = if (isRunning) onPause else onResume,
                    shape   = RoundedCornerShape(12.dp),
                    colors  = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(if (isRunning) "Pause" else "Resume")
                }
            }
        }
    }
}

//Task
@Composable
private fun TaskItem(
    task: FocusTask,
    isActive: Boolean,
    onStart: () -> Unit
) {
    Card(
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(
            containerColor = if (isActive)
                MaterialTheme.colorScheme.secondaryContainer
            else
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier              = Modifier.weight(1f)
            ) {
                Text(text = task.emoji, fontSize = 22.sp)
                Column {
                    Text(
                        text       = task.label,
                        style      = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                        color      = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text  = "${task.durationSeconds / 60} min",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Button(
                onClick        = onStart,
                shape          = RoundedCornerShape(10.dp),
                colors         = ButtonDefaults.buttonColors(
                    containerColor = if (isActive)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.primaryContainer
                ),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text  = if (isActive) "Active" else "Start",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isActive)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}
