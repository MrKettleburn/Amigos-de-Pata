package Views

import Class_DB.AnimalDB
import Models.Animal
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
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@Composable
fun AnimalesMostrar(colors: RefugioColorPalette, selectedItem: String, selectedSubItem: String) {
    val coroutineScope = rememberCoroutineScope()
    var animales by remember { mutableStateOf<List<Animal>>(emptyList()) }

    // Estados para los filtros
    var codigo by remember { mutableStateOf<String?>(null) }
    var nombre by remember { mutableStateOf<String?>(null) }
    var especie by remember { mutableStateOf<String?>(null) }
    var raza by remember { mutableStateOf<String?>(null) }
    var edad by remember { mutableStateOf<Int?>(null) }
    var fechaLI by remember { mutableStateOf<LocalDate?>(null) }
    var fechaLS by remember { mutableStateOf<LocalDate?>(null) }


    // Cargar los datos iniciales
    LaunchedEffect(Unit) {
        animales = AnimalDB.getAnimalesFilter(null, null, null, null, null, null, null)
    }

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
            FilterComponents(
                onFilterApplied = {
                    // Convertir los valores de los filtros a los tipos correctos y aplicar el filtro
                    coroutineScope.launch {
                        animales = AnimalDB.getAnimalesFilter(
                            codigo?.toIntOrNull(),
                            nombre,
                            especie,
                            raza,
                            edad,
                            fechaLI?.format(DateTimeFormatter.ISO_DATE),
                            fechaLS?.format(DateTimeFormatter.ISO_DATE)
                        )
                    }
                },
                codigo = codigo,
                onCodigoChange = { codigo = it },
                nombre = nombre,
                onNombreChange = { nombre = it },
                especie = especie,
                onEspecieChange = { especie = it },
                raza = raza,
                onRazaChange = { raza = it },
                edad = edad,
                onEdadChange = { edad = it },
                fechaLI = fechaLI,
                onFechaLIChange = { fechaLI = it },
                fechaLS = fechaLS,
                onFechaLSChange = { fechaLS = it }
            )

            // Tabla expandible
            ExpandableTable(getTableRows(animales))
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
fun FilterComponents(
    onFilterApplied: () -> Unit,
    codigo: String?,
    onCodigoChange: (String?) -> Unit,
    nombre: String?,
    onNombreChange: (String?) -> Unit,
    especie: String?,
    onEspecieChange: (String?) -> Unit,
    raza: String?,
    onRazaChange: (String?) -> Unit,
    edad: Int?,
    onEdadChange: (Int?) -> Unit,
    fechaLI: LocalDate?,
    onFechaLIChange: (LocalDate?) -> Unit,
    fechaLS: LocalDate?,
    onFechaLSChange: (LocalDate?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = codigo.orEmpty(),
            onValueChange = { onCodigoChange(if (it.isEmpty()) null else it) },
            label = { Text("Código") },
            modifier = Modifier.width(120.dp) // Tamaño fijo
        )
        OutlinedTextField(
            value = nombre.orEmpty(),
            onValueChange = { onNombreChange(if (it.isEmpty()) null else it) },
            label = { Text("Nombre") },
            modifier = Modifier.width(160.dp) // Tamaño fijo
        )
        OutlinedTextField(
            value = especie.orEmpty(),
            onValueChange = { onEspecieChange(if (it.isEmpty()) null else it) },
            label = { Text("Especie") },
            modifier = Modifier.width(120.dp) // Tamaño fijo
        )
        OutlinedTextField(
            value = raza.orEmpty(),
            onValueChange = { onRazaChange(if (it.isEmpty()) null else it) },
            label = { Text("Raza") },
            modifier = Modifier.width(120.dp) // Tamaño fijo
        )
        Spinner(
            value = edad ?: 0,
            onValueChange = { onEdadChange(if (it == 0) null else it) },
            label = { Text("Edad") },
            modifier = Modifier.width(100.dp) // Tamaño fijo
        )
        DatePicker(
            label = { Text("Fecha Desde") },
            selectedDate = fechaLI,
            onDateChange = { onFechaLIChange(it) },
            modifier = Modifier.width(140.dp) // Tamaño fijo
        )
        DatePicker(
            label = { Text("Fecha Hasta") },
            selectedDate = fechaLS,
            onDateChange = { onFechaLSChange(it) },
            modifier = Modifier.width(140.dp) // Tamaño fijo
        )
        Button(
            onClick = { onFilterApplied() },
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text("Filtrar")
        }
    }
}

@Composable
fun Spinner(
    value: Int,
    onValueChange: (Int) -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier
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
                val intValue = it.toIntOrNull() ?: 0
                onValueChange(intValue)
            },
            label = label,
            modifier = Modifier.weight(1f)
        )

        Column {
            IconButton(
                onClick = {
                    val newValue = (text.toIntOrNull() ?: 0) + 1
                    text = newValue.toString()
                    onValueChange(newValue)
                },
                modifier = Modifier.size(24.dp) // Tamaño pequeño
            ) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Incrementar")
            }
            IconButton(
                onClick = {
                    val newValue = (text.toIntOrNull() ?: 0) - 1
                    if (newValue >= 0) {
                        text = newValue.toString()
                        onValueChange(newValue)
                    }
                },
                modifier = Modifier.size(24.dp) // Tamaño pequeño
            ) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrementar")
            }
        }
    }
}
@Composable
fun DatePicker(
    label: @Composable () -> Unit,
    selectedDate: LocalDate?,
    onDateChange: (LocalDate?) -> Unit,
    modifier: Modifier = Modifier
) {
    var datePickerDialogVisible by remember { mutableStateOf(false) }
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val displayDate = selectedDate?.format(formatter) ?: "Seleccionar"

    Box(modifier = modifier) {
        OutlinedTextField(
            value = displayDate,
            onValueChange = {},
            readOnly = true,
            label = label,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { datePickerDialogVisible = true }
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
        "Código" -> Icons.Default.Info
        "Nombre" -> Icons.Default.Person
        "Especie" -> Icons.Default.Pets
        "Raza" -> Icons.Default.Pets
        "Edad" -> Icons.Default.Cake
        "Peso" -> Icons.Default.FitnessCenter
        "Días en Refugio" -> Icons.Default.Numbers
        "Fecha de Ingreso" -> Icons.Default.CalendarToday
        else -> Icons.Default.Info
    }
}

data class TableRow(
    val id: String,
    val mainAttributes: Map<String, String>,
    val expandedAttributes: Map<String, String>
)

// Función de ejemplo para obtener datos. En un caso real, esto se conectaría a tu base de datos.
fun getTableRows(animales: List<Animal>): List<TableRow> {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return animales.map { animal ->
        TableRow(
            id = animal.codigo.toString(),
            mainAttributes = mapOf(
                "Código" to "${animal.codigo}",
                "Nombre" to animal.nombre,
                "Especie" to animal.especie
            ),
            expandedAttributes = mapOf(
                "Raza" to animal.raza,
                "Edad" to "${animal.edad} años",
                "Peso" to "${animal.peso} kg",
                "Fecha de Ingreso" to animal.fecha_ingreso.format(formatter),
                "Días en Refugio" to animal.cantDias.toString()
            )
        )
    }
}