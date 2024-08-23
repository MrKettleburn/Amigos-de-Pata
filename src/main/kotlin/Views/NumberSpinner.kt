package Views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T : Number> Spinner(
    value: T,
    onValueChange: (T) -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    step: T,
) {
    var text by remember { mutableStateOf(value.toString()) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                val numberValue: T? = parseNumber(it, value)
                if (numberValue != null) {
                    onValueChange(numberValue)
                }
            },
            label = label,
            modifier = Modifier.weight(1f)
        )

        Column {
            IconButton(
                onClick = {
                    val numberValue: T? = parseNumber(text, value)
                    if (numberValue != null) {
                        val newValue = increment(numberValue, step)
                        text = newValue.toString()
                        onValueChange(newValue)
                    }
                },
                modifier = Modifier.size(24.dp) // Tamaño pequeño
            ) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Incrementar")
            }
            IconButton(
                onClick = {
                    val numberValue: T? = parseNumber(text, value)
                    if (numberValue != null) {
                        val newValue = decrement(numberValue, step)
                        if (newValue.toDouble() >= 0) {
                            text = newValue.toString()
                            onValueChange(newValue)
                        }
                    }
                },
                modifier = Modifier.size(24.dp) // Tamaño pequeño
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