package Views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun <T> DropdownMenu(
    label: @Composable () -> Unit,
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    itemToString: (T) -> String = { it.toString() }
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember {
        mutableStateOf(
            selectedItem?.let(itemToString) ?: items.firstOrNull()?.let(itemToString).orEmpty()
        )
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            label = label,
            readOnly = true,
            enabled = false,
            trailingIcon = {
                IconButton(
                    onClick = { expanded = !expanded },
                ) {
                    Icon(
                        if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    selectedText = itemToString(item)
                    onItemSelected(item)
                    expanded = false },
                    modifier = Modifier.width(400.dp)
                ) {
                    Text(text = itemToString(item))
                }
            }
        }
    }
}


