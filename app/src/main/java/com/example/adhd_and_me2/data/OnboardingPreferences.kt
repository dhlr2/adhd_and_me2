package com.example.adhd_and_me2.data

import android.content.Context
import android.content.SharedPreferences

class OnboardingPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun save(answers: OnboardingAnswers) {
        prefs.edit()
            .putString(KEY_NAME,        answers.name)
            .putString(KEY_FEELING,     answers.currentFeeling)
            .putString(KEY_TOP_NEED,    answers.topNeed)
            .putBoolean(KEY_COMPLETED,  true)
            .apply()
    }

    fun load(): OnboardingAnswers = OnboardingAnswers(
        currentFeeling = prefs.getString(KEY_FEELING,  "") ?: "",
        topNeed        = prefs.getString(KEY_TOP_NEED, "") ?: "",
        name           = prefs.getString(KEY_NAME,     "") ?: ""
    )

    fun hasCompleted(): Boolean = prefs.getBoolean(KEY_COMPLETED, false)

    fun clear() = prefs.edit().clear().apply()

    companion object {
        private const val PREFS_NAME    = "adhd_and_me_prefs"
        private const val KEY_NAME      = "user_name"
        private const val KEY_FEELING   = "current_feeling"
        private const val KEY_TOP_NEED  = "top_need"
        private const val KEY_COMPLETED = "onboarding_completed"
    }
}
