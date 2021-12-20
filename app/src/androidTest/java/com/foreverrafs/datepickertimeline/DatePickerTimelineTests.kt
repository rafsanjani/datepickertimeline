package com.foreverrafs.datepickertimeline

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.foreverrafs.datepicker.DatePickerTimeline
import com.foreverrafs.datepicker.state.DatePickerState
import com.foreverrafs.datepicker.state.rememberDatePickerState
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
internal class DatePickerTimelineTests : BaseTest() {
    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    @Composable
    fun TestDatePickerContent(
        state: DatePickerState,
        pastDaysCount: Int = 180,
        onButtonClicked: () -> Unit = {}
    ) {
        Column {
            DatePickerTimeline(
                onDateSelected = {
                },
                state = state,
                pastDaysCount = pastDaysCount,
                todayLabel = {
                    Text(text = "Today")
                }
            )

            // With a button to manipulate the date
            Button(
                modifier = Modifier.testTag("button"),
                onClick = onButtonClicked
            ) {
                Text(text = "Click")
            }
        }
    }

    @Test
    fun verifyTimeLineAppearsCorrectly() {
        var state: DatePickerState?
        val initialDate = LocalDate.of(2020, 5, 12)

        setContent {
            state = rememberDatePickerState(initialDate)

            state?.let { state ->
                TestDatePickerContent(state = state, onButtonClicked = {
                    // Move to 20th June, 2020
                    state.smoothScrollToDate(
                        LocalDate.of(2020, 6, 20)
                    )
                })
            }
        }

        datePickerTimeLineRobot {
            verifyDateIsDisplayed(date = "12/05/2020")

            performClickOnNodeWithTag(testTag = "button")
            verifyDateIsDisplayed(date = "20/06/2020")
        }
    }

    @Test
    fun verifyClickingOnTodayTextScrollsToToday() {
        setContent {
            TestDatePickerContent(
                state = rememberDatePickerState(
                    initialDate = LocalDate.of(
                        2021,
                        5,
                        12
                    )
                )
            )
        }

        datePickerTimeLineRobot {
            // Given the original date of 12/05/2021
            verifyDateIsDisplayed(date = "12/05/2021")

            // And I swipe right on the calendar three times
            performSwipeRightOnNodeWithTag(testTag = "datepickertimeline", times = 3)

            // Verify that the original date goes out of view
            verifyDateIsNotDisplayed(date = "12/05/2021")

            // And when I click on the 'Today' text
            performClickOnNodeWithText(text = "Today")

            // Verify that today's date comes into view
            verifyDateIsDisplayed(date = dateFormatter.format(LocalDate.now()))
        }
    }

    @Test
    fun verifyStateManipulationWorksAsExpected() {
        var state: DatePickerState? = null

        setContent {
            state = rememberDatePickerState(initialDate = LocalDate.of(2021, 12, 12))

            TestDatePickerContent(state = state!!, pastDaysCount = 180)
        }
        datePickerTimeLineRobot {
            // Programmatically change state to another date
            state?.smoothScrollToDate(LocalDate.of(2022, 5, 12))
            verifyDateIsDisplayed(date = "12/05/2022")
        }
    }

    @Test
    fun verifyInvalidPastDateSelectsFirstDate() {
        var state: DatePickerState? = null
        val initialDate = LocalDate.of(2021, 12, 12)
        val pastDaysCount = 10

        setContent {
            state = rememberDatePickerState(initialDate = initialDate)

            TestDatePickerContent(state = state!!, pastDaysCount = pastDaysCount)
        }

        datePickerTimeLineRobot {
            // Scroll to an invalid date
            state?.smoothScrollToDate(LocalDate.of(2017, 1, 1))

            val requiredDate = initialDate.minusDays(pastDaysCount.toLong())

            verifyDateIsDisplayed(date = dateFormatter.format(requiredDate))
        }
    }
}
