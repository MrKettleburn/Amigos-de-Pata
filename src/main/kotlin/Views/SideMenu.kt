package Views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedSideMenu(colors: RefugioColorPalette) {
    var selectedItem by remember { mutableStateOf("") }
    var selectedSubItem by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .width(250.dp)
            .fillMaxHeight()
            .background(colors.menuBackground)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            "Gestión",
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            color = colors.onMenuItem,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ExpandableMenuItem("Contratos", colors, selectedItem, selectedSubItem) { item, subItem ->
            selectedItem = item
            selectedSubItem = subItem
        }
        ExpandableMenuItem("Contratados", colors, selectedItem, selectedSubItem) { item, subItem ->
            selectedItem = item
            selectedSubItem = subItem
        }
        ExpandableMenuItem("Servicios", colors, selectedItem, selectedSubItem) { item, subItem ->
            selectedItem = item
            selectedSubItem = subItem
        }
        MenuItem("Animales", colors, selectedItem, selectedSubItem) {
            selectedItem = it
            selectedSubItem = ""
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Informes",
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            color = colors.onMenuItem,
            modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
        )

        for (i in 1..5) {
            MenuItem("Reporte $i", colors, selectedItem, selectedSubItem) {
                selectedItem = it
                selectedSubItem = ""
            }
        }
    }
}

@Composable
fun ExpandableMenuItem(
    title: String,
    colors: RefugioColorPalette,
    selectedItem: String,
    selectedSubItem: String,
    onItemSelected: (String, String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(if (expanded) 180f else 0f)

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    expanded = !expanded
                    onItemSelected(title, "")
                }
                .background(if (selectedItem == title) colors.menuItemSelected else Color.Transparent)
                .padding(vertical = 8.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = if (selectedItem == title) colors.onMenuItemSelected else colors.onMenuItem,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = "Expand",
                modifier = Modifier.rotate(rotationState),
                tint = if (selectedItem == title) colors.onMenuItemSelected else colors.onMenuItem
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column {
                SubMenuItem("Veterinarios", colors, selectedItem, selectedSubItem, title) { onItemSelected(title, it) }
                SubMenuItem("Transporte", colors, selectedItem, selectedSubItem, title) { onItemSelected(title, it) }
                SubMenuItem("Proveedor de alimentos", colors, selectedItem, selectedSubItem, title) { onItemSelected(title, it) }
            }
        }
    }
}

@Composable
fun SubMenuItem(
    title: String,
    colors: RefugioColorPalette,
    selectedItem: String,
    selectedSubItem: String,
    parentTitle: String,
    onItemSelected: (String) -> Unit
) {
    Text(
        text = title,
        color = if (selectedItem == parentTitle && selectedSubItem == title) colors.onMenuItemSelected else colors.onMenuItem,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemSelected(title) }
            .background(if (selectedItem == parentTitle && selectedSubItem == title) colors.menuItemSelected else Color.Transparent)
            .padding(vertical = 8.dp, horizontal = 24.dp)
    )
}

@Composable
fun MenuItem(
    title: String,
    colors: RefugioColorPalette,
    selectedItem: String,
    selectedSubItem: String,
    onItemSelected: (String) -> Unit
) {
    Text(
        text = title,
        color = if (selectedItem == title && selectedSubItem.isEmpty()) colors.onMenuItemSelected else colors.onMenuItem,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemSelected(title) }
            .background(if (selectedItem == title && selectedSubItem.isEmpty()) colors.menuItemSelected else Color.Transparent)
            .padding(vertical = 8.dp, horizontal = 8.dp)
    )
}