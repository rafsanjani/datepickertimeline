package com.foreverrafs.datepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
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
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

private val EVENT_INDICATOR_SIZE = 8.dp
private val CALENDAR_DATE_ITEM_SIZE = 100.dp

@Suppress("LongMethod")
@Composable
fun DatePickerTimeline(
    modifier: Modifier = Modifier,
    state: DatePickerState = rememberDatePickerState(
        Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
    ),
    backgroundBrush: Brush,
    selectedBackgroundBrush: Brush,
    eventIndicatorColor: Color = MaterialTheme.colors.primaryVariant,
    eventDates: List<LocalDate>,
    pastDaysCount: Int = 120,
    orientation: Orientation = Orientation.Horizontal,
    selectedTextColor: Color = MaterialTheme.colors.onSurface,
    dateTextColor: Color = MaterialTheme.colors.onSurface,
    todayLabel: @Composable BoxScope.() -> Unit = {},
    onDateSelected: (LocalDate) -> Unit,
) {
    // The first date shown on the calendar
    val startDate by remember {
        mutableStateOf(
            state.selectedDate.minus(pastDaysCount.toLong(), DateTimeUnit.DAY),
        )
    }

    val currentEventDates by rememberUpdatedState(newValue = eventDates)

    var totalWindowWidth by remember { mutableIntStateOf(1) }

    val selectedDateIndex = startDate.daysUntil(state.selectedDate)

    val coroutineScope = rememberCoroutineScope()

    // placeholder for how many items a row/column can occupy. Actual value gets calculated after placement
    var span by remember { mutableIntStateOf(0) }

    val listState = rememberLazyListState()

    val fullyVisibleIndices: List<Int> by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (visibleItemsInfo.isEmpty()) {
                emptyList()
            } else {
                val fullyVisibleItemsInfo = visibleItemsInfo.toMutableList()

                val lastItem = fullyVisibleItemsInfo.last()

                val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset

                if (lastItem.offset + lastItem.size > viewportHeight) {
                    fullyVisibleItemsInfo.removeAt(fullyVisibleItemsInfo.lastIndex)
                }

                val firstItemIfLeft = fullyVisibleItemsInfo.firstOrNull()

                if (firstItemIfLeft != null &&
                    firstItemIfLeft.offset < layoutInfo.viewportStartOffset
                ) {
                    fullyVisibleItemsInfo.removeAt(0)
                }

                fullyVisibleItemsInfo.map { it.index }
            }
        }
    }

    // We don't want smooth scrolling during initial composition
    var isInitialComposition by remember {
        mutableStateOf(true)
    }

    // scroll to the selected date when it changes
    LaunchedEffect(state.shouldScrollToSelectedDate) {
        // Scroll position should at least be 0
        val scrollPosition = (selectedDateIndex - span / 2).coerceAtLeast(0)

        val isSelectedDateVisible = fullyVisibleIndices.contains(selectedDateIndex)

        if (state.shouldScrollToSelectedDate) {
            if (isInitialComposition) {
                listState.scrollToItem(scrollPosition)
            } else if (!isSelectedDateVisible) {
                listState.animateScrollToItem(
                    scrollPosition,
                )
            }

            // Reset the shouldScrollToSelectedDate flag
            state.onScrollCompleted()
        }
    }

    LaunchedEffect(Unit) {
        println(selectedDateIndex)

        state.onScrollCompleted()
        isInitialComposition = false
    }

    Surface(
        elevation = 6.dp,
        modifier =
        modifier.onPlaced {
            totalWindowWidth =
                if (orientation == Orientation.Horizontal) {
                    it.size.width
                } else {
                    it.size.height
                }
        },
    ) {
        Column(
            modifier =
            Modifier
                .then(
                    if (orientation == Orientation.Vertical) {
                        Modifier.fillMaxHeight()
                    } else {
                        Modifier.fillMaxWidth()
                    },
                ).background(brush = backgroundBrush)
                .padding(4.dp),
        ) {
            Box(
                modifier =
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .clip(
                        RoundedCornerShape(12.dp),
                    ).clickable {
                        coroutineScope.launch {
                            state.smoothScrollToDate(
                                Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
                            )
                        }
                    }.wrapContentSize(),
                contentAlignment = Alignment.Center,
            ) {
                todayLabel()
            }

            Spacer(modifier = Modifier.height(4.dp))

            val hasEvent =
                remember {
                    eventDates.isNotEmpty()
                }

            val visibleItemsCount by remember {
                derivedStateOf { listState.layoutInfo.visibleItemsInfo.size }
            }
            val firstVisibleItemIndex by remember {
                derivedStateOf { listState.firstVisibleItemIndex }
            }
            val lastVisibleItemIndex =
                (firstVisibleItemIndex + visibleItemsCount - 1)
                    .coerceAtLeast(
                        0,
                    )

            LaunchedEffect(key1 = firstVisibleItemIndex, key2 = lastVisibleItemIndex) {
                val firstVisibleDate =
                    startDate.plus(
                        value = firstVisibleItemIndex.toLong(),
                        unit = DateTimeUnit.DAY,
                    )
                val lastVisibleDate =
                    startDate.plus(lastVisibleItemIndex.toLong(), DateTimeUnit.DAY)

                state.setVisibleDates(firstVisibleDate, lastVisibleDate)
            }

            DatePickerLayout(
                orientation = orientation,
                listState = listState,
                hasEvent = hasEvent,
            ) {
                items(Int.MAX_VALUE) { position ->
                    val date = startDate.plus(position.toLong(), DateTimeUnit.DAY)
                    val isEventDate = currentEventDates.contains(date)

                    DateCard(
                        modifier =
                        Modifier.onPlaced {
                            span = totalWindowWidth /
                                if (orientation == Orientation.Horizontal) {
                                    it.size.width
                                } else {
                                    it.size.height
                                }
                        },
                        date = date,
                        isSelected = date == state.selectedDate,
                        onDateSelected = {
                            onDateSelected(it)
                            state.smoothScrollToDate(it)
                        },
                        selectedBackgroundBrush = selectedBackgroundBrush,
                        selectedTextColor = selectedTextColor,
                        dateTextColor = dateTextColor,
                        isEventDate = isEventDate,
                        eventIndicatorColor = eventIndicatorColor,
                    )
                }
            }
        }
    }
}

@Composable
private fun DatePickerLayout(
    orientation: Orientation,
    listState: LazyListState,
    hasEvent: Boolean,
    content: LazyListScope.() -> Unit,
) {
    val testTag = "datepickertimeline"

    when (orientation) {
        Orientation.Vertical -> {
            LazyColumn(
                modifier = Modifier.testTag(tag = testTag),
                horizontalAlignment = Alignment.CenterHorizontally,
                state = listState,
                content = content,
                flingBehavior = rememberSnapFlingBehavior(listState),
            )
        }

        Orientation.Horizontal -> {
            val combinedSize = CALENDAR_DATE_ITEM_SIZE + EVENT_INDICATOR_SIZE

            LazyRow(
                modifier =
                Modifier
                    .testTag(tag = testTag)
                    .height(if (hasEvent) combinedSize else CALENDAR_DATE_ITEM_SIZE),
                state = listState,
                content = content,
                flingBehavior = rememberSnapFlingBehavior(listState),
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
    onDateSelected: (LocalDate) -> Unit,
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

@OptIn(FormatStringsInDatetimeFormats::class)
private val dateFormatter = LocalDate.Format {
    byUnicodePattern("dd/MM/yyyy")
}

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
    dateTextColor: Color = MaterialTheme.colors.onSurface,
) {
    Column(
        modifier =
        modifier
            .testTag(tag = dateFormatter.format(date))
            .clip(
                shape = RoundedCornerShape(16.dp),
            ).then(
                if (isSelected) {
                    Modifier.background(
                        brush = selectedBackgroundBrush,
                        alpha = 0.65f,
                    )
                } else {
                    Modifier
                },
            ).padding(vertical = 4.dp)
            .clickable {
                onDateSelected(date)
            }.padding(vertical = 2.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val textColor = if (isSelected) selectedTextColor else dateTextColor

        // Month
        Text(
            text = date.month.name.take(3),
            color = textColor,
        )

        // Day of month
        Text(
            text = date.dayOfMonth.toString(),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,
            color = textColor,
        )

        // Day of week
        Text(
            text = date.dayOfWeek.name.take(3),
            color = textColor,
        )

        if (isEventDate) {
            Box(
                modifier =
                Modifier
                    .padding(2.dp)
                    .background(
                        color = eventIndicatorColor,
                        shape = CircleShape,
                    ).size(8.dp),
            )
        }
    }
}

enum class Orientation {
    Vertical,
    Horizontal,
}
