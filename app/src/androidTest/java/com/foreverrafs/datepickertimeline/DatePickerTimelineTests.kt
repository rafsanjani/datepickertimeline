package com.foreverrafs.datepickertimeline

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.foreverrafs.datepicker.DatePickerTimeline
import com.foreverrafs.datepicker.state.rememberDatePickerState
import org.junit.Test
import java.time.LocalDate

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@ExperimentalComposeUiApi
internal class DatePickerTimelineTests : BaseTest() {
    @Composable
    fun June2020Content() {
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
            June2020Content()
        }

        datePickerTimeLineRobot {
            clickButtonWithTag(tag = "button")
            verifyDateIsDisplayed(LocalDate.of(2020, 6, 20))
        }
    }
}
