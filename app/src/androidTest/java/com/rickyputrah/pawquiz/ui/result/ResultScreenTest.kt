package com.rickyputrah.pawquiz.ui.result

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.rickyputrah.pawquiz.R
import com.rickyputrah.pawquiz.ui.theme.PawQuizTheme
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class ResultScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val onPlayAgainClicked: () -> Unit = mockk(relaxed = true)


    private fun setComposeContent(score: Int, isNewHighScore: Boolean) {
        composeTestRule.setContent {
            PawQuizTheme {
                ResultScreen(
                    finalScore = score,
                    isNewHighScore = isNewHighScore,
                    onPlayAgainClicked = onPlayAgainClicked
                )
            }
        }
    }

    @Test
    fun verifyNotNewHighScoreResultScreen() {
        setComposeContent(
            score = 10,
            isNewHighScore = false
        )
        composeTestRule.onNodeWithText(context.getString(R.string.result_score))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("10")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.result_button_play_again))
            .assertIsDisplayed()
            .performClick()
        verify { onPlayAgainClicked.invoke() }
    }

    @Test
    fun verifyNewHighScoreResultScreen() {
        setComposeContent(
            score = 12,
            isNewHighScore = true
        )

        composeTestRule.onNodeWithText(context.getString(R.string.result_new_high_score))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("12")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.result_button_play_again))
            .assertIsDisplayed()
            .performClick()
        verify { onPlayAgainClicked.invoke() }
    }
}