package Views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainContent(colors: RefugioColorPalette) {
    var selectedItem by remember { mutableStateOf("") }
    var selectedSubItem by remember { mutableStateOf("") }

    Row(modifier = Modifier.fillMaxSize()) {
        AnimatedSideMenu(colors, selectedItem, selectedSubItem) { item, subItem ->
            selectedItem = item
            selectedSubItem = subItem
        }
        MainArea(colors, selectedItem, selectedSubItem)
    }
}

@Composable
fun MainArea(colors: RefugioColorPalette, selectedItem: String, selectedSubItem: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Título de la sección
            Text(
                text = "$selectedItem - $selectedSubItem",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Componentes de filtrado
            FilterComponents()

            // Tabla expandible
            ExpandableTable(getDataForSection(selectedItem, selectedSubItem))
        }

        // Botón flotante de agregar
        FloatingActionButton(
            onClick = { /* TODO: Implementar agregar */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Agregar")
        }
    }
}

@Composable
fun FilterComponents() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = { /* TODO: Implementar filtrado */ },
            label = { Text("Buscar") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { /* TODO: Implementar filtrado avanzado */ }) {
            Text("Filtros avanzados")
        }
    }
}

@Composable
fun ExpandableTable(data: List<TableRow>) {
    LazyColumn {
        items(data) { row ->
            ExpandableRow(row)
        }
    }
}

@Composable
fun ExpandableRow(row: TableRow) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            row.mainAttributes.forEach { (key, value) ->
                Text("$key: $value", modifier = Modifier.weight(1f))
            }
            Row {
                IconButton(onClick = { /* TODO: Implementar modificar */ }) {
                    Icon(Icons.Default.Edit, contentDescription = "Modificar")
                }
                IconButton(onClick = { /* TODO: Implementar eliminar */ }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }
        }

        if (expanded) {
            row.expandedAttributes.forEach { (key, value) ->
                Text("$key: $value", modifier = Modifier.padding(start = 16.dp, top = 4.dp))
            }
        }
    }
}

// El resto del código permanece igual

data class TableRow(
    val id: String,
    val mainAttributes: Map<String, String>,
    val expandedAttributes: Map<String, String>
)

// Función de ejemplo para obtener datos. En un caso real, esto se conectaría a tu base de datos.
fun getDataForSection(selectedItem: String, selectedSubItem: String): List<TableRow> {
    // Aquí deberías implementar la lógica para obtener los datos reales de tu base de datos
    return when ("$selectedItem-$selectedSubItem") {
        "Contratos-Transporte" -> List(10) { index ->
            TableRow(
                id = "V$index",
                mainAttributes = mapOf(
                    "Nombre" to "Veterinario $index",
                    "Especialidad" to "Especialidad $index"
                ),
                expandedAttributes = mapOf(
                    "Teléfono" to "123-456-789$index",
                    "Email" to "vet$index@example.com",
                    "Fecha de contrato" to "2023-0$index-01"
                )
            )
        }
        // Añade más casos para otras combinaciones de selectedItem y selectedSubItem
        else -> emptyList()
    }
}