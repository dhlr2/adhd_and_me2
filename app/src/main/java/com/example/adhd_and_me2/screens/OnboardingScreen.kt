package com.example.adhd_and_me2.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adhd_and_me2.data.OnboardingAnswers

private data class OnboardingStep(
    val question: String,
    val subtitle: String,
    val options: List<String>
)

private val steps = listOf(
    OnboardingStep(
        question = "How are you feeling right now?",
        subtitle = "Whatever you're feeling is completely valid.",
        options  = listOf(
            "Relieved — it finally makes sense",
            "Overwhelmed — there's so much to take in",
            "Hopeful — I want to learn more",
            "Uncertain — I'm still processing",
            "A mix of everything"
        )
    ),
    OnboardingStep(
        question = "What matters most to you right now?",
        subtitle = "We'll use this to shape your experience.",
        options  = listOf(
            "Understanding what ADHD means for me",
            "Tools to help me focus day-to-day",
            "Planning and staying organised",
            "Connecting with others like me",
            "All of the above"
        )
    )
)

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onFinished: (OnboardingAnswers) -> Unit
) {
    var currentStep by remember { mutableIntStateOf(0) }
    var nameInput   by remember { mutableStateOf("") }
    var answers     by remember { mutableStateOf(OnboardingAnswers()) }

    AnimatedContent(
        targetState = currentStep,
        transitionSpec = {
            (slideInHorizontally { it } + fadeIn())
                .togetherWith(slideOutHorizontally { -it } + fadeOut())
        },
        label = "onboardingStep"
    ) { step ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            if (step < steps.size) {
                val s = steps[step]

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text       = s.question,
                        style      = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color      = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 36.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text  = s.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    s.options.forEach { option ->
                        val isSelected = when (step) {
                            0    -> answers.currentFeeling == option
                            else -> answers.topNeed == option
                        }
                        OptionChip(
                            text     = option,
                            selected = isSelected,
                            onClick  = {
                                answers = when (step) {
                                    0    -> answers.copy(currentFeeling = option)
                                    else -> answers.copy(topNeed = option)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                val canProceed = when (step) {
                    0    -> answers.currentFeeling.isNotEmpty()
                    else -> answers.topNeed.isNotEmpty()
                }

                NextButton(
                    enabled = canProceed,
                    label   = if (step < steps.size - 1) "Continue" else "Almost there",
                    onClick = { currentStep++ }
                )

            } else {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text       = "What should we call you?",
                        style      = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color      = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 36.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text  = "Just your first name is fine — or a nickname, whatever feels right.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedTextField(
                        value         = nameInput,
                        onValueChange = { nameInput = it },
                        placeholder   = {
                            Text("Your name", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        },
                        singleLine = true,
                        shape      = RoundedCornerShape(16.dp),
                        colors     = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor      = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor    = MaterialTheme.colorScheme.secondary,
                            focusedContainerColor   = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                NextButton(
                    enabled = nameInput.isNotBlank(),
                    label   = "Let's go",
                    onClick = { onFinished(answers.copy(name = nameInput.trim())) }
                )
            }
        }
    }
}


@Composable
private fun OptionChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bgColor     = if (selected) MaterialTheme.colorScheme.primaryContainer
                      else MaterialTheme.colorScheme.surface
    val borderColor = if (selected) MaterialTheme.colorScheme.primary
                      else MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
    val textColor   = if (selected) MaterialTheme.colorScheme.onPrimaryContainer
                      else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text       = text,
            style      = MaterialTheme.typography.bodyLarge,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color      = textColor
        )
    }
}

@Composable
private fun NextButton(
    enabled: Boolean,
    label: String,
    onClick: () -> Unit
) {
    Button(
        onClick  = onClick,
        enabled  = enabled,
        shape    = RoundedCornerShape(16.dp),
        colors   = ButtonDefaults.buttonColors(
            containerColor         = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(
            text       = label,
            style      = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color      = if (enabled) MaterialTheme.colorScheme.onPrimary
                         else MaterialTheme.colorScheme.primary
        )
    }
}
