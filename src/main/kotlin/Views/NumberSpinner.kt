package Views

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.onKeyEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun <T : Number> Spinner(
    value: T,
    onValueChange: (T) -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    step: T,
) {
    var text by remember { mutableStateOf(value.toString()) }
    var incrementJob: Job? by remember { mutableStateOf(null) }  // Track the job for increment/decrement
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = modifier
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown) {
                    when (event.key) {
                        Key.DirectionUp -> {
                            val numberValue: T = parseNumber(text, value) ?: getDefaultNumber(value)
                            val newValue = increment(numberValue, step)
                            text = newValue.toString()
                            onValueChange(newValue)
                            true
                        }
                        Key.DirectionDown -> {
                            val numberValue: T = parseNumber(text, value) ?: getDefaultNumber(value)
                            val newValue = decrement(numberValue, step)
                            if (newValue.toDouble() >= 0) {
                                text = newValue.toString()
                                onValueChange(newValue)
                            }
                            true
                        }
                        else -> false
                    }
                } else {
                    false
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                val numberValue: T = parseNumber(it, value) ?: getDefaultNumber(value)
                onValueChange(numberValue)
            },
            label = label,
            modifier = Modifier.weight(1f)
        )

        Column {
            // Increment Button with hold functionality
            IconButton(
                onClick = {
                    incrementJob?.cancel()  // Cancel any previous job
                    val numberValue: T = parseNumber(text, value) ?: getDefaultNumber(value)
                    val newValue = increment(numberValue, step)
                    text = newValue.toString()
                    onValueChange(newValue)
                },
                modifier = Modifier
                    .size(24.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                incrementJob = coroutineScope.launch {
                                    while (true) {
                                        val numberValue: T = parseNumber(text, value) ?: getDefaultNumber(value)
                                        val newValue = increment(numberValue, step)
                                        text = newValue.toString()
                                        onValueChange(newValue)
                                        delay(200) // Repeat the increment every 200ms
                                    }
                                }
                                tryAwaitRelease()  // Wait until the press is released
                                incrementJob?.cancel()  // Cancel the job after release
                            }
                        )
                    }
            ) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Incrementar")
            }

            // Decrement Button with hold functionality
            IconButton(
                onClick = {
                    incrementJob?.cancel()  // Cancel any previous job
                    val numberValue: T = parseNumber(text, value) ?: getDefaultNumber(value)
                    val newValue = decrement(numberValue, step)
                    if (newValue.toDouble() >= 0) {
                        text = newValue.toString()
                        onValueChange(newValue)
                    }
                },
                modifier = Modifier
                    .size(24.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                incrementJob = coroutineScope.launch {
                                    while (true) {
                                        val numberValue: T = parseNumber(text, value) ?: getDefaultNumber(value)
                                        val newValue = decrement(numberValue, step)
                                        if (newValue.toDouble() >= 0) {
                                            text = newValue.toString()
                                            onValueChange(newValue)
                                        }
                                        delay(200)  // Repeat the decrement every 200ms
                                    }
                                }
                                tryAwaitRelease()  // Wait until the press is released
                                incrementJob?.cancel()  // Cancel the job after release
                            }
                        )
                    }
            ) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrementar")
            }
        }
    }
}

private fun <T : Number> parseNumber(input: String, defaultValue: T): T? {
    return when (defaultValue) {
        is Int -> input.toIntOrNull() as? T
        is Double -> input.toDoubleOrNull() as? T
        else -> null
    }
}

private fun <T : Number> getDefaultNumber(value: T): T {
    return when (value) {
        is Int -> 0 as T
        is Double -> 0.0 as T
        else -> value
    }
}

private fun <T : Number> increment(value: T, step: T): T {
    return when (value) {
        is Int -> (value + step.toInt()) as T
        is Double -> (value + step.toDouble()) as T
        else -> value
    }
}

private fun <T : Number> decrement(value: T, step: T): T {
    return when (value) {
        is Int -> (value - step.toInt()) as T
        is Double -> (value - step.toDouble()) as T
        else -> value
    }
}