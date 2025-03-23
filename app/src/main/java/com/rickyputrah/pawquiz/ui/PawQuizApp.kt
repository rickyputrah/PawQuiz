package com.rickyputrah.pawquiz.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.rickyputrah.pawquiz.ui.home.HomeRoute
import com.rickyputrah.pawquiz.ui.home.home
import com.rickyputrah.pawquiz.ui.question.question
import com.rickyputrah.pawquiz.ui.result.result

@Composable
fun PawQuizApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = HomeRoute) {
        home(navController = navController)
        question(navController = navController)
        result(navController = navController)
    }
}