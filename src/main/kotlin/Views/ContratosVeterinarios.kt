//package Views
//
//
//import Class_DB.ActividadDB
//import Class_DB.AnimalDB
//import Class_DB.ContratoDB
//import Models.Actividad
//import Models.Animal
//import Models.ContratoVeterinario
//import androidx.compose.foundation.*
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material.icons.rounded.Badge
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import kotlinx.coroutines.launch
//import java.time.LocalDate
//import java.time.format.DateTimeFormatter
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.grid.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.window.Dialog
//import java.time.YearMonth
//import java.time.format.TextStyle
//import java.util.*
//
//
//@Composable
//fun ContratosVeterinariosMostrar(colors: RefugioColorPalette, selectedItem: String, selectedSubItem: String) {
//    val coroutineScope = rememberCoroutineScope()
//    var contratos by remember { mutableStateOf<List<ContratoVeterinario>>(emptyList()) }
//    var showDialog by remember { mutableStateOf(false) }
//
//
//    // Estados para los filtros
//    var codigo by remember { mutableStateOf<String?>(null) }
//    var precioLI by remember { mutableStateOf<Double?>(null) }
//    var precioLS by remember { mutableStateOf<Double?>(null) }
//    var nombreVet by remember { mutableStateOf<String?>(null) }
//    var clinicaVet by remember { mutableStateOf<String?>(null) }
//    var provinciaVet by remember { mutableStateOf<String?>(null) }
//    var especialidad by remember { mutableStateOf<String?>(null) }
//    var modalidadServVet by remember { mutableStateOf<String?>(null) }
//    var fechaInicioLI by remember { mutableStateOf<LocalDate?>(null) }
//    var fechaInicioLS by remember { mutableStateOf<LocalDate?>(null) }
//    var fechaFinLI by remember { mutableStateOf<LocalDate?>(null) }
//    var fechaFinLS by remember { mutableStateOf<LocalDate?>(null) }
//    var fechaConcilLI by remember { mutableStateOf<LocalDate?>(null) }
//    var fechaConcilLS by remember { mutableStateOf<LocalDate?>(null) }
//
//
//    // Cargar los datos iniciales
//    LaunchedEffect(Unit) {
//        contratos = ContratoDB.getContratosVeterinariosFilter(
//            null,
//            null,
//            null,
//            null,
//            null,
//            null,
//            null,
//            null,
//            null,
//            null,
//            null,
//            null,
//            null,
//            null)
//    }
//
//    Box(modifier = Modifier.fillMaxSize().background(Color(0xDCFFFFFF)).padding(16.dp)) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(1.dp)
//                .background(Color(255,251,242,0))
//        ) {
//            // Título de la sección
//            Text(
//                text = "$selectedItem - $selectedSubItem",
//                style = MaterialTheme.typography.h5,
//                modifier = Modifier.padding(bottom = 4.dp)
//            )
//
//
//            // Componentes de filtrado
//            FilterComponentsVeterinarios(
//                colors,
//                onFilterApplied = {
//                    // Convertir los valores de los filtros a los tipos correctos y aplicar el filtro
//                    coroutineScope.launch {
//                        contratos = ContratoDB.getContratosVeterinariosFilter(
//                            codigo?.toIntOrNull(),
//                            precioLI,
//                            precioLS,
//                            nombreVet,
//                            clinicaVet,
//                            provinciaVet,
//                            especialidad,
//                            modalidadServVet,
//                            fechaInicioLI?.format(DateTimeFormatter.ISO_DATE),
//                            fechaInicioLS?.format(DateTimeFormatter.ISO_DATE),
//                            fechaFinLI?.format(DateTimeFormatter.ISO_DATE),
//                            fechaFinLS?.format(DateTimeFormatter.ISO_DATE),
//                            fechaConcilLI?.format(DateTimeFormatter.ISO_DATE),
//                            fechaConcilLS?.format(DateTimeFormatter.ISO_DATE)
//                        )
//                    }
//                },
//                codigo = codigo,
//                onCodigoChange = { codigo = it },
//                precioLI = precioLI,
//                onPrecioLIChange = { precioLI = it },
//                precioLS = precioLS,
//                onPrecioLSChange = { precioLS = it },
//                nombreVet = nombreVet,
//                onNombreVetChange = { nombreVet = it },
//                clinicaVet = clinicaVet,
//                onClinicaVetChange = { clinicaVet = it },
//
//                raza = raza,
//                onRazaChange = { raza = it },
//                edad = edad,
//                onEdadChange = { edad = it },
//                fechaLI = fechaLI,
//                onFechaLIChange = { fechaLI = it },
//                fechaLS = fechaLS,
//                onFechaLSChange = { fechaLS = it }
//            )
//
//            // Tabla expandible
//            AnimalsExpandableTable(colors, getAnimalsTableRows(animales))
//        }
//
//        // Botón flotante de agregar
//        FloatingActionButton(
//            onClick = {  showDialog = true  },
//            modifier = Modifier
//                .align(Alignment.BottomEnd)
//                .padding(16.dp)
//        ) {
//            Icon(Icons.Default.Add, contentDescription = "Agregar")
//        }
//
//        if (showDialog) {
//            AddAnimalDialog(
//                colors = colors,
//                onDismissRequest = { showDialog = false },
//                onAnimalAdded = { newAnimal ->
//                    coroutineScope.launch {
//
//                        AnimalDB.createAnimal(newAnimal)
//
//                        animales = AnimalDB.getAnimalesFilter(null, null, null, null, null, null, null)
//                        showDialog = false
//                    }
//                }
//            )
//        }
//    }
//}
//
//
//@Composable
//fun FilterComponentsVeterinarios(
//    colors: RefugioColorPalette,
//    onFilterApplied: () -> Unit,
//    codigo: String?,
//    onCodigoChange: (String?) -> Unit,
//    precioLI: Double?,
//    onPrecioLIChange: (Double?) -> Unit,
//    precioLS: Double?,
//    onPrecioLSChange: (Double?) -> Unit,
//    nombreVet: String?,
//    onNombreVetChange: (String?) -> Unit,
//    clinicaVet: String?,
//    onClinicaVetChange: (String?) -> Unit,
//    edad: Int?,
//    onEdadChange: (Int?) -> Unit,
//    fechaLI: LocalDate?,
//    onFechaLIChange: (LocalDate?) -> Unit,
//    fechaLS: LocalDate?,
//    onFechaLSChange: (LocalDate?) -> Unit
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        horizontalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        OutlinedTextField(
//            value = codigo.orEmpty(),
//            onValueChange = { onCodigoChange(if (it.isEmpty()) null else it) },
//            label = { Text("Código") },
//            modifier = Modifier.width(120.dp) // Tamaño fijo
//        )
//        Spacer(modifier = Modifier.width(9.dp))
//        OutlinedTextField(
//            value = nombre.orEmpty(),
//            onValueChange = { onNombreChange(if (it.isEmpty()) null else it) },
//            label = { Text("Nombre") },
//            modifier = Modifier.width(160.dp) // Tamaño fijo
//        )
//        Spacer(modifier = Modifier.width(9.dp))
//        OutlinedTextField(
//            value = especie.orEmpty(),
//            onValueChange = { onEspecieChange(if (it.isEmpty()) null else it) },
//            label = { Text("Especie") },
//            modifier = Modifier.width(120.dp) // Tamaño fijo
//        )
//        Spacer(modifier = Modifier.width(9.dp))
//        OutlinedTextField(
//            value = raza.orEmpty(),
//            onValueChange = { onRazaChange(if (it.isEmpty()) null else it) },
//            label = { Text("Raza") },
//            modifier = Modifier.width(120.dp) // Tamaño fijo
//        )
//        Spacer(modifier = Modifier.width(9.dp))
//        Spinner(
//            value = edad ?: 0,
//            onValueChange = { onEdadChange(if (it == 0) null else it) },
//            label = { Text("Edad") },
//            modifier = Modifier.width(100.dp) // Tamaño fijo
//        )
//        Spacer(modifier = Modifier.width(9.dp))
//        DatePicker(
//            label = { Text("Fecha Desde") },
//            selectedDate = fechaLI,
//            onDateChange = { onFechaLIChange(it) },
//            modifier = Modifier.width(180.dp) // Tamaño fijo
//        )
//        DatePicker(
//            label = { Text("Fecha Hasta") },
//            selectedDate = fechaLS,
//            onDateChange = { onFechaLSChange(it) },
//            modifier = Modifier.width(180.dp) // Tamaño fijo
//        )
//        Button(
//            onClick = { onFilterApplied() },
//            modifier = Modifier.align(Alignment.CenterVertically)
//        ) {
//            Text("Filtrar")
//        }
//    }
//}
//////
//////@Composable
//////fun Spinner(
//////    value: Int,
//////    onValueChange: (Int) -> Unit,
//////    label: @Composable () -> Unit,
//////    modifier: Modifier = Modifier
//////) {
//////    var text by remember { mutableStateOf(value.toString()) }
//////
//////    Row(
//////        modifier = modifier,
//////        verticalAlignment = Alignment.CenterVertically
//////    ) {
//////        OutlinedTextField(
//////            value = text,
//////            onValueChange = {
//////                text = it
//////                val intValue = it.toIntOrNull() ?: 0
//////                onValueChange(intValue)
//////            },
//////            label = label,
//////            modifier = Modifier.weight(1f)
//////        )
//////
//////        Column {
//////            IconButton(
//////                onClick = {
//////                    val newValue = (text.toIntOrNull() ?: 0) + 1
//////                    text = newValue.toString()
//////                    onValueChange(newValue)
//////                },
//////                modifier = Modifier.size(24.dp) // Tamaño pequeño
//////            ) {
//////                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Incrementar")
//////            }
//////            IconButton(
//////                onClick = {
//////                    val newValue = (text.toIntOrNull() ?: 0) - 1
//////                    if (newValue >= 0) {
//////                        text = newValue.toString()
//////                        onValueChange(newValue)
//////                    }
//////                },
//////                modifier = Modifier.size(24.dp) // Tamaño pequeño
//////            ) {
//////                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrementar")
//////            }
//////        }
//////    }
//////}
//////
//////
//////@Composable
//////fun AnimalsExpandableTable(colors: RefugioColorPalette, data: List<AnimalTableRow>) {
//////    LazyColumn {
//////        items(data) { row ->
//////            AnimalsExpandableRow(colors, row)
//////            Divider(color = colors.primary, thickness = 1.5.dp)
//////        }
//////    }
//////}
//
//
//////@Composable
//////fun AnimalsExpandableRow(colors: RefugioColorPalette, row: AnimalTableRow) {
//////    var expanded by remember { mutableStateOf(false) }
//////    var showActivityDialog by remember { mutableStateOf(false) }
//////    val backgroundColor = if (expanded) colors.menuBackground else Color.Transparent
//////
//////    Column(
//////        modifier = Modifier
//////            .fillMaxWidth()
//////            .background(backgroundColor) // Color de fondo para la fila expandida
//////            .padding(vertical = 8.dp)
//////    ) {
//////        Row(
//////            modifier = Modifier
//////                .fillMaxWidth()
//////                .clickable { expanded = !expanded },
//////            horizontalArrangement = Arrangement.SpaceBetween,
//////            verticalAlignment = Alignment.CenterVertically
//////        ) {
//////            row.mainAttributes.forEach { (key, value) ->
//////                Row(
//////                    verticalAlignment = Alignment.CenterVertically,
//////                    modifier = Modifier.weight(1f)
//////                ) {
//////                    Icon(
//////                        imageVector = getIconForAttribute(key),
//////                        contentDescription = null,
//////                        modifier = Modifier.size(20.dp)
//////                    )
//////                    Spacer(modifier = Modifier.width(4.dp))
//////                    Text(
//////                        text = "$key: ",
//////                        fontWeight = FontWeight.Bold
//////                    )
//////                    Text(text = value)
//////                }
//////            }
//////            Row {
//////                Button(
//////                    onClick = { showActivityDialog = true },
//////                    modifier = Modifier.align(Alignment.CenterVertically)
//////                ) {
//////                    Text("Ver Actividades")
//////                }
//////                IconButton(onClick = { /* TODO: Implementar modificar */ }) {
//////                    Icon(Icons.Default.Edit, contentDescription = "Modificar")
//////                }
//////                IconButton(onClick = { /* TODO: Implementar eliminar */ }) {
//////                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
//////                }
//////                IconButton(onClick = { expanded = !expanded }) {
//////                    Icon(
//////                        if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
//////                        contentDescription = if (expanded) "Collapse" else "Expand"
//////                    )
//////                }
//////            }
//////        }
//////
//////        if (expanded) {
//////            row.expandedAttributes.forEach { (key, value) ->
//////                Row(
//////                    verticalAlignment = Alignment.CenterVertically,
//////                    modifier = Modifier.padding(start = 16.dp, top = 8.dp)
//////                ) {
//////                    Icon(
//////                        imageVector = getIconForAttribute(key),
//////                        contentDescription = null,
//////                        modifier = Modifier.size(20.dp)
//////                    )
//////                    Spacer(modifier = Modifier.width(8.dp))
//////                    Text(
//////                        text = "$key: ",
//////                        fontWeight = FontWeight.Bold
//////                    )
//////                    Text(text = value)
//////                }
//////            }
//////        }
//////
//////        if (showActivityDialog) {
//////            ActividadesDialog(
//////                colors = colors,
//////                codigoAnim = row.id.toInt(),
//////                nombreAnim = row.nombreAnim,
//////                onDismissRequest = { showActivityDialog = false }
//////            )
//////        }
//////    }
//////}
//////
//
//////fun getAnimalsTableRows(animales: List<Animal>): List<AnimalTableRow> {
//////    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
//////    return animales.map { animal ->
//////        AnimalTableRow(
//////            id = animal.codigo.toString(),
//////            nombreAnim = animal.nombre,
//////            mainAttributes = mapOf(
//////                "Código" to "${animal.codigo}",
//////                "Nombre" to animal.nombre,
//////                "Especie" to animal.especie
//////            ),
//////            expandedAttributes = mapOf(
//////                "Raza" to animal.raza,
//////                "Edad" to "${animal.edad} años",
//////                "Peso" to "${animal.peso} kg",
//////                "Fecha de Ingreso" to animal.fecha_ingreso.format(formatter),
//////                "Días en Refugio" to animal.cantDias.toString()
//////            )
//////        )
//////    }
//////}
