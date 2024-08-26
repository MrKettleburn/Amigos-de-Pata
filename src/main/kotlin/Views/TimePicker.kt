package Views

import androidx.compose.runtime.Composable
import java.time.LocalTime
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.window.Dialog

@Composable
fun TimePicker(
    label: @Composable () -> Unit,
    selectedTime: LocalTime?,
    onTimeChange: (LocalTime?) -> Unit,
    modifier: Modifier = Modifier
) {
    var timePickerDialogVisible by remember { mutableStateOf(false) }
    val displayTime = selectedTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "Seleccionar"
    var selectedHour by remember { mutableStateOf(selectedTime?.hour ?: 0) }
    var selectedMinute by remember { mutableStateOf(selectedTime?.minute ?: 0) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = displayTime,
            onValueChange = {},
            label = label,
            readOnly = true,
            trailingIcon = {
                IconButton(
                    onClick = { timePickerDialogVisible = true },
                ) {
                    Icon(Icons.Default.Timer, contentDescription = "Seleccionar hora")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
        )

        if (timePickerDialogVisible) {
            Dialog(onDismissRequest = { timePickerDialogVisible = false }) {
                Surface(
                    modifier = Modifier.padding(16.dp).width(300.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colors.surface
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Hour Picker
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            val hours = (0..23).toList()
                            val minutes = (0..59).toList()

                            DropdownMenu(
                                label = { Text("Horas") },
                                items = hours,
                                selectedItem = selectedHour,
                                onItemSelected = { selectedHour = it },
                                modifier = Modifier.weight(1f)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            DropdownMenu(
                                label = { Text("Minutos") },
                                items = minutes,
                                selectedItem = selectedMinute,
                                onItemSelected = { selectedMinute = it },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { timePickerDialogVisible = false }) {
                                Text("Cancelar")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = {
                                onTimeChange(LocalTime.of(selectedHour, selectedMinute))
                                timePickerDialogVisible = false
                            }) {
                                Text("Aceptar")
                            }
                        }
                    }
                }
            }
        }
    }
}
