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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@Composable
fun AnimalesEnRefugioMostrar(colors: RefugioColorPalette, selectedItem: String, selectedSubItem: String) {
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
            FilterComponentsAnimals(
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
fun FilterComponentsAnimals(
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
            modifier = Modifier.width(100.dp), // Tamaño fijo
            step = 1
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
    LazyColumn {
        items(data) { row ->
            ActividadesExpandableRow(colors, row)
            Divider(color = colors.primary, thickness = 1.5.dp)
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
            .height(400.dp)
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
                        modifier = Modifier.width(160.dp)  // Tamaño fijo
                    )
                    OutlinedTextField(
                        value = tipo.orEmpty(),
                        onValueChange = { tipo = if (it.isEmpty()) null else it },
                        label = { Text("Tipo Actividad") },
                        modifier = Modifier.width(150.dp)  // Tamaño fijo
                    )
                    OutlinedTextField(
                        value = tipoContrato.orEmpty(),
                        onValueChange = { tipoContrato = if (it.isEmpty()) null else it },
                        label = { Text("Tipo Contrato") },
                        modifier = Modifier.width(150.dp)  // Tamaño fijo
                    )
                    DatePicker(
                        label = { Text("Fecha Desde") },
                        selectedDate = fechaLI,
                        onDateChange = { fechaLI = it },
                        modifier = Modifier.width(180.dp)  // Tamaño fijo
                    )
                    DatePicker(
                        label = { Text("Fecha Hasta") },
                        selectedDate = fechaLS,
                        onDateChange = { fechaLS = it },
                        modifier = Modifier.width(180.dp)  // Tamaño fijo
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
                    Spacer(modifier = Modifier.width(30.dp))
                    Button(
                        onClick = {
                            /* AddActividadDialog()*/
                        },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Text("Agregar Actividad")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tabla de actividades
                ActividadesExpandableTable(colors, getActividadesTableRows(actividades))
            }
        }
    }
}