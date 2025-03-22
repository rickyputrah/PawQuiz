package com.rickyputrah.pawquiz.ui.question

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.rickyputrah.pawquiz.R
import com.rickyputrah.pawquiz.domain.DogBreed
import com.rickyputrah.pawquiz.domain.Question
import com.rickyputrah.pawquiz.navigation.Route
import com.rickyputrah.pawquiz.ui.loading.LoadingScreen
import com.rickyputrah.pawquiz.ui.theme.PawQuizTheme
import com.rickyputrah.pawquiz.util.LocalImageLoader
import com.rickyputrah.pawquiz.util.ReferencePreviewDevicesLightDarkMode
import kotlinx.serialization.Serializable
import kotlin.random.Random

const val DOG_IMAGE_TEST_TAG = "DOG_IMAGE_TEST_TAG"
const val FAILED_TO_LOAD_IMAGE_TEST_TAG = "FAILED_TO_LOAD_IMAGE_TEST_TAG"

@Serializable
private data object QuestionRoute : Route

fun NavGraphBuilder.question() {
    composable<QuestionRoute> {
        BackHandler {
            // Disable back click when in Game
        }
        val viewModel = hiltViewModel<QuestionViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        val question = uiState.question
        if (uiState.isLoading) {
            LoadingScreen()
        } else if (question != null) {
            QuestionScreen(
                promptNumber = uiState.promptNumber,
                question = question,
                onCardSelected = viewModel::onQuestionAnswered
            )

            val correctAnswerLottie by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.correct_answer_animation))
            if (uiState.isSuccess) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.surfaceDim.copy(alpha = 0.5f))
                ) {
                    LottieAnimation(
                        composition = correctAnswerLottie,
                        iterations = Int.MAX_VALUE,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else if (uiState.isWrongAnswer) {
                // TODO navigate to result/score page
            }
        } else {
            FailedToLoadScreen(
                onTryAgainClicked = viewModel::fetchQuestion
            )
        }
    }
}

fun NavController.navigateToQuestion(builder: (NavOptionsBuilder.() -> Unit)? = null) {
    navigate(QuestionRoute, builder?.let(::navOptions))
}

@Composable
internal fun QuestionScreen(
    promptNumber: Int,
    question: Question,
    onCardSelected: (Question, DogBreed) -> Unit,
) {
    Scaffold { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 20.dp)
        ) {
            Text(
                modifier = Modifier.padding(top = 40.dp),
                text = getQuestionPrompt(promptNumber),
                style = MaterialTheme.typography.headlineSmall,
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(top = 30.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                val request = ImageRequest.Builder(LocalContext.current)
                    .data(question.imageUrl)
                    .size(512)
                    .crossfade(true)
                    .build()
                AsyncImage(
                    model = request,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.loader_icon),
                    error = painterResource(R.drawable.ic_broken_image),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(DOG_IMAGE_TEST_TAG)
                        .heightIn(min = 150.dp, max = 300.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    imageLoader = LocalImageLoader.current,
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
            question.options.forEach { option ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 36.dp)
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder(),
                    onClick = { onCardSelected(question, option) }
                ) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        text = option.name,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
internal fun FailedToLoadScreen(
    onTryAgainClicked: () -> Unit
) {
    Scaffold { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(vertical = 20.dp)
        ) {

            Column(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_error_load),
                    modifier = Modifier
                        .testTag(FAILED_TO_LOAD_IMAGE_TEST_TAG)
                        .size(300.dp)
                        .align(Alignment.CenterHorizontally),
                    contentDescription = null,
                )
                Text(
                    text = stringResource(R.string.failed_to_load_text),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                )
            }
            Button(
                modifier = Modifier
                    .padding(20.dp)
                    .heightIn(min = 56.dp),
                onClick = onTryAgainClicked,
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically),
                    text = stringResource(R.string.failed_to_load_try_again),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun getQuestionPrompt(promptNumber: Int): String {
    val stringRes = when (promptNumber) {
        1 -> R.string.question_prompt_1
        2 -> R.string.question_prompt_2
        3 -> R.string.question_prompt_3
        4 -> R.string.question_prompt_4
        5 -> R.string.question_prompt_5
        6 -> R.string.question_prompt_6
        7 -> R.string.question_prompt_7
        8 -> R.string.question_prompt_8
        9 -> R.string.question_prompt_9
        10 -> R.string.question_prompt_10
        11 -> R.string.question_prompt_11
        12 -> R.string.question_prompt_12
        13 -> R.string.question_prompt_13
        else -> R.string.question_prompt_1
    }
    return stringResource(stringRes)
}

@ReferencePreviewDevicesLightDarkMode
@Composable
private fun PreviewQuestionScreen() {
    PawQuizTheme {
        QuestionScreen(
            question = Question(
                options = listOf(
                    DogBreed(name = "Airedale", code = "airedale"),
                    DogBreed(name = "Akita", code = "akita"),
                    DogBreed(name = "Kelpie Australian", code = "australian/kelpie"),
                    DogBreed(name = "Shepherd Australian", code = "australian/shepherd"),
                ), correctOption = DogBreed(name = "Akita", code = "akita"), imageUrl = ""
            ),
            onCardSelected = { question: Question, breed: DogBreed -> },
            promptNumber = Random.nextInt(1, 10)
        )
    }
}

@ReferencePreviewDevicesLightDarkMode
@Composable
private fun PreviewFailedToLoadScreen() {
    PawQuizTheme {
        FailedToLoadScreen { }
    }
}