package com.foreverrafs.datepickertimeline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.foreverrafs.datepicker.DatePickerTimeline
import com.foreverrafs.datepicker.Orientation
import com.foreverrafs.datepickertimeline.ui.theme.DatepickertimelineTheme
import com.foreverrafs.datepickertimeline.ui.theme.Purple700

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TextStyle()
            DatepickertimelineTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Box {
                        DatePickerTimeline(
                            modifier = Modifier.wrapContentSize(),
                            onDateSelected = {},
                            backgroundBrush = Brush.linearGradient(
                                colors = listOf(
                                    Purple700,
                                    Purple700.copy(alpha = 0.85f)
                                )
                            ),
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
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    DatepickertimelineTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            DatePickerTimeline(onDateSelected = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DatepickertimelineTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            DatePickerTimeline(onDateSelected = {})
        }
    }
}
