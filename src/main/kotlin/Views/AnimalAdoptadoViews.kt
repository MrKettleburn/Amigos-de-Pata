package Views

import Class_DB.AnimalDB
import Models.AnimalAdoptado
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
import Class_DB.ActividadDB
import Class_DB.ContratadosDB
import Class_DB.ContratoDB
import Class_DB.ServiciosDB
import Models.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import java.time.temporal.ChronoUnit

@Composable
fun AnimalesEnAdopcionMostrar(colors: RefugioColorPalette, selectedItem: String, selectedSubItem: String) {
    val coroutineScope = rememberCoroutineScope()
    var animales by remember { mutableStateOf<List<AnimalAdoptado>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }

    // Estados para los filtros
    var codigo by remember { mutableStateOf<String?>(null) }
    var nombre by remember { mutableStateOf<String?>(null) }
    var especie by remember { mutableStateOf<String?>(null) }
    var raza by remember { mutableStateOf<String?>(null) }
    var edad by remember { mutableStateOf<Int?>(null) }
    var fechaLI by remember { mutableStateOf<LocalDate?>(null) }
    var fechaLS by remember { mutableStateOf<LocalDate?>(null) }
    var precio by remember { mutableStateOf<Double?>(null) }

    // Cargar los datos iniciales
    LaunchedEffect(Unit) {
        animales = AnimalDB.getAnimalAdoptFilter(null, null, null, null, null, null, null, null)
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xDCFFFFFF)).padding(16.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(1.dp)
                .background(Color(255,251,242,0))
        ) {
            // Título de la sección
            Text(
                text = "$selectedItem - $selectedSubItem",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Componentes de filtrado
            FilterComponentsAnimalsAdopted(
                colors,
                onFilterApplied = {
                    // Convertir los valores de los filtros a los tipos correctos y aplicar el filtro
                    coroutineScope.launch {
                        animales = AnimalDB.getAnimalAdoptFilter(
                            codigo?.toIntOrNull(),
                            nombre,
                            especie,
                            raza,
                            edad,
                            fechaLI?.format(DateTimeFormatter.ISO_DATE),
                            fechaLS?.format(DateTimeFormatter.ISO_DATE),
                            precio
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
                onFechaLSChange = { fechaLS = it },
                precio = precio,
                onPrecioChange = { precio = it }
            )

            // Tabla expandible
            AnimalsAdoptedExpandableTable(colors, getAnimalsAdoptedTableRows(animales))
        }

        // Botón flotante de agregar
        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Agregar")
        }

        if (showDialog) {
            AddAnimalAdoptadoDialog(
                colors = colors,
                onDismissRequest = { showDialog = false },
                onAnimalAdded = { newAnimal ->
                    coroutineScope.launch {
                        AnimalDB.createAnimalAdoptado(newAnimal)
                        animales = AnimalDB.getAnimalAdoptFilter(null, null, null, null, null, null, null, null)
                        showDialog = false
                    }
                }
            )
        }
    }
}

@Composable
fun FilterComponentsAnimalsAdopted(
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
    onFechaLSChange: (LocalDate?) -> Unit,
    precio: Double?,
    onPrecioChange: (Double?) -> Unit
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
            modifier = Modifier.width(120.dp)
        )
        Spacer(modifier = Modifier.width(9.dp))
        OutlinedTextField(
            value = nombre.orEmpty(),
            onValueChange = { onNombreChange(if (it.isEmpty()) null else it) },
            label = { Text("Nombre") },
            modifier = Modifier.width(160.dp)
        )
        Spacer(modifier = Modifier.width(9.dp))
        OutlinedTextField(
            value = especie.orEmpty(),
            onValueChange = { onEspecieChange(if (it.isEmpty()) null else it) },
            label = { Text("Especie") },
            modifier = Modifier.width(120.dp)
        )
        Spacer(modifier = Modifier.width(9.dp))
        OutlinedTextField(
            value = raza.orEmpty(),
            onValueChange = { onRazaChange(if (it.isEmpty()) null else it) },
            label = { Text("Raza") },
            modifier = Modifier.width(120.dp)
        )
        Spacer(modifier = Modifier.width(9.dp))
        Spinner(
            value = edad ?: 0,
            onValueChange = { onEdadChange(if (it == 0) null else it) },
            label = { Text("Edad") },
            modifier = Modifier.width(100.dp),
            step = 1
        )
        Spacer(modifier = Modifier.width(9.dp))
        DatePicker(
            label = { Text("Fecha Desde") },
            selectedDate = fechaLI,
            onDateChange = { onFechaLIChange(it) },
            modifier = Modifier.width(180.dp)
        )
        DatePicker(
            label = { Text("Fecha Hasta") },
            selectedDate = fechaLS,
            onDateChange = { onFechaLSChange(it) },
            modifier = Modifier.width(180.dp)
        )
        Spacer(modifier = Modifier.width(9.dp))
        Spinner(
            value = precio ?: 0.0,
            onValueChange = { onPrecioChange(if (it == 0.0) null else it) },
            label = { Text("Precio") },
            modifier = Modifier.width(120.dp),
            step = 0.5
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
fun AnimalsAdoptedExpandableTable(colors: RefugioColorPalette, data: List<AnimalAdoptadoTableRow>) {
    LazyColumn {
        items(data) { row ->
            AnimalsAdoptedExpandableRow(colors, row)
            Divider(color = colors.primary, thickness = 1.5.dp)
        }
    }
}
@Composable
fun AnimalsAdoptedExpandableRow(colors: RefugioColorPalette, row: AnimalAdoptadoTableRow) {
    var expanded by remember { mutableStateOf(false) }
    var showActivityDialog by remember { mutableStateOf(false) }
    val backgroundColor = if (expanded) colors.menuBackground else Color.Transparent

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
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
//                IconButton(onClick = { /* TODO: Implementar modificar */ }) {
//                    Icon(Icons.Default.Edit, contentDescription = "Modificar")
//                }
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
                        imageVector = getIconForAttributeAA(key),
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

fun getAnimalsAdoptedTableRows(animales: List<AnimalAdoptado>): List<AnimalAdoptadoTableRow> {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return animales.map { animal ->
        AnimalAdoptadoTableRow(
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
                "Días en Adopción" to animal.cantDias.toString(),
                "Precio" to "${animal.precioAdop} USD"
            )
        )
    }
}
fun getIconForAttributeAA(attribute: String): ImageVector {
    return when (attribute) {
        "Código" -> Icons.Default.Badge
        "Nombre" -> Icons.Default.Person
        "Especie" -> Icons.Default.Pets
        "Raza" -> Icons.Default.Pets
        "Edad" -> Icons.Default.Cake
        "Peso" -> Icons.Default.FitnessCenter
        "Días en Adopción" -> Icons.Default.Numbers
        "Fecha de Ingreso" -> Icons.Default.CalendarToday
        "Precio" -> Icons.Default.AttachMoney
        else -> Icons.Default.Info
    }
}
data class AnimalAdoptadoTableRow(
    val id: String,
    val nombreAnim: String,
    val mainAttributes: Map<String, String>,
    val expandedAttributes: Map<String, String>
)

@Composable
fun AddAnimalAdoptadoDialog(
    colors: RefugioColorPalette,
    onDismissRequest: () -> Unit,
    onAnimalAdded: (AnimalAdoptado) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf<Int?>(null) }
    var peso by remember { mutableStateOf<Double?>(null) }
    var fechaIngreso by remember { mutableStateOf<LocalDate?>(null) }
    var precio by remember { mutableStateOf<Double?>(null) }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Agregar Animal en Adopción", style = MaterialTheme.typography.h6)

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
                Row(modifier = Modifier.padding(0.dp))
                {
                    OutlinedTextField(
                        value = edad?.toString() ?: "",
                        onValueChange = { edad = it.toIntOrNull() },
                        label = { Text("Edad") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    OutlinedTextField(
                        value = peso?.toString() ?: "",
                        onValueChange = { peso = it.toDoubleOrNull() },
                        label = { Text("Peso (kg)") },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(9.dp))
                Spinner(
                    value = precio ?: 0.0,
                    onValueChange = { precio = it },
                    label = { Text("Precio (USD)") },
                    modifier = Modifier.fillMaxWidth(),
                    step = 0.5
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
                        if (nombre.isNotBlank() && especie.isNotBlank() && raza.isNotBlank() && edad != null && peso != null && fechaIngreso != null && precio != null) {
                            onAnimalAdded(
                                AnimalAdoptado(
                                    codigo = 0,
                                    nombre = nombre,
                                    especie = especie,
                                    raza = raza,
                                    edad = edad!!,
                                    peso = peso!!,
                                    cantDias = 0,
                                    fecha_ingreso = fechaIngreso!!,
                                    precioAdop = precio!!
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
