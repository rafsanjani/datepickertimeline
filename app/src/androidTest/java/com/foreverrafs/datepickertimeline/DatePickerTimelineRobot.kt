package com.foreverrafs.datepickertimeline

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import java.time.LocalDate

internal fun BaseTest.datePickerTimeLineRobot(
    func: DatePickerTimelineRobot.() -> Unit
) =
    DatePickerTimelineRobot(rule).apply {
        rule.waitForIdle()
        func()
    }

internal class DatePickerTimelineRobot(private val rule: ComposeTestRule) {

    fun clickOnDate(date: LocalDate) {
        rule.onNodeWithText(text = date.dayOfMonth.toString(), ignoreCase = true)
            .assertExists()
            .assertHasClickAction()
            .performClick()
    }

    fun verifyDateIsDisplayed(date: LocalDate) {
        rule.onNodeWithText(text = date.dayOfMonth.toString(), ignoreCase = true)
            .assertExists(errorMessageOnFail = "The date $date isn't displayed")
    }

    fun clickButtonWithTag(tag: String) {
        rule.onNodeWithTag(testTag = tag)
            .assertExists()
            .performClick()
    }
}
