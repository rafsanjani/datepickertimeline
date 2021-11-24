package com.foreverrafs.datepickertimeline

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeRight

internal open class BaseRobot(private val rule: ComposeTestRule) {
    // Actions
    fun performClickOnNodeWithText(text: String) {
        rule.onNodeWithText(text = text, ignoreCase = true)
            .assertExists(errorMessageOnFail = "View with text: $text does not exist")
            .assertHasClickAction()
            .performClick()
    }

    fun performClickOnNodeWithTag(testTag: String) {
        rule.onNodeWithTag(testTag = testTag)
            .assertExists()
            .performClick()
    }

    fun performSwipeRightOnNodeWithTag(testTag: String, times: Int = 1) {
        rule.onNodeWithTag(testTag = testTag)
            .assertExists()
            .performTouchInput {
                repeat(times) {
                    swipeRight()
                }
            }
    }

    // Assertions
    fun verifyNodeWithTextIsDisplayed(
        text: String,
        ignoreCase: Boolean = true,
    ) {
        rule.onNodeWithText(text = text, ignoreCase = true)
            .assertIsDisplayed()
    }

    fun verifyNodeWithTagIsDisplayed(
        testTag: String,
    ) {
        rule.onNodeWithTag(testTag = testTag)
            .assertIsDisplayed()
    }

    fun verifyNodeWithTagIsNotDisplayed(
        testTag: String,
    ) {
        rule.onNodeWithTag(testTag = testTag)
            .assertDoesNotExist()
    }

    fun verifyNodeWithTextIsNotDisplayed(
        text: String,
        ignoreCase: Boolean = true,
    ) {
        rule.onNodeWithText(text = text, ignoreCase = true)
            .assertExists()
            .assertIsNotDisplayed()
    }
}
