package com.foreverrafs.datepickertimeline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foreverrafs.datepicker.DatePickerTimeline
import com.foreverrafs.datepicker.Orientation
import com.foreverrafs.datepicker.state.rememberDatePickerState
import com.foreverrafs.datepickertimeline.ui.theme.DatepickertimelineTheme
import com.foreverrafs.datepickertimeline.ui.theme.Purple500
import com.godaddy.android.colorpicker.ClassicColorPicker
import com.godaddy.android.colorpicker.HsvColor
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Suppress("LongMethod")
@Composable
fun App() {
    DatepickertimelineTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
            ) {
                val datePickerState =
                    rememberDatePickerState(initialDate = LocalDate.of(2021, 5, 12))

                var mainBackgroundColor by remember { mutableStateOf(Purple500) }
                var selectedDateBackgroundColor by remember {
                    mutableStateOf(Color.Black.copy(alpha = 0.35f))
                }
                var eventIndicatorColor by remember {
                    mutableStateOf(Color.Black.copy(alpha = 0.35f))
                }
                var dateTextColor by remember { mutableStateOf(Color.White) }
                val today = LocalDate.now()

                DatePickerTimeline(
                    modifier = Modifier.wrapContentSize(),
                    onDateSelected = {},
                    backgroundColor = mainBackgroundColor,
                    state = datePickerState,
                    orientation = Orientation.Horizontal,
                    selectedBackgroundColor = selectedDateBackgroundColor,
                    selectedTextColor = Color.White,
                    dateTextColor = dateTextColor,
                    eventDates = listOf(
                        today.plusDays(1),
                        today.plusDays(3),
                        today.plusDays(5),
                        today.plusDays(8),
                    ),
                    todayLabel = {
                        Text(
                            modifier = Modifier.padding(10.dp),
                            text = "Today",
                            color = Color.White,
                            style = MaterialTheme.typography.h6,
                        )
                    },
                    pastDaysCount = 1,
                    eventIndicatorColor = eventIndicatorColor,
                )

                Spacer(modifier = Modifier.height(8.dp))

                var selected by remember { mutableStateOf(Radio.MainBackground) }

                Column {
                    RadioButtonWithText(
                        text = "Main Background",
                        selected = selected == Radio.MainBackground,
                        onClick = { selected = Radio.MainBackground },
                    )

                    RadioButtonWithText(
                        text = "Selected date background",
                        selected = selected == Radio.SelectedDateBackground,
                        onClick = { selected = Radio.SelectedDateBackground },
                    )

                    RadioButtonWithText(
                        text = "Date text color",
                        selected = selected == Radio.DateTextColor,
                        onClick = { selected = Radio.DateTextColor },
                    )

                    RadioButtonWithText(
                        text = "Event Indicator color",
                        selected = selected == Radio.EventIndicatorColor,
                        onClick = { selected = Radio.EventIndicatorColor },
                    )

                    ClassicColorPicker(
                        modifier = Modifier.height(250.dp),
                        color = HsvColor.from(color = mainBackgroundColor),
                        onColorChanged = { color: HsvColor ->
                            when (selected) {
                                Radio.MainBackground -> mainBackgroundColor = color.toColor()
                                Radio.SelectedDateBackground ->
                                    selectedDateBackgroundColor =
                                        color.toColor()

                                Radio.DateTextColor ->
                                    dateTextColor =
                                        color.toColor()

                                Radio.EventIndicatorColor ->
                                    eventIndicatorColor = color.toColor()
                            }
                        },
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .padding(10.dp)
                            .height(48.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        onClick = {
                            datePickerState.smoothScrollToDate(
                                LocalDate.now().plusDays(Random.nextLong(until = 50)),
                            )
                        },
                    ) {
                        Text("Random Date")
                    }

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
                            .format(datePickerState.initialDate),
                        style = MaterialTheme.typography.h6,
                    )
                }
            }
        }
    }
}

enum class Radio {
    MainBackground,
    SelectedDateBackground,
    DateTextColor,
    EventIndicatorColor,
}

@Composable
fun RadioButtonWithText(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = text, textAlign = TextAlign.Center)
        RadioButton(selected = selected, onClick = onClick)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    App()
}
