package com.rickyputrah.pawquiz.ui.question

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.rickyputrah.pawquiz.navigation.Route
import com.rickyputrah.pawquiz.ui.theme.PawQuizTheme
import com.rickyputrah.pawquiz.util.ReferencePreviewDevicesLightDarkMode
import kotlinx.serialization.Serializable

@Serializable
internal data object QuestionRoute : Route

fun NavGraphBuilder.question() {
    composable<QuestionRoute> {
        QuestionScreen()
    }
}

fun NavController.navigateToQuestion(builder: (NavOptionsBuilder.() -> Unit)? = null) {
    navigate(QuestionRoute, builder?.let(::navOptions))
}

@Composable
internal fun QuestionScreen() {
    // TODO : Create Proper Question Screen
    Scaffold { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Text("Show Question here")
        }
    }
}


@ReferencePreviewDevicesLightDarkMode
@Composable
private fun PreviewQuestionScreen() {
    PawQuizTheme {
        QuestionScreen()
    }
}