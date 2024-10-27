package com.foreverrafs.datepicker.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.structuralEqualityPolicy
import java.time.LocalDate

class DatePickerState(
    selectedDate: LocalDate,
    shouldScrollToSelectedDate: Boolean = true,
) {
    private var _selectedDate by mutableStateOf(selectedDate, structuralEqualityPolicy())
    private var _shouldScrollToSelectedDate by mutableStateOf(
        shouldScrollToSelectedDate,
    )

    internal fun onScrollCompleted() {
        _shouldScrollToSelectedDate = false
    }

    val shouldScrollToSelectedDate: Boolean
        get() = _shouldScrollToSelectedDate

    val selectedDate: LocalDate
        get() = _selectedDate

    fun smoothScrollToDate(date: LocalDate) {
        _shouldScrollToSelectedDate = true
        _selectedDate = date
    }

    fun setVisibleDates(
        firstDate: LocalDate?,
        lastDate: LocalDate?,
    ) {
        _firstVisibleDate = firstDate
        _lastVisibleDate = lastDate
    }

    private var _firstVisibleDate by mutableStateOf<LocalDate?>(null)
    val firstVisibleDate get() = _firstVisibleDate

    private var _lastVisibleDate by mutableStateOf<LocalDate?>(null)
    val lastVisibleDate get() = _lastVisibleDate

    companion object {
        val Saver: Saver<DatePickerState, *> =
            listSaver(
                save = {
                    listOf(
                        it.selectedDate.year,
                        it.selectedDate.monthValue,
                        it.selectedDate.dayOfMonth,
                        it.shouldScrollToSelectedDate.toString(),
                    )
                },
                restore = {
                    DatePickerState(
                        selectedDate = LocalDate.of(
                            it[0].toString().toInt(), // year
                            it[1].toString().toInt(), // month
                            it[2].toString().toInt(), // day
                        ),
                        shouldScrollToSelectedDate = it[3]
                            .toString()
                            .toBoolean(),
                        // shouldScrollToSelectedDate
                    )
                },
            )
    }
}

@Composable
fun rememberDatePickerState(initialDate: LocalDate = LocalDate.now()) = rememberSaveable(saver = DatePickerState.Saver) { DatePickerState(initialDate) }
