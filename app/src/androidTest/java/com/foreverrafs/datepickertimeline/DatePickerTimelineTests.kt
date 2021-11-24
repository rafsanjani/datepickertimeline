package com.foreverrafs.datepickertimeline

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.foreverrafs.datepicker.DatePickerTimeline
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
    fun May2020Content() {
        val state = rememberDatePickerState(initialDate = LocalDate.of(2020, 5, 12))

        Column {
            DatePickerTimeline(
                onDateSelected = {
                },
                state = state
            )

            // With a button to manipulate the date
            Button(
                modifier = Modifier.testTag("button"),
                onClick = {
                    // Move to 20th June, 2020
                    state.smoothScrollToDate(
                        LocalDate.of(2020, 6, 20)
                    )
                }
            ) {
                Text(text = "Click")
            }
        }
    }

    @Test
    fun verifyTimeLineAppearsCorrectly() {
        setContent {
            May2020Content()
        }

        datePickerTimeLineRobot {
            performClickOnNodeWithTag(testTag = "button")
            verifyDateIsDisplayed(date = "20/06/2020")
        }
    }

    @Test
    fun verifyTodayTextComesBackToToday() {
        setContent {
            May2020Content()
        }

        datePickerTimeLineRobot {
            // Given the original date of 12/05/2020
            verifyDateIsDisplayed(date = "12/05/2020")

            // And I swipe right on the calendar three times
            performSwipeRightOnNodeWithTag(testTag = "datepickertimeline", times = 3)

            // Verify that the original date goes out of view
            verifyDateIsNotDisplayed(date = "12/05/2020")

            // And when I click on the 'Today' text
            performClickOnNodeWithText(text = "Today")

            // Verify that today's date comes into view
            verifyDateIsDisplayed(date = dateFormatter.format(LocalDate.now()))
        }
    }
}
