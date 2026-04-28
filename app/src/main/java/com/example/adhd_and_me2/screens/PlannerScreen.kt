package com.example.adhd_and_me2.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import org.json.JSONArray
import org.json.JSONObject
import androidx.core.content.edit

private data class PlannerTask(
    val id: Long,
    val label: String,
    val isCompleted: Boolean = false
)

private const val PREFS_PLANNER = "planner_prefs"
private const val KEY_TASKS     = "tasks"

private fun saveTasks(context: android.content.Context, tasks: List<PlannerTask>) {
    val array = JSONArray()
    tasks.forEach { task ->
        val obj = JSONObject()
        obj.put("id",          task.id)
        obj.put("label",       task.label)
        obj.put("isCompleted", task.isCompleted)
        array.put(obj)
    }
    context.getSharedPreferences(PREFS_PLANNER, android.content.Context.MODE_PRIVATE)
        .edit {
            putString(KEY_TASKS, array.toString())
        }
}

private fun loadTasks(context: android.content.Context): List<PlannerTask> {
    val json = context.getSharedPreferences(PREFS_PLANNER, android.content.Context.MODE_PRIVATE)
        .getString(KEY_TASKS, null) ?: return emptyList()
    val array = JSONArray(json)
    val tasks  = mutableListOf<PlannerTask>()
    for (i in 0 until array.length()) {
        val obj = array.getJSONObject(i)
        tasks.add(
            PlannerTask(
                id          = obj.getLong("id"),
                label       = obj.getString("label"),
                isCompleted = obj.getBoolean("isCompleted")
            )
        )
    }
    return tasks
}

@Composable
fun PlannerScreen(modifier: Modifier = Modifier) {
    val context      = LocalContext.current
    val focusManager = LocalFocusManager.current

    var tasks        by remember { mutableStateOf(loadTasks(context)) }
    var newTaskLabel by remember { mutableStateOf("") }
    var showClearDialog by remember { mutableStateOf(false) }

    // Save whenever tasks change
    LaunchedEffect(tasks) {
        saveTasks(context, tasks)
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title   = { Text("Clear all tasks?") },
            text    = { Text("This will remove all your tasks. This can't be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    tasks           = emptyList()
                    showClearDialog = false
                }) {
                    Text("Clear all", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {

        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(
                text       = "Daily Planner",
                style      = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color      = MaterialTheme.colorScheme.onBackground,
                modifier   = Modifier.padding(vertical = 12.dp)
            )
            if (tasks.isNotEmpty()) {
                IconButton(onClick = { showClearDialog = true }) {
                    Icon(
                        imageVector        = Icons.Default.Delete,
                        contentDescription = "Clear all tasks",
                        tint               = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Text(
            text     = "What do you need to get done today? Take it one task at a time.",
            style    = MaterialTheme.typography.bodyMedium,
            color    = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment     = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value         = newTaskLabel,
                onValueChange = { newTaskLabel = it },
                placeholder   = { Text("Add a task...") },
                singleLine    = true,
                shape         = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (newTaskLabel.isNotBlank()) {
                        tasks = tasks + PlannerTask(
                            id    = System.currentTimeMillis(),
                            label = newTaskLabel.trim()
                        )
                        newTaskLabel = ""
                        focusManager.clearFocus()
                    }
                }),
                colors   = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor      = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor    = MaterialTheme.colorScheme.secondary,
                    focusedContainerColor   = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = {
                    if (newTaskLabel.isNotBlank()) {
                        tasks = tasks + PlannerTask(
                            id    = System.currentTimeMillis(),
                            label = newTaskLabel.trim()
                        )
                        newTaskLabel = ""
                        focusManager.clearFocus()
                    }
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector        = Icons.Default.Add,
                    contentDescription = "Add task",
                    tint               = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (tasks.isNotEmpty()) {
            val completedCount = tasks.count { it.isCompleted }
            val totalCount     = tasks.size

            Surface(
                color    = MaterialTheme.colorScheme.primaryContainer,
                shape    = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier              = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        text  = "$completedCount of $totalCount tasks done",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    if (completedCount == totalCount) {
                        Text(
                            text  = "All done!",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        if (tasks.isEmpty()) {
            Box(
                modifier         = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text  = "No tasks yet — add one above!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(tasks, key = { it.id }) { task ->
                    PlannerTaskItem(
                        task       = task,
                        onToggle   = {
                            tasks = tasks.map {
                                if (it.id == task.id) it.copy(isCompleted = !it.isCompleted) else it
                            }
                        },
                        onDelete   = {
                            tasks = tasks.filter { it.id != task.id }
                        }
                    )
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun PlannerTaskItem(
    task: PlannerTask,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (task.isCompleted)
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        else
            MaterialTheme.colorScheme.surface,
        label = "taskBg"
    )

    Card(
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = bgColor),
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
            //Tick
            IconButton(onClick = onToggle) {
                Icon(
                    imageVector        = Icons.Default.Check,
                    contentDescription = "Mark done",
                    tint               = if (task.isCompleted)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                )
            }
            //task name
            Text(
                text           = task.label,
                style          = MaterialTheme.typography.bodyLarge,
                color          = if (task.isCompleted)
                    MaterialTheme.colorScheme.onSurfaceVariant
                else
                    MaterialTheme.colorScheme.onSurface,
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                modifier       = Modifier.weight(1f)
            )

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector        = Icons.Default.Delete,
                    contentDescription = "Delete task",
                    tint               = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                )
            }
        }
    }
}
