package Views

import Class_DB.ActividadDB
import Class_DB.AnimalDB
import Models.Actividad
import Models.Animal
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun AnimalesMostrar(colors: RefugioColorPalette, selectedItem: String, selectedSubItem: String) {
    val coroutineScope = rememberCoroutineScope()
    var animales by remember { mutableStateOf<List<Animal>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }

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
                .padding(1.dp)

        ) {
            // Título de la sección
            Text(
                text = "$selectedItem - $selectedSubItem",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(bottom = 4.dp)
            )


            // Componentes de filtrado
            FilterComponents(
                colors,
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
            AnimalsExpandableTable(colors, getAnimalsTableRows(animales))
        }

        // Botón flotante de agregar
        FloatingActionButton(
            onClick = {  showDialog = true  },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Agregar")
        }

        if (showDialog) {
            AddAnimalDialog(
                colors = colors,
                onDismissRequest = { showDialog = false },
                onAnimalAdded = { newAnimal ->
                    coroutineScope.launch {

                        AnimalDB.createAnimal(newAnimal)

                        animales = AnimalDB.getAnimalesFilter(null, null, null, null, null, null, null)
                        showDialog = false
                    }
                }
            )
        }
    }
}


@Composable
fun FilterComponents(
    colors: RefugioColorPalette,
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
        Spacer(modifier = Modifier.width(9.dp))
        OutlinedTextField(
            value = nombre.orEmpty(),
            onValueChange = { onNombreChange(if (it.isEmpty()) null else it) },
            label = { Text("Nombre") },
            modifier = Modifier.width(160.dp) // Tamaño fijo
        )
        Spacer(modifier = Modifier.width(9.dp))
        OutlinedTextField(
            value = especie.orEmpty(),
            onValueChange = { onEspecieChange(if (it.isEmpty()) null else it) },
            label = { Text("Especie") },
            modifier = Modifier.width(120.dp) // Tamaño fijo
        )
        Spacer(modifier = Modifier.width(9.dp))
        OutlinedTextField(
            value = raza.orEmpty(),
            onValueChange = { onRazaChange(if (it.isEmpty()) null else it) },
            label = { Text("Raza") },
            modifier = Modifier.width(120.dp) // Tamaño fijo
        )
        Spacer(modifier = Modifier.width(9.dp))
        Spinner(
            value = edad ?: 0,
            onValueChange = { onEdadChange(if (it == 0) null else it) },
            label = { Text("Edad") },
            modifier = Modifier.width(100.dp) // Tamaño fijo
        )
        Spacer(modifier = Modifier.width(9.dp))
        DatePicker(
            label = { Text("Fecha Desde") },
            selectedDate = fechaLI,
            onDateChange = { onFechaLIChange(it) },
            modifier = Modifier.width(180.dp) // Tamaño fijo
        )
        DatePicker(
            label = { Text("Fecha Hasta") },
            selectedDate = fechaLS,
            onDateChange = { onFechaLSChange(it) },
            modifier = Modifier.width(180.dp) // Tamaño fijo
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
fun AnimalsExpandableTable(colors: RefugioColorPalette, data: List<AnimalTableRow>) {
    LazyColumn {
        items(data) { row ->
            AnimalsExpandableRow(colors, row)
            Divider(color = colors.primary, thickness = 1.5.dp)
        }
    }
}

@Composable
fun ActividadesExpandableTable(colors: RefugioColorPalette, data: List<ActividadTableRow>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(0.dp, 100.dp) // Establecer una altura máxima de 400 dp
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = colors.primary,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            data.forEach { row ->
                ActividadesExpandableRow(colors, row)
                Divider(color = colors.primary, thickness = 1.5.dp)
            }
        }
    }
}

