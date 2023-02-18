package com.foreverrafs.datepicker

import com.foreverrafs.datepicker.state.DatePickerState
import java.time.LocalDate
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DatePickerStateTest {
    @Test
    fun testInitialDateIsEqualToSelectedDate() {
        val selectedDate = LocalDate.of(2021, 12, 12)
        val state = DatePickerState(
            selectedDate = selectedDate,
            shouldScrollToSelectedDate = true
        )

        assertThat(state.initialDate).isEqualTo(selectedDate)
    }

    @Test
    fun testScrollingFlagResetsAfterScrolling() {
        val state = DatePickerState(
            selectedDate = LocalDate.now(),
            shouldScrollToSelectedDate = true
        )

        state.onScrollCompleted()

        assertThat(state.shouldScrollToSelectedDate).isEqualTo(false)
    }

    @Test
    fun testScrollingActuallyOccursDuringSmoothScrolling() {
        val originalDate = LocalDate.of(2021, 12, 12)
        val state = DatePickerState(
            selectedDate = originalDate,
            shouldScrollToSelectedDate = false
        )

        val newDate = LocalDate.of(2021, 5, 5)
        state.smoothScrollToDate(date = newDate)

        assertThat(state.shouldScrollToSelectedDate).isEqualTo(true)
        assertThat(state.initialDate).isEqualTo(newDate)
    }
}
