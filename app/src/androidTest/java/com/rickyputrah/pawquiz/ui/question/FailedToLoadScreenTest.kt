package com.rickyputrah.pawquiz.ui.question

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.rickyputrah.pawquiz.R
import com.rickyputrah.pawquiz.ui.theme.PawQuizTheme
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class FailedToLoadScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val onTryAgainClicked: () -> Unit = mockk(relaxed = true)


    private fun setComposeContent() {
        composeTestRule.setContent {
            PawQuizTheme {
                FailedToLoadScreen(onTryAgainClicked = onTryAgainClicked)
            }
        }
    }

    @Test
    fun verifyFailedToLoadQuestion() {
        setComposeContent()

        composeTestRule.onNodeWithTag(FAILED_TO_LOAD_IMAGE_TEST_TAG)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.failed_to_load_text))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.failed_to_load_try_again))
            .assertIsDisplayed()
            .performClick()

        verify { onTryAgainClicked.invoke() }
    }
}