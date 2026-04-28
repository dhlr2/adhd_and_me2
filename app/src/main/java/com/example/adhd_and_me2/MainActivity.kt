package com.example.adhd_and_me2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.adhd_and_me2.navigation.Screen
import com.example.adhd_and_me2.screens.CommunityScreen
import com.example.adhd_and_me2.screens.FocusScreen
import com.example.adhd_and_me2.screens.LandingScreen
import com.example.adhd_and_me2.screens.LearnScreen
import com.example.adhd_and_me2.screens.OnboardingScreen
import com.example.adhd_and_me2.screens.PlannerScreen
import com.example.adhd_and_me2.screens.SplashScreen
import com.example.adhd_and_me2.ui.theme.AdhdandmeTheme
import com.example.adhd_and_me2.viewmodel.OnboardingViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AdhdandmeTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController       = rememberNavController()
    val onboardingViewModel = viewModel<OnboardingViewModel>()
    val answers             by onboardingViewModel.answers.collectAsState()

    val postSplashDestination = if (onboardingViewModel.hasCompletedOnboarding()) {
        Screen.Landing.route
    } else {
        Screen.Onboarding.route
    }

    AppScaffold { padding ->
        NavHost(
            navController    = navController,
            startDestination = Screen.Splash.route
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    onFinished = {
                        navController.navigate(postSplashDestination) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Onboarding.route) {
                OnboardingScreen(
                    modifier   = padding,
                    onFinished = { newAnswers ->
                        onboardingViewModel.saveAnswers(newAnswers)
                        navController.navigate(Screen.Landing.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Landing.route) {
                val greeting = if (answers.name.isNotEmpty()) "Welcome, ${answers.name}" else "Welcome"
                val message  = onboardingViewModel.contextualMessage()

                LandingScreen(
                    modifier          = padding,
                    greeting          = greeting,
                    contextualMessage = message,
                    onCardClick       = { route -> navController.navigate(route) },
                    onResetOnboarding = {
                        onboardingViewModel.resetOnboarding()
                        navController.navigate(Screen.Onboarding.route) {
                            popUpTo(Screen.Landing.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Learn.route)     { LearnScreen(modifier = padding) }
            composable(Screen.Focus.route)     { FocusScreen(modifier = padding) }
            composable(Screen.Community.route) { CommunityScreen(modifier = padding) }
            composable(Screen.Planner.route)   { PlannerScreen(modifier = padding) }
        }
    }
}
