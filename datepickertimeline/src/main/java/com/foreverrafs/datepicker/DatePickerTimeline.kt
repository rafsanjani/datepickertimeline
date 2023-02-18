package com.foreverrafs.datepicker

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foreverrafs.datepicker.state.DatePickerState
import com.foreverrafs.datepicker.state.rememberDatePickerState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit.DAYS
import java.util.*

private val EVENT_INDICATOR_SIZE = 8.dp
private val CALENDAR_DATE_ITEM_SIZE = 100.dp

@Suppress("LongMethod")
@ExperimentalComposeUiApi
@Composable
fun DatePickerTimeline(
    modifier: Modifier = Modifier,
    state: DatePickerState = rememberDatePickerState(LocalDate.now()),
    backgroundBrush: Brush,
    selectedBackgroundBrush: Brush,
    eventIndicatorColor: Color = MaterialTheme.colors.primaryVariant,
    eventDates: List<LocalDate>,
    pastDaysCount: Int = 120,
    orientation: Orientation = Orientation.Horizontal,
    selectedTextColor: Color = MaterialTheme.colors.onSurface,
    dateTextColor: Color = MaterialTheme.colors.onSurface,
    todayLabel: @Composable BoxScope.() -> Unit = {},
    onDateSelected: (LocalDate) -> Unit
) {
    // The first date shown on the calendar
    val startDate by remember {
        mutableStateOf(state.initialDate.minusDays(pastDaysCount.toLong()))
    }

    val currentEventDates by rememberUpdatedState(newValue = eventDates)

    var totalWindowWidth by remember { mutableStateOf(1) }

    val selectedDateIndex = DAYS.between(startDate, state.initialDate).toInt()

    val coroutineScope = rememberCoroutineScope()

    // placeholder for how many items a row/column can occupy. Actual value gets calculated after placement
    var span by remember { mutableStateOf(0) }

    val listState = rememberLazyListState()

    // Don't scroll if selected date is already visible on the screen
    val isVisible by remember { derivedStateOf { listState.layoutInfo.visibleItemsInfo.any { it.index == selectedDateIndex } } }

    // We don't want smooth scrolling during initial composition
    var isInitialComposition by remember {
        mutableStateOf(true)
    }

    // scroll to the selected date when it changes
    LaunchedEffect(state.initialDate) {
        if (!isInitialComposition) {
            val scrollPosition = selectedDateIndex - span / 2

            if (scrollPosition <= 0) {
                // Invalid start date, so just scroll to the first item
                listState.animateScrollToItem(0)
            } else if (state.shouldScrollToSelectedDate && !isVisible) {
                listState.animateScrollToItem(scrollPosition)
            }

            // Reset the shouldScrollToSelectedDate flag
            state.onScrollCompleted()
        }
    }

    LaunchedEffect(Unit) {
        listState.scrollToItem(selectedDateIndex)

        state.onScrollCompleted()
        isInitialComposition = false
    }

    Surface(
        elevation = 6.dp,
        modifier = modifier
            .onPlaced {
                totalWindowWidth =
                    if (orientation == Orientation.Horizontal) it.size.width else it.size.height
            }
    ) {
        Column(
            modifier = Modifier
                .then(if (orientation == Orientation.Vertical) Modifier.fillMaxHeight() else Modifier.fillMaxWidth())
                .background(brush = backgroundBrush)
                .padding(4.dp),
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        coroutineScope.launch {
                            state.smoothScrollToDate(LocalDate.now())

                            // state.smoothScrollToDate is backed by a MutableState which doesn't cause recomposition
                            // when the user still has today's date selected because currentValue will be the same
                            // as the applied value. This can happen when the user selects today's date and flings the
                            // calendar. Technically they still have today's date selected so clicking on the 'Today'
                            // text after the fling animation does nothing. We perform this extra step to see if today's
                            // date is visible on the screen. If yes, do nothing, else scroll to it
                            val requiredItemPosition = selectedDateIndex - span / 2

                            if (!isVisible) {
                                listState.animateScrollToItem(
                                    if (requiredItemPosition >= 0) requiredItemPosition else 0
                                )
                            }
                        }
                    }
                    .wrapContentSize(),
                contentAlignment = Alignment.Center
            ) {
                todayLabel()
            }

            Spacer(modifier = Modifier.height(4.dp))

            val hasEvent = remember {
                eventDates.isNotEmpty()
            }


            val visibleItemsInfo by remember { derivedStateOf { listState.layoutInfo.visibleItemsInfo } }

            val firstVisibleDate = visibleItemsInfo.firstOrNull()?.index?.toLong()?.let { startDate.plusDays(it) }
            val lastVisibleDate = visibleItemsInfo.lastOrNull()?.index?.toLong()?.let { startDate.plusDays(it) }

            LaunchedEffect(key1 = firstVisibleDate, key2 = lastVisibleDate) {
                state.setVisibleDates(firstVisibleDate, lastVisibleDate)
            }

            DatePickerLayout(
                orientation = orientation,
                listState = listState,
                hasEvent = hasEvent
            ) {
                items(Integer.MAX_VALUE) { position ->
                    val date = startDate.plusDays(position.toLong())
                    val isEventDate = currentEventDates.contains(date)

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
                        isSelected = date == state.initialDate,
                        onDateSelected = {
                            onDateSelected(it)
                            state.smoothScrollToDate(it)
                        },
                        selectedBackgroundBrush = selectedBackgroundBrush,
                        selectedTextColor = selectedTextColor,
                        dateTextColor = dateTextColor,
                        isEventDate = isEventDate,
                        eventIndicatorColor = eventIndicatorColor
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DatePickerLayout(
    orientation: Orientation,
    listState: LazyListState,
    hasEvent: Boolean,
    content: LazyListScope.() -> Unit,
) {
    when (orientation) {
        Orientation.Vertical -> {
            LazyColumn(
                modifier = Modifier.testTag(tag = "datepickertimeline"),
                horizontalAlignment = Alignment.CenterHorizontally,
                state = listState,
                content = content
            )
        }

        Orientation.Horizontal -> {
            val combinedSize = CALENDAR_DATE_ITEM_SIZE + EVENT_INDICATOR_SIZE

            LazyRow(
                modifier = Modifier
                    .testTag(tag = "datepickertimeline")
                    .height(if (hasEvent) combinedSize else CALENDAR_DATE_ITEM_SIZE),
                state = listState,
                content = content,
            )
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun DatePickerTimeline(
    modifier: Modifier = Modifier,
    state: DatePickerState = rememberDatePickerState(),
    backgroundColor: Color = MaterialTheme.colors.surface,
    selectedBackgroundColor: Color = MaterialTheme.colors.secondaryVariant,
    eventIndicatorColor: Color = MaterialTheme.colors.primaryVariant,
    eventDates: List<LocalDate> = listOf(),
    pastDaysCount: Int = 120,
    orientation: Orientation = Orientation.Horizontal,
    dateTextColor: Color = MaterialTheme.colors.onSurface,
    selectedTextColor: Color = MaterialTheme.colors.onSurface,
    todayLabel: @Composable BoxScope.() -> Unit = {},
    onDateSelected: (LocalDate) -> Unit
) {
    DatePickerTimeline(
        modifier = modifier,
        state = state,
        backgroundBrush = SolidColor(backgroundColor),
        selectedBackgroundBrush = SolidColor(selectedBackgroundColor),
        eventIndicatorColor = eventIndicatorColor,
        eventDates = eventDates,
        orientation = orientation,
        pastDaysCount = pastDaysCount,
        onDateSelected = onDateSelected,
        selectedTextColor = selectedTextColor,
        dateTextColor = dateTextColor,
        todayLabel = todayLabel,
    )
}

private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

@Composable
private fun DateCard(
    modifier: Modifier = Modifier,
    date: LocalDate,
    isSelected: Boolean,
    isEventDate: Boolean,
    eventIndicatorColor: Color,
    onDateSelected: (LocalDate) -> Unit,
    selectedBackgroundBrush: Brush,
    selectedTextColor: Color = MaterialTheme.colors.onSurface,
    dateTextColor: Color = MaterialTheme.colors.onSurface
) {
    Column(
        modifier = modifier
            .testTag(tag = dateFormatter.format(date))
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

        // Month
        Text(
            text = date.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH).uppercase(),
            color = textColor
        )

        // Day of month
        Text(
            text = date.dayOfMonth.toString(),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,
            color = textColor
        )

        // Day of week
        Text(
            text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH).uppercase(),
            color = textColor
        )

        if (isEventDate) {
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .background(color = eventIndicatorColor, shape = CircleShape)
                    .size(8.dp)
            )
        }
    }
}

enum class Orientation {
    Vertical,
    Horizontal
}
