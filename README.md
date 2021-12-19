

# DatePickerTimeline ![Android CI](https://github.com/Rafsanjani/datepickertimeline/actions/workflows/android.yml/badge.svg?branch=main)


A linear Android date picker library written in Jetpack compose.<br> Inspired by https://pub.dev/packages/date_picker_timeline



<img height="500" width="250" src="https://user-images.githubusercontent.com/9197459/146689659-647e230a-57ef-45e6-be76-c1cb2bb8b0c1.gif">  <img height="500" width="250" src="https://user-images.githubusercontent.com/9197459/146690796-5a979f20-a7be-4956-a991-36750cdcf0ab.gif">


## Installation ![](https://jitpack.io/v/Rafsanjani/datepickertimeline.svg)

```groovy
repositories {
  maven { url 'https://jitpack.io' }
}

dependencies{
  implementation 'com.github.Rafsanjani:datepickertimeline:<latest_version>'
}
```
## Usage 
### For a horizontal date picker with no customization
```kotlin 
DatePickerTimeLine(
	modifier = Modifier,
	onDateSelected = {selectedDate: LocalDate->
		//do something with selected date
	}
) 
```
### For a vertical date picker with no customization

```kotlin 
import com.foreverrafs.datepicker.Orientation

DatePickerTimeLine(
	modifier = Modifier,
	orientation = Orientation.Vertical,
	onDateSelected = {selectedDate: LocalDate->
		//do something with selected date
	}
) 
```

### You can pass an optional state to the picker and specify the initial selected date

```kotlin 
DatePickerTimeLine(
	modifier = Modifier,
	state = rememberDatePickerState(initialDate = LocalDate.now()),
	onDateSelected = { selectedDate: LocalDate ->
		//do something with selected date
	}
) 
```

### To customize the full list of properties
```kotlin 
import com.foreverrafs.datepicker.Orientation

val today = LocalDate.now()
val datePickerState =  
    rememberDatePickerState(initialDate = LocalDate.now())
    
DatePickerTimeline(
  modifier = Modifier.wrapContentSize(),  
  onDateSelected = { selectedDate: LocalDate ->
		// do something with the selected date
  },  
  backgroundColor = Color.Yellow,  // the main background color
  state = datePickerState,  
  orientation = Orientation.Horizontal,  
  selectedBackgroundColor = Color.Green, // The background of the currently selected date  
  selectedTextColor = Color.White,  // Text color of currently selected date
  dateTextColor = Color.Black, //Text color of all dates  
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
          style = MaterialTheme.typography.h6  
      )
    },  
  pastDaysCount = 10,  // The number of previous dates to display, relative to the initial date. Defaults to 120
  eventIndicatorColor = Color.Brown // Indicator color for marked event dates.
)
```

### Scrolling to different dates
You can store the `DatePickerState` object into a variable and use it to perform smooth scrolling to any date. This is very similar to how `LazyListState` works. If you try to scroll to an invalid date as constrained by the `pastDaysCount` property, you are scrolled to the first valid date in the calendar instead.  

```kotlin
val today = LocalDate.now()
val datePickerState =  
    rememberDatePickerState(initialDate = LocalDate.now())
    
Column{
  DatePickerTimeline(
    state = datePickerState
  )
  
  Button(
    onClick = {
      // Scroll to Jan 01, 2022
      datePickerState.smoothScrollTodate(LocalDate.of(2022, 1, 1)
    }
  ){
    Text("Go to different date")
  }
}
```
