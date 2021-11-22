package com.foreverrafs.datepicker.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.structuralEqualityPolicy
import java.time.LocalDate

@Stable
interface DatePickerState {
    val initialDate: LocalDate
    val shouldScrollToSelectedDate: Boolean
    fun smoothScrollToDate(date: LocalDate)
    fun onScrollCompleted()
}

internal class DatePickerStateImpl(
    selectedDate: LocalDate,
    shouldScrollToSelectedDate: Boolean = true
) : DatePickerState {
    private var _initialDate by mutableStateOf(selectedDate, structuralEqualityPolicy())
    private var _shouldScrollToSelectedDate by mutableStateOf(
        shouldScrollToSelectedDate,
        structuralEqualityPolicy()
    )

    override fun onScrollCompleted() {
        _shouldScrollToSelectedDate = false
    }

    override val shouldScrollToSelectedDate: Boolean
        get() = _shouldScrollToSelectedDate

    override val initialDate: LocalDate
        get() = _initialDate

    override fun smoothScrollToDate(date: LocalDate) {
        _shouldScrollToSelectedDate = true
        _initialDate = _initialDate
    }

    companion object {
        val Saver: Saver<DatePickerState, *> = listSaver(
            save = {
                listOf(
                    it.initialDate.year,
                    it.initialDate.monthValue,
                    it.initialDate.dayOfMonth,
                    it.shouldScrollToSelectedDate.toString()
                )
            },
            restore = {
                DatePickerStateImpl(
                    selectedDate = LocalDate.of(
                        it[0].toString().toInt(), // year
                        it[1].toString().toInt(), // month
                        it[2].toString().toInt(), // day
                    ),

                    shouldScrollToSelectedDate = it[3].toString()
                        .toBoolean() // shouldScrollToSelectedDate
                )
            }
        )
    }
}

@Composable
fun rememberDatePickerState(initialDate: LocalDate = LocalDate.now()) =
    rememberSaveable(saver = DatePickerStateImpl.Saver) { DatePickerStateImpl(initialDate) }
