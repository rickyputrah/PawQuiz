package com.rickyputrah.pawquiz.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.rickyputrah.pawquiz.R
import com.rickyputrah.pawquiz.navigation.Route
import com.rickyputrah.pawquiz.ui.question.navigateToQuestion
import com.rickyputrah.pawquiz.ui.theme.PawQuizTheme
import com.rickyputrah.pawquiz.ui.theme.primaryLight
import com.rickyputrah.pawquiz.util.ReferencePreviewDevicesLightDarkMode
import kotlinx.serialization.Serializable

@Serializable
internal data object HomeRoute : Route

fun NavGraphBuilder.home(
    navController: NavController,
) {
    composable<HomeRoute> {
        val viewModel = hiltViewModel<HomeViewModel>()
        val score by viewModel.score.collectAsStateWithLifecycle()
        HomeScreen(
            score = score,
            onStartClicked = {
                navController.navigateToQuestion()
            }
        )
    }
}

fun NavController.navigateToHome(builder: (NavOptionsBuilder.() -> Unit)? = null) {
    navigate(HomeRoute, builder?.let(::navOptions))
}

@Composable
internal fun HomeScreen(
    score: Int,
    onStartClicked: () -> Unit = {}
) {
    Scaffold { contentPadding ->
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.home_background),
            contentDescription = "",
            contentScale = ContentScale.Crop,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colorStops = arrayOf(
                            0f to Color.Black.copy(0.5f),
                            1f to primaryLight.copy(0.5f)
                        )
                    ),
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 30.dp)
                .padding(bottom = 40.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.welcome_title),
                style = MaterialTheme.typography.displaySmall,
                color = Color.White,
                fontWeight = FontWeight.W900,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                text = stringResource(R.string.welcome_subtitle),
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Button(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .heightIn(min = 56.dp),
                onClick = onStartClicked,
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically),
                    text = stringResource(R.string.home_button_start),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp),
                text = if (score == 0) stringResource(R.string.welcome_empty_high_score) else stringResource(
                    R.string.welcome_high_score,
                    score
                ),
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}


@ReferencePreviewDevicesLightDarkMode
@Composable
private fun PreviewHomeScreen() {
    PawQuizTheme {
        HomeScreen(
            score = 0,
            onStartClicked = {}
        )
    }
}