@Composable
fun AnimalsExpandableRow(colors: RefugioColorPalette, row: AnimalTableRow) {
    var expanded by remember { mutableStateOf(false) }
    var showActivityDialog by remember { mutableStateOf(false) }
    val backgroundColor = if (expanded) colors.menuBackground else Color.Transparent

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor) // Color de fondo para la fila expandida
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
                Button(
                    onClick = { showActivityDialog = true },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text("Ver Actividades")
                }
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

        if (showActivityDialog) {
            ActividadesDialog(
                colors = colors,
                codigoAnim = row.id.toInt(),
                nombreAnim = row.nombreAnim,
                onDismissRequest = { showActivityDialog = false }
            )
        }
    }
}

@Composable
fun ActividadesExpandableRow(colors: RefugioColorPalette, row: ActividadTableRow) {
    var expanded by remember { mutableStateOf(false) }
    val backgroundColor = if (expanded) colors.secondary else Color.Transparent

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor) // Color de fondo para la fila expandida
            .padding(vertical = 8.dp)
            .heightIn(100.dp) // Establecer una altura máxima para el Column

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
            Column(
                modifier = Modifier
                    .heightIn(200.dp) // Establecer una altura máxima para el Column anidado
            ) {
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
}

// Función para asignar íconos a cada atributo
fun getIconForAttribute(attribute: String): ImageVector {
    return when (attribute) {
        "Código" -> Icons.Default.Badge
        "Nombre" -> Icons.Default.Person
        "Especie" -> Icons.Default.Pets
        "Raza" -> Icons.Default.Pets
        "Edad" -> Icons.Default.Cake
        "Peso" -> Icons.Default.FitnessCenter
        "Días en Refugio" -> Icons.Default.Numbers
        "Fecha de Ingreso" -> Icons.Default.CalendarToday
        "Fecha" -> Icons.Default.CalendarToday
        "Tipo" -> Icons.Default.Bloodtype
        "Hora" -> Icons.Default.Timer
        "Descripción" -> Icons.Default.Textsms
        "Código Contrato" -> Icons.Default.Description
        else -> Icons.Default.Info
    }
}

data class AnimalTableRow(
    val id: String,
    val nombreAnim: String,
    val mainAttributes: Map<String, String>,
    val expandedAttributes: Map<String, String>
)

data class ActividadTableRow(
    val id: String,
    val mainAttributes: Map<String, String>,
    val expandedAttributes: Map<String, String>
)


fun getAnimalsTableRows(animales: List<Animal>): List<AnimalTableRow> {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return animales.map { animal ->
        AnimalTableRow(
            id = animal.codigo.toString(),
            nombreAnim = animal.nombre,
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

fun getActividadesTableRows(actividades: List<Actividad>): List<ActividadTableRow> {
    val formatterF = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val formatterH = DateTimeFormatter.ofPattern("HH:mm")
    return actividades.map { actividad ->
        ActividadTableRow(
            id = actividad.codigo.toString(),
            mainAttributes = mapOf(
                "Código" to "${actividad.codigo}",
                "Tipo" to actividad.tipo,
                "Fecha" to actividad.fecha.format(formatterF),
                "Hora" to actividad.hora.format(formatterH)
            ),
            expandedAttributes = mapOf(
                "Descripción" to actividad.descrip,
                "Código Contrato" to "${actividad.codigoContr}   Tipo de Contrato: ${actividad.tipoContrato}"
            )
        )
    }
}

@Composable
fun AddAnimalDialog(
    colors: RefugioColorPalette,
    onDismissRequest: () -> Unit,
    onAnimalAdded: (Animal) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf<Int?>(null) }
    var peso by remember { mutableStateOf<Double?>(null) }
    var fechaIngreso by remember { mutableStateOf<LocalDate?>(null) }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Agregar Animal", style = MaterialTheme.typography.h6)

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = especie,
                    onValueChange = { especie = it },
                    label = { Text("Especie") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = raza,
                    onValueChange = { raza = it },
                    label = { Text("Raza") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = edad?.toString() ?: "",
                    onValueChange = { edad = it.toIntOrNull() },
                    label = { Text("Edad") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = peso?.toString() ?: "",
                    onValueChange = { peso = it.toDoubleOrNull() },
                    label = { Text("Peso (kg)") },
                    modifier = Modifier.fillMaxWidth()
                )

                DatePicker(
                    label = { Text("Fecha de Ingreso") },
                    selectedDate = fechaIngreso,
                    onDateChange = { fechaIngreso = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        if (nombre.isNotBlank() && especie.isNotBlank() && raza.isNotBlank() && edad != null && peso != null && fechaIngreso != null) {
                            onAnimalAdded(
                                Animal(
                                    codigo = 0,
                                    nombre = nombre,
                                    especie = especie,
                                    raza = raza,
                                    edad = edad!!,
                                    peso = peso!!,
                                    cantDias = 0,
                                    fecha_ingreso = fechaIngreso!!
                                )
                            )
                        }
                    }) {
                        Text("Agregar")
                    }
                }
            }
        }
    }
}

