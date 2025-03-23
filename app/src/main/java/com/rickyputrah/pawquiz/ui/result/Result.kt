package com.rickyputrah.pawquiz.ui.result

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.toRoute
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.rickyputrah.pawquiz.R
import com.rickyputrah.pawquiz.navigation.Route
import com.rickyputrah.pawquiz.ui.home.HomeRoute
import com.rickyputrah.pawquiz.ui.home.navigateToHome
import com.rickyputrah.pawquiz.ui.theme.PawQuizTheme
import com.rickyputrah.pawquiz.util.ReferencePreviewDevicesLightDarkMode
import kotlinx.serialization.Serializable

@Serializable
private data class ResultRoute(val finalScore: Int, val isNewHighScore: Boolean) : Route

fun NavGraphBuilder.result(navController: NavController) {
    composable<ResultRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<ResultRoute>()
        ResultScreen(
            finalScore = route.finalScore,
            isNewHighScore = route.isNewHighScore,
            onPlayAgainClicked = {
                navController.navigateToHome {
                    popUpTo(HomeRoute) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
            }
        )
    }
}

fun NavController.navigateToResult(
    finalScore: Int,
    isNewHighScore: Boolean,
    builder: (NavOptionsBuilder.() -> Unit)? = null
) {
    navigate(
        route = ResultRoute(finalScore = finalScore, isNewHighScore = isNewHighScore),
        builder?.let(::navOptions)
    )
}

@Composable
fun ResultScreen(
    finalScore: Int,
    isNewHighScore: Boolean,
    onPlayAgainClicked: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 40.dp)
        ) {
            val titleText = if (isNewHighScore) {
                stringResource(R.string.result_new_high_score)
            } else {
                stringResource(R.string.result_score)
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = titleText,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayLarge,
                text = finalScore.toString()
            )

            Box(
                modifier = Modifier.weight(1f)
            ) {
                val highScoreComposition by rememberLottieComposition(
                    LottieCompositionSpec.RawRes(
                        if (isNewHighScore) R.raw.high_score else R.raw.not_high_score
                    )
                )
                LottieAnimation(
                    composition = highScoreComposition,
                    iterations = Int.MAX_VALUE,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Button(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .heightIn(min = 56.dp),
                onClick = onPlayAgainClicked,
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically),
                    text = stringResource(R.string.result_button_play_again),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@ReferencePreviewDevicesLightDarkMode
@Composable
private fun PreviewResultNewHighScoreScreen() {
    PawQuizTheme {
        ResultScreen(
            finalScore = 10,
            isNewHighScore = true,
            onPlayAgainClicked = {}
        )
    }
}

@ReferencePreviewDevicesLightDarkMode
@Composable
private fun PreviewResultHighScoreScreen() {
    PawQuizTheme {
        ResultScreen(
            finalScore = 10,
            isNewHighScore = false,
            onPlayAgainClicked = {}
        )
    }
}