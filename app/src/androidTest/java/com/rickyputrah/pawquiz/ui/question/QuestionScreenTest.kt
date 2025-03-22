package com.rickyputrah.pawquiz.ui.question

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import coil3.ImageLoader
import coil3.asImage
import coil3.test.FakeImageLoaderEngine
import com.rickyputrah.pawquiz.R
import com.rickyputrah.pawquiz.domain.DogBreed
import com.rickyputrah.pawquiz.domain.Question
import com.rickyputrah.pawquiz.ui.theme.PawQuizTheme
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class QuestionScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private lateinit var imageLoader: ImageLoader
    private val onCardSelected: (Question, DogBreed) -> Unit = mockk(relaxed = true)

    @Before
    fun before() {
        val bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888).apply {
            eraseColor(Color.RED)
        }
        val engine = FakeImageLoaderEngine.Builder()
            .intercept("https://example.com/image.jpg", image = bitmap.asImage())
            .build()
        imageLoader = ImageLoader.Builder(context)
            .components { add(engine) }
            .build()
    }

    private fun setComposeContent(promptNumber: Int) {
        composeTestRule.setContent {
            PawQuizTheme(imageLoader = imageLoader) {
                QuestionScreen(
                    promptNumber = promptNumber,
                    question = QUESTION,
                    onCardSelected = onCardSelected
                )
            }
        }
    }

    @Test
    fun verifyQuestionScreenInteraction() {
        setComposeContent(promptNumber = 1)
        composeTestRule.onNodeWithTag(DOG_IMAGE_TEST_TAG)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(context.getString(R.string.question_prompt_1))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(AIREDALE.name)
            .assertIsDisplayed()
            .performClick()

        verify { onCardSelected.invoke(QUESTION, AIREDALE) }

        composeTestRule
            .onNodeWithText(AKITA.name)
            .assertIsDisplayed()
            .performClick()
        verify { onCardSelected.invoke(QUESTION, AKITA) }

        composeTestRule
            .onNodeWithText(KELPIE_AUSTRALIAN.name)
            .assertIsDisplayed()
            .performClick()
        verify { onCardSelected.invoke(QUESTION, KELPIE_AUSTRALIAN) }

        composeTestRule
            .onNodeWithText(SHEPHERD_AUSTRALIAN.name)
            .assertIsDisplayed()
            .performClick()
        verify { onCardSelected.invoke(QUESTION, SHEPHERD_AUSTRALIAN) }
    }

    companion object {
        private val AIREDALE = DogBreed(name = "airedale", code = "airedale")
        private val AKITA = DogBreed(name = "akita", code = "akita")
        private val KELPIE_AUSTRALIAN =
            DogBreed(name = "kelpie australian", code = "australian/kelpie")
        private val SHEPHERD_AUSTRALIAN =
            DogBreed(name = "shepherd australian", code = "australian/shepherd")
        private val QUESTION = Question(
            options = listOf(AIREDALE, AKITA, KELPIE_AUSTRALIAN, SHEPHERD_AUSTRALIAN),
            correctOption = AKITA,
            imageUrl = "https://example.com/image.jpg"
        )
    }
}