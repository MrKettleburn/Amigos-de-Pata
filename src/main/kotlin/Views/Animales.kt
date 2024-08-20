package Views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AnimalesMostrar(colors: RefugioColorPalette, selectedItem: String, selectedSubItem: String) {
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Agrega el ícono y el subtítulo en negrita
                    Icon(
                        imageVector = getIconForAttribute(key),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$key: ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = value)
                }
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                ) {

                    // Agrega el ícono y el subtítulo en negrita para los atributos expandibles
                    Icon(
                        imageVector = getIconForAttribute(key),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$key: ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = value)
                }
            }
        }
    }
}

// Función para asignar íconos a cada atributo
fun getIconForAttribute(attribute: String): ImageVector {
    return when (attribute) {
        "Nombre" -> Icons.Default.Person
        "Especialidad" -> Icons.Default.Work
        "Teléfono" -> Icons.Default.Phone
        "Email" -> Icons.Default.Email
        "Fecha de contrato" -> Icons.Default.CalendarToday
        else -> Icons.Default.Info
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
    return List(10) { index ->
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

    }

@Composable
fun PatientDetailItem(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        androidx.compose.material3.Icon(
            imageVector = icon,
            contentDescription = label,
            tint = androidx.compose.material3.MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            androidx.compose.material3.Text(
                label,
                style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            androidx.compose.material3.Text(
                value,
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
            )
        }
    }
}