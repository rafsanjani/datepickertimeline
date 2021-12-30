package com.foreverrafs.datepicker

import com.foreverrafs.datepicker.state.DatePickerStateImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.LocalDate

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DatePickerStateTest {
    @Test
    fun testInitialDateIsEqualToSelectedDate() {
        val selectedDate = LocalDate.of(2021, 12, 12)
        val state = DatePickerStateImpl(
            selectedDate = selectedDate,
            shouldScrollToSelectedDate = true
        )

        assertThat(state.initialDate).isEqualTo(selectedDate)
    }

    @Test
    fun testScrollingFlagResetsAfterScrolling() {
        val state = DatePickerStateImpl(
            selectedDate = LocalDate.now(),
            shouldScrollToSelectedDate = true
        )

        state.onScrollCompleted()

        assertThat(state.shouldScrollToSelectedDate).isEqualTo(false)
    }

    @Test
    fun testScrollingActuallyOccursDuringSmoothScrolling() {
        val originalDate = LocalDate.of(2021, 12, 12)
        val state = DatePickerStateImpl(
            selectedDate = originalDate,
            shouldScrollToSelectedDate = false
        )

        val newDate = LocalDate.of(2021, 5, 5)
        state.smoothScrollToDate(date = newDate)

        assertThat(state.shouldScrollToSelectedDate).isEqualTo(true)
        assertThat(state.initialDate).isEqualTo(newDate)
    }
}
