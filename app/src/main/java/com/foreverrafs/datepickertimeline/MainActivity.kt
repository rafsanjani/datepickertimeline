package com.foreverrafs.datepickertimeline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foreverrafs.datepicker.DatePickerTimeline
import com.foreverrafs.datepicker.Orientation
import com.foreverrafs.datepicker.state.rememberDatePickerState
import com.foreverrafs.datepickertimeline.ui.theme.DatepickertimelineTheme
import com.foreverrafs.datepickertimeline.ui.theme.Purple700
import java.time.LocalDate
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Composable
fun App() {
    DatepickertimelineTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Box(modifier = Modifier.padding(8.dp)) {
                val datePickerState = rememberDatePickerState(initialDate = LocalDate.now().minusDays(5))

                DatePickerTimeline(
                    modifier = Modifier.wrapContentSize(),
                    onDateSelected = {},
                    backgroundBrush = Brush.linearGradient(
                        colors = listOf(
                            Purple700,
                            Purple700.copy(alpha = 0.85f)
                        )
                    ),
                    state = datePickerState,
                    orientation = Orientation.Horizontal,
                    selectedBackgroundBrush = Brush.linearGradient(
                        colors = listOf(
                            Color.Blue,
                            Color.Blue.copy(alpha = 0.65f)
                        )
                    ),
                    selectedTextColor = Color.White,
                    dateTextColor = Color.White,
                    todayTextColor = Color.White
                )
                Button(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    onClick = {
                        datePickerState.smoothScrollToDate(LocalDate.now().plusDays(Random.nextLong(until = 10)))
                    }
                ) {
                    Text(datePickerState.initialDate.toString())
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DatepickertimelineTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            DatePickerTimeline(onDateSelected = {})
        }
    }
}