@Composable
fun ActividadesDialog(
    colors: RefugioColorPalette,
    codigoAnim: Int,
    nombreAnim: String,
    onDismissRequest: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var actividades by remember { mutableStateOf<List<Actividad>>(emptyList()) }
    var codigoActividad by remember { mutableStateOf<String?>(null) }
    var tipo by remember { mutableStateOf<String?>(null) }
    var tipoContrato by remember { mutableStateOf<String?>(null) }
    var fechaLI by remember { mutableStateOf<LocalDate?>(null) }
    var fechaLS by remember { mutableStateOf<LocalDate?>(null) }

    // Cargar las actividades iniciales
    LaunchedEffect(Unit) {
        actividades = ActividadDB.getActividadesFilter(codigoAnim, null, null, null, null, null)
    }

    Box(
        modifier = Modifier
            .border(width = 1.2.dp, brush = Brush.verticalGradient(colors = listOf(colors.primary, colors.secondary)), shape = RoundedCornerShape(8.dp))
            .width(1200.dp)
            .height(500.dp)
            .background(Color.White) // Fondo para la caja
            .padding(0.dp)
    ) {
        Surface(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Actividades de ${nombreAnim}", style = MaterialTheme.typography.h6)

                    IconButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Volver")
                    }
                }

                // Componentes de filtrado
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = codigoActividad.orEmpty(),
                        onValueChange = { codigoActividad = if (it.isEmpty()) null else it },
                        label = { Text("Código Actividad") },
                        modifier = Modifier.width(130.dp)  // Tamaño fijo
                    )
                    OutlinedTextField(
                        value = tipo.orEmpty(),
                        onValueChange = { tipo = if (it.isEmpty()) null else it },
                        label = { Text("Tipo Actividad") },
                        modifier = Modifier.width(130.dp)  // Tamaño fijo
                    )
                    OutlinedTextField(
                        value = tipoContrato.orEmpty(),
                        onValueChange = { tipoContrato = if (it.isEmpty()) null else it },
                        label = { Text("Tipo Contrato") },
                        modifier = Modifier.width(130.dp)  // Tamaño fijo
                    )
                    DatePicker(
                        label = { Text("Fecha Desde") },
                        selectedDate = fechaLI,
                        onDateChange = { fechaLI = it },
                        modifier = Modifier.width(160.dp)  // Tamaño fijo
                    )
                    DatePicker(
                        label = { Text("Fecha Hasta") },
                        selectedDate = fechaLS,
                        onDateChange = { fechaLS = it },
                        modifier = Modifier.width(160.dp)  // Tamaño fijo
                    )
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                actividades = ActividadDB.getActividadesFilter(
                                    codigoAnim,
                                    codigoActividad?.toIntOrNull(),
                                    fechaLI?.format(DateTimeFormatter.ISO_DATE),
                                    fechaLS?.format(DateTimeFormatter.ISO_DATE),
                                    tipo,
                                    tipoContrato
                                )
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Text("Filtrar")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tabla de actividades
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = 1.dp,
                            color = colors.primary,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .heightIn(100.dp) // Establecer la altura máxima deseada
                    ) {
                        ActividadesExpandableTable(colors, getActividadesTableRows(actividades))
                    }
                }
            }
        }
    }
}
