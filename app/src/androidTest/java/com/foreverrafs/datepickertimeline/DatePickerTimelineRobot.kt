package com.foreverrafs.datepickertimeline

import androidx.compose.ui.test.junit4.ComposeTestRule
import java.time.LocalDate

internal fun BaseTest.datePickerTimeLineRobot(
    func: DatePickerTimelineRobot.() -> Unit
) =
    DatePickerTimelineRobot(rule).apply {
        rule.waitForIdle()
        func()
    }

internal class DatePickerTimelineRobot(rule: ComposeTestRule) : BaseRobot(rule) {

    fun performClickOnDate(date: LocalDate) {
        performClickOnNodeWithText(date.dayOfMonth.toString())
    }

    fun verifyDateIsDisplayed(date: String) {
        verifyNodeWithTagIsDisplayed(testTag = date)
    }

    fun verifyDateIsNotDisplayed(date: String) {
        verifyNodeWithTagIsNotDisplayed(testTag = date)
    }
}
