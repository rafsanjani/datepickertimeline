package com.foreverrafs.datepicker

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.foreverrafs.datepicker.state.DatePickerState
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.test.Test

class DatePickerStateTest {
    @Test
    fun `test initial date is equal to selected date`() {
        val selectedDate = LocalDate(2021, 12, 12)
        val state =
            DatePickerState(
                selectedDate = selectedDate,
                shouldScrollToSelectedDate = true,
            )

        assertThat(state.selectedDate).isEqualTo(selectedDate)
    }

    @Test
    fun `test scrolling flag resets after scrolling`() {
        val state =
            DatePickerState(
                selectedDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
                shouldScrollToSelectedDate = true,
            )

        state.onScrollCompleted()

        assertThat(state.shouldScrollToSelectedDate).isEqualTo(false)
    }

    @Test
    fun `test scrolling actually occurs during smooth scrolling`() {
        val originalDate = LocalDate(2021, 12, 12)
        val state =
            DatePickerState(
                selectedDate = originalDate,
                shouldScrollToSelectedDate = false,
            )

        val newDate = LocalDate(2021, 5, 5)
        state.smoothScrollToDate(date = newDate)

        assertThat(state.shouldScrollToSelectedDate).isEqualTo(true)
        assertThat(state.selectedDate).isEqualTo(newDate)
    }
}
