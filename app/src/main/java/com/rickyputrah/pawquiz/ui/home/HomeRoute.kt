package com.rickyputrah.pawquiz.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.rickyputrah.pawquiz.R
import com.rickyputrah.pawquiz.navigation.Route
import com.rickyputrah.pawquiz.ui.question.navigateToQuestion
import com.rickyputrah.pawquiz.ui.theme.PawQuizTheme
import com.rickyputrah.pawquiz.util.ReferencePreviewDevicesLightDarkMode
import kotlinx.serialization.Serializable

@Serializable
internal data object HomeRoute : Route

fun NavGraphBuilder.home(
    navController: NavController,
) {
    composable<HomeRoute> {
        HomeScreen(
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
    onStartClicked: () -> Unit = {}
) {
    // TODO : Create Proper Home Screen
    Scaffold { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
        ) {
            Button(
                modifier = Modifier
                    .padding(20.dp),
                onClick = onStartClicked,
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically),
                    text = stringResource(R.string.home_button_start),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}


@ReferencePreviewDevicesLightDarkMode
@Composable
private fun PreviewHomeScreen() {
    PawQuizTheme {
        HomeScreen()
    }
}
