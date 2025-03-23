package com.rickyputrah.pawquiz.ui.home

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

class HomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val onStartClicked: () -> Unit = mockk(relaxed = true)


    private fun setComposeContent(score: Int = 0) {
        composeTestRule.setContent {
            PawQuizTheme {
                HomeScreen(
                    score = score,
                    onStartClicked = onStartClicked
                )
            }
        }
    }

    @Test
    fun verifyHomeScreen_scoreZero() {
        setComposeContent(score = 0)

        composeTestRule.onNodeWithText(context.getString(R.string.welcome_title))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.welcome_subtitle))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.welcome_empty_high_score))
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(context.getString(R.string.home_button_start))
            .assertIsDisplayed()
            .performClick()
        verify { onStartClicked.invoke() }
    }

    @Test
    fun verifyHomeScreen_scoreNotZero() {
        setComposeContent(score = 10)

        composeTestRule.onNodeWithText(context.getString(R.string.welcome_title))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.welcome_subtitle))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.welcome_high_score, 10))
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(context.getString(R.string.home_button_start))
            .assertIsDisplayed()
            .performClick()
        verify { onStartClicked.invoke() }
    }

}