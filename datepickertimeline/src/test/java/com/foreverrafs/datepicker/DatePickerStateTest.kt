package com.foreverrafs.datepicker

import com.foreverrafs.datepicker.state.DatePickerState
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.LocalDate

class DatePickerStateTest {
    @Test
    fun `test initial date is equal to selected date`() {
        val selectedDate = LocalDate.of(2021, 12, 12)
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
                selectedDate = LocalDate.now(),
                shouldScrollToSelectedDate = true,
            )

        state.onScrollCompleted()

        assertThat(state.shouldScrollToSelectedDate).isEqualTo(false)
    }

    @Test
    fun `test scrolling actually occurs during smooth scrolling`() {
        val originalDate = LocalDate.of(2021, 12, 12)
        val state =
            DatePickerState(
                selectedDate = originalDate,
                shouldScrollToSelectedDate = false,
            )

        val newDate = LocalDate.of(2021, 5, 5)
        state.smoothScrollToDate(date = newDate)

        assertThat(state.shouldScrollToSelectedDate).isEqualTo(true)
        assertThat(state.selectedDate).isEqualTo(newDate)
    }
}
