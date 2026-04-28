package com.example.adhd_and_me2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.adhd_and_me2.data.OnboardingAnswers
import com.example.adhd_and_me2.data.OnboardingPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OnboardingViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = OnboardingPreferences(application)

    private val _answers = MutableStateFlow(prefs.load())
    val answers: StateFlow<OnboardingAnswers> = _answers

    fun hasCompletedOnboarding(): Boolean = prefs.hasCompleted()

    fun saveAnswers(answers: OnboardingAnswers) {
        _answers.value = answers
        prefs.save(answers)
    }

    // Clears data and resets it. Testing purposes
    fun resetOnboarding() {
        prefs.clear()
        _answers.value = OnboardingAnswers()
    }

    fun contextualMessage(): String {
        return when {
            _answers.value.currentFeeling.contains("Relieved", ignoreCase = true) ->
                "Feeling relieved? It's the start of something good."
            _answers.value.currentFeeling.contains("Overwhelmed", ignoreCase = true) ->
                "Take it one step at a time. You don't have to figure it all out today."
            _answers.value.currentFeeling.contains("Hopeful", ignoreCase = true) ->
                "Your curiosity is your superpower. Let's explore together."
            _answers.value.currentFeeling.contains("Uncertain", ignoreCase = true) ->
                "It's okay not to have all the answers yet. We'll work through it together."
            else ->
                "However you're feeling right now — you're in the right place."
        }
    }
}
