package com.example.adhd_and_me2.navigation

sealed class Screen(val route: String) {
    object Splash      : Screen("splash")
    object Onboarding  : Screen("onboarding")
    object Landing     : Screen("landing")


    object Focus       : Screen("focus")
    object Learn       : Screen("learn")
    object Planner     : Screen("planner")
    object Community   : Screen("community")
}