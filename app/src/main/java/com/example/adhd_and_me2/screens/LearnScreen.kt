package com.example.adhd_and_me2.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class SymptomCard(
    val title: String,
    val emoji: String,
    val description: String,
    val whatHelps: String
)

private val symptoms = listOf(
    SymptomCard(
        title       = "Time Blindness",
        emoji       = "⏰",
        description = "People with ADHD often struggle to sense how much time is passing. " +
                      "Tasks can feel like they take minutes when hours have gone by, " +
                      "making punctuality and deadlines genuinely difficult.",
        whatHelps   = "Use visible timers (a clock you can see), set alarms " +
                      "and break tasks into timed chunks. The Pomodoro technique works well."
    ),
    SymptomCard(
        title       = "Hyperfocus",
        emoji       = "🔍",
        description = "ADHD isn't always about being distracted — sometimes the brain locks " +
                      "onto something interesting so intensely that everything else disappears. " +
                      "Hours can pass without noticing hunger, tiredness, or other responsibilities.",
        whatHelps   = "Use alarms to break out of hyperfocus sessions. Schedule enjoyable " +
                      "tasks after responsibilities so hyperfocus becomes a reward, not an escape."
    ),
    SymptomCard(
        title       = "Emotional Dysregulation",
        emoji       = "😱",
        description = "Emotions in ADHD can feel bigger and harder to control than for others. " +
                      "Frustration, excitement, or rejection can hit suddenly and intensely, " +
                      "sometimes called Rejection Sensitive Dysphoria (RSD).",
        whatHelps   = "Name the emotion out loud or in writing — it reduces its intensity. " +
                      "Give yourself a short pause before reacting. Therapy, especially CBT, " +
                      "can also help build emotional regulation skills."
    ),
    SymptomCard(
        title       = "Working Memory Struggles",
        emoji       = "🧩",
        description = "Working memory is the brain's short-term scratchpad. In ADHD it's " +
                      "often unreliable — you might forget what you were doing mid-task, " +
                      "lose track of conversations, or walk into a room and forget why.",
        whatHelps   = "Write everything down immediately. Use to-do lists, voice memos, " +
                      "and reminders liberally. Don't rely on remembering — externalise it."
    ),
    SymptomCard(
        title       = "Task Initiation",
        emoji       = "🧳",
        description = "Starting a task — especially a boring or overwhelming one — can feel " +
                      "almost impossible with ADHD, even when you genuinely want to do it. " +
                      "This isn't laziness; the ADHD brain struggles to self-start without " +
                      "interest, urgency, or external pressure.",
        whatHelps   = "The two-minute rule helps: just do two minutes of the task. " +
                      "Body doubling (working alongside someone else) is also very effective, " +
                      "as is working to music or background noise."
    ),
    SymptomCard(
        title       = "Impulsivity",
        emoji       = "⚡️",
        description = "Acting or speaking before thinking is a core ADHD trait. This can " +
                      "show up as interrupting conversations, making quick decisions without " +
                      "considering consequences, or difficulty waiting.",
        whatHelps   = "Build in a deliberate pause before responding or deciding. " +
                      "Write down the impulse rather than acting on it immediately — " +
                      "revisit it after 10 minutes to see if it still feels urgent."
    ),
    SymptomCard(
        title       = "Sensory Sensitivity",
        emoji       = "🎧",
        description = "Many people with ADHD are easily overwhelmed by noise, light, " +
                      "textures, or crowds. This isn't a separate condition — sensory " +
                      "processing differences are common in ADHD and can make everyday " +
                      "environments exhausting.",
        whatHelps   = "Noise-cancelling headphones, fidget tools, and reducing clutter " +
                      "in your workspace can all help. Identify your personal triggers " +
                      "and reduce exposure where possible."
    ),
    SymptomCard(
        title       = "Sleep Difficulties",
        emoji       = "😴",
        description = "ADHD and sleep problems often go hand in hand. A racing mind at " +
                      "bedtime, delayed sleep phase (naturally wanting to sleep and wake later), " +
                      "and difficulty winding down are all common.",
        whatHelps   = "A consistent wind-down routine matters more than a strict bedtime. " +
                      "Reduce screens before bed, try white noise, and avoid caffeine " +
                      "after midday. Some find exercise earlier in the day helps significantly."
    )
)

@Composable
fun LearnScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Text(
            text       = "Understanding ADHD",
            style      = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color      = MaterialTheme.colorScheme.onBackground,
            modifier   = Modifier.padding(vertical = 12.dp)
        )
        Text(
            text     = "ADHD looks different for everyone. Here are some of the most common " +
                       "experiences — and what can help.",
            style    = MaterialTheme.typography.bodyMedium,
            color    = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(symptoms) { symptom ->
                SymptomCardItem(symptom)
            }
            // Bottom padding so last card isn't against the nav bar
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun SymptomCardItem(symptom: SymptomCard) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape     = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors    = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick   = { expanded = !expanded },
        modifier  = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = symptom.emoji, fontSize = 22.sp)
                    Text(
                        text       = symptom.title,
                        style      = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color      = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text  = if (expanded) "▲" else "▼",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text  = symptom.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                // What helps
                Surface(
                    color  = MaterialTheme.colorScheme.primaryContainer,
                    shape  = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text       = "💡 What helps",
                            style      = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color      = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text  = symptom.whatHelps,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}
