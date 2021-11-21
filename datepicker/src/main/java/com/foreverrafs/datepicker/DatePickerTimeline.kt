package com.foreverrafs.datepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@ExperimentalComposeUiApi
@Composable
fun DatePickerTimeline(
    modifier: Modifier = Modifier,
    initialSelectedDate: LocalDate = LocalDate.now(),
    backgroundBrush: Brush,
    selectedBackgroundBrush: Brush,
    pastDaysCount: Int = 120,
    orientation: Orientation = Orientation.Horizontal,
    selectedTextColor: Color = MaterialTheme.colors.onSurface,
    dateTextColor: Color = MaterialTheme.colors.onSurface,
    todayTextColor: Color = MaterialTheme.colors.onSurface,
    onDateSelected: (LocalDate) -> Unit
) {
    var selectedDate by remember { mutableStateOf(initialSelectedDate) }

    var totalWindowWidth by remember { mutableStateOf(1) }

    val startDate by remember { mutableStateOf(LocalDate.now().minusDays(pastDaysCount.toLong())) }

    val todayDatePosition by remember { mutableStateOf(pastDaysCount) }
    val coroutineScope = rememberCoroutineScope()

    // placeholder for how many items a row/column can occupy. Actual value gets calculated after placement
    var span by remember { mutableStateOf(1) }

    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        listState.scrollToItem(todayDatePosition - span / 2)
    }

    Column(
        modifier = modifier
            .then(if (orientation == Orientation.Vertical) Modifier.fillMaxHeight() else Modifier.fillMaxWidth())
            .clip(RoundedCornerShape(size = 4.dp))
            .background(brush = backgroundBrush)
            .padding(8.dp)
            .onPlaced {
                totalWindowWidth =
                    if (orientation == Orientation.Horizontal) it.size.width else it.size.height
            },
    ) {
        TodayText(
            text = "Today",
            color = todayTextColor,
            style = MaterialTheme.typography.h6
        ) {
            coroutineScope.launch {
                listState.animateScrollToItem(todayDatePosition - span / 2)
                selectedDate = LocalDate.now()
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        val content: LazyListScope.() -> Unit = {
            items(Integer.MAX_VALUE) { value ->
                val date = startDate.plusDays(value.toLong())

                DateCard(
                    modifier = Modifier
                        .onPlaced {
                            span =
                                totalWindowWidth / if (orientation == Orientation.Horizontal) {
                                it.size.width
                            } else {
                                it.size.height
                            }
                        },
                    date = date,
                    isSelected = date == selectedDate,
                    onDateSelected = {
                        selectedDate = it
                        onDateSelected(it)
                    },
                    selectedBackgroundBrush = selectedBackgroundBrush,
                    selectedTextColor = selectedTextColor,
                    dateTextColor = dateTextColor,
                )
            }
        }

        DatePickerLayout(orientation = orientation, listState = listState) {
            content()
        }
    }
}

@Composable
private fun DatePickerLayout(
    orientation: Orientation,
    listState: LazyListState,
    content: LazyListScope.() -> Unit,
) {
    when (orientation) {
        Orientation.Vertical -> {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                state = listState,
                content = content
            )
        }
        Orientation.Horizontal -> {
            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                state = listState,
                content = content
            )
        }
    }
}

@Composable
private fun ColumnScope.TodayText(
    text: String,
    color: Color,
    style: androidx.compose.ui.text.TextStyle,
    onClick: () -> Unit
) {
    Text(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                onClick()
            }
            .padding(12.dp),
        text = text,
        textAlign = TextAlign.Center,
        color = color,
        style = style
    )
}

@ExperimentalComposeUiApi
@Composable
fun DatePickerTimeline(
    modifier: Modifier = Modifier,
    initialSelectedDate: LocalDate = LocalDate.now(),
    backgroundColor: Color = MaterialTheme.colors.surface,
    selectedBackgroundColor: Color = MaterialTheme.colors.secondaryVariant,
    pastDaysCount: Int = 120,
    orientation: Orientation = Orientation.Horizontal,
    dateTextColor: Color = MaterialTheme.colors.onSurface,
    selectedTextColor: Color = MaterialTheme.colors.onSurface,
    todayTextColor: Color = MaterialTheme.colors.onSurface,
    onDateSelected: (LocalDate) -> Unit
) {
    DatePickerTimeline(
        modifier = modifier,
        initialSelectedDate = initialSelectedDate,
        backgroundBrush = SolidColor(backgroundColor),
        selectedBackgroundBrush = SolidColor(selectedBackgroundColor),
        orientation = orientation,
        pastDaysCount = pastDaysCount,
        onDateSelected = onDateSelected,
        selectedTextColor = selectedTextColor,
        dateTextColor = dateTextColor,
        todayTextColor = todayTextColor,
    )
}

@Composable
private fun DateCard(
    modifier: Modifier = Modifier,
    date: LocalDate,
    isSelected: Boolean,
    onDateSelected: (LocalDate) -> Unit,
    selectedBackgroundBrush: Brush,
    selectedTextColor: Color = MaterialTheme.colors.onSurface,
    dateTextColor: Color = MaterialTheme.colors.onSurface
) {
    Column(
        modifier = modifier
            .clip(shape = RoundedCornerShape(16.dp))
            .then(
                if (isSelected) {
                    Modifier.background(
                        brush = selectedBackgroundBrush,
                        alpha = 0.65f,
                    )
                } else Modifier
            )
            .padding(vertical = 4.dp)
            .clickable {
                onDateSelected(date)
            }
            .padding(vertical = 2.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val textColor = if (isSelected) selectedTextColor else dateTextColor

        Text(
            text = date.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH).uppercase(),
            color = textColor
        )
        Text(
            text = date.dayOfMonth.toString(),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,
            color = textColor
        )
        Text(
            text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH).uppercase(),
            color = textColor
        )
    }
}

enum class Orientation {
    Vertical,
    Horizontal
}
