package Views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@Composable
fun DatePicker(
    label: @Composable () -> Unit,
    selectedDate: LocalDate?,
    onDateChange: (LocalDate?) -> Unit,
    modifier: Modifier = Modifier
) {
    var datePickerDialogVisible by remember { mutableStateOf(false) }
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val displayDate = selectedDate?.format(formatter) ?: ""

    Box(modifier = modifier) {
        OutlinedTextField(
            value = displayDate,
            onValueChange = {},
            readOnly = true,
            label = label,
            enabled = false,
            trailingIcon = {
                IconButton(
                    onClick = { datePickerDialogVisible = true },
                ) {
                    Icon(Icons.Default.CalendarToday, contentDescription = "Seleccionar fecha")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
        )

        if (datePickerDialogVisible) {
            DatePickerDialog(
                initialDate = selectedDate ?: LocalDate.now(),
                onDateSelected = { date ->
                    onDateChange(date)
                    datePickerDialogVisible = false
                },
                onDismissRequest = {
                    datePickerDialogVisible = false
                }
            )
        }
    }
}

@Composable
fun DatePickerDialog(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit
) {
    var selectedDate by remember { mutableStateOf(initialDate) }
    var currentMonth by remember { mutableStateOf(YearMonth.from(initialDate)) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Surface(modifier = Modifier.padding(16.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                // Month and Year selector
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Previous Month")
                    }
                    Text(
                        text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                        Icon(Icons.Default.ArrowForward, contentDescription = "Next Month")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Days of the week header
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                        Text(
                            text = day,
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Days of the month
                val daysInMonth = currentMonth.lengthOfMonth()
                val firstDayOfMonth = currentMonth.atDay(1).dayOfWeek.value % 7
                val days = (1..daysInMonth).map { currentMonth.atDay(it) }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier.height(240.dp)
                ) {
                    // Fill empty spaces before the first day
                    items(firstDayOfMonth) {
                        Spacer(modifier = Modifier.size(40.dp))
                    }
                    // Fill days
                    items(days) { date ->
                        DateCell(
                            date = date,
                            isSelected = date == selectedDate,
                            onClick = { selectedDate = date }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Confirm and cancel buttons
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = { onDismissRequest() }) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { onDateSelected(selectedDate) }) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

@Composable
fun DateCell(
    date: LocalDate,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) MaterialTheme.colors.primary else Color.Transparent
    val textColor = if (isSelected) Color.White else MaterialTheme.colors.onSurface

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(40.dp)
            .background(backgroundColor, shape = MaterialTheme.shapes.small)
            .clickable { onClick() }
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            color = textColor,
            style = MaterialTheme.typography.body1
        )
    }
}