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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.window.Dialog
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AnimalesAdoptadosMostrar(colors: RefugioColorPalette, selectedItem: String, selectedSubItem: String) {
    val coroutineScope = rememberCoroutineScope()
    var animalesAdoptados by remember { mutableStateOf<List<AnimalAdoptado>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }

    // Estados para los filtros
    var codigo by remember { mutableStateOf<String?>(null) }
    var nombre by remember { mutableStateOf<String?>(null) }
    var especie by remember { mutableStateOf<String?>(null) }
    var raza by remember { mutableStateOf<String?>(null) }
    var edad by remember { mutableStateOf<String?>(null) }
    var fechaInf by remember { mutableStateOf<LocalDate?>(null) }
    var fechaSup by remember { mutableStateOf<LocalDate?>(null) }
    var precioInf by remember { mutableStateOf<String?>(null) }
    var precioSup by remember { mutableStateOf<String?>(null) }
    var nombreAdoptante by remember { mutableStateOf<String?>(null) }

    // Cargar los datos iniciales
    LaunchedEffect(Unit) {
        animalesAdoptados = AnimalDB.getAnimalAdoptFilter(null, null, null, null, null, null, null, null, null, null)
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xDCFFFFFF)).padding(16.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(1.dp)
                .background(Color(255, 251, 242, 0))
        ) {
            Text(
                text = "$selectedItem - $selectedSubItem",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            FilterComponentsA(
                colors,
                onFilterApplied = {
                    coroutineScope.launch {
                        animalesAdoptados = AnimalDB.getAnimalAdoptFilter(
                            codigo?.toIntOrNull(),
                            nombre,
                            especie,
                            raza,
                            edad?.toIntOrNull(),
                            fechaInf?.format(DateTimeFormatter.ISO_DATE),
                            fechaSup?.format(DateTimeFormatter.ISO_DATE),
                            precioInf?.toDoubleOrNull(),
                            precioSup?.toDoubleOrNull(),
                            nombreAdoptante
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
                fechaInf = fechaInf,
                onFechaInfChange = { fechaInf = it },
                fechaSup = fechaSup,
                onFechaSupChange = { fechaSup = it },
                precioInf = precioInf,
                onPrecioInfChange = { precioInf = it },
                precioSup = precioSup,
                onPrecioSupChange = { precioSup = it },
                nombreAdoptante = nombreAdoptante,
                onNombreAdoptanteChange = { nombreAdoptante = it }
            )

            AnimalesAdoptadosExpandableTable(colors, getAnimalesAdoptadosTableRows(animalesAdoptados))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        animalesAdoptados =
                            AnimalDB.getAnimalAdoptFilter(null, null, null, null, null, null, null, null, null, null)
                    }
                },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(Icons.Default.ArrowCircleDown, contentDescription = "Recargar")
            }
        }
    }
}

@Composable
fun FilterComponentsA(
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
    edad: String?,
    onEdadChange: (String?) -> Unit,
    fechaInf: LocalDate?,
    onFechaInfChange: (LocalDate?) -> Unit,
    fechaSup: LocalDate?,
    onFechaSupChange: (LocalDate?) -> Unit,
    precioInf: String?,
    onPrecioInfChange: (String?) -> Unit,
    precioSup: String?,
    onPrecioSupChange: (String?) -> Unit,
    nombreAdoptante: String?,
    onNombreAdoptanteChange: (String?) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = codigo.orEmpty(),
                onValueChange = { onCodigoChange(if (it.isEmpty()) null else it) },
                label = { Text("Código") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = nombre.orEmpty(),
                onValueChange = { onNombreChange(if (it.isEmpty()) null else it) },
                label = { Text("Nombre") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = especie.orEmpty(),
                onValueChange = { onEspecieChange(if (it.isEmpty()) null else it) },
                label = { Text("Especie") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = raza.orEmpty(),
                onValueChange = { onRazaChange(if (it.isEmpty()) null else it) },
                label = { Text("Raza") },
                modifier = Modifier.weight(1f)
            )
            Spinner(
                value = edad?.toIntOrNull() ?: 0,
                onValueChange = { onEdadChange(it.toString()) },
                label = { Text("Edad") },
                step = 1,
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = onFilterApplied,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text("Filtrar")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DatePicker(
                label = { Text("Fecha Inferior") },
                selectedDate = fechaInf,
                onDateChange = onFechaInfChange,
                modifier = Modifier.weight(1f)
            )
            DatePicker(
                label = { Text("Fecha Superior") },
                selectedDate = fechaSup,
                onDateChange = onFechaSupChange,
                modifier = Modifier.weight(1f)
            )
            Spinner(
                value = precioInf?.toDoubleOrNull() ?: 0.0,
                onValueChange = { onPrecioInfChange(it.toString()) },
                label = { Text("Precio Inferior") },
                step = 0.5,
                modifier = Modifier.weight(1f)
            )
            Spinner(
                value = precioSup?.toDoubleOrNull() ?: 0.0,
                onValueChange = { onPrecioSupChange(it.toString()) },
                label = { Text("Precio Superior") },
                step = 0.5,
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = nombreAdoptante.orEmpty(),
                onValueChange = { onNombreAdoptanteChange(if (it.isEmpty()) null else it) },
                label = { Text("Nombre del Adoptante") },
                modifier = Modifier.weight(1f)
            )
        }


    }
}
@Composable
fun AnimalesAdoptadosExpandableTable(colors: RefugioColorPalette, data: List<AnimalAdoptadoTableRow>) {
    LazyColumn {
        items(data) { row ->
            AnimalAdoptadoExpandableRow(colors, row)
            Divider(color = colors.primary, thickness = 1.5.dp)
        }
    }
}

@Composable
fun AnimalAdoptadoExpandableRow(colors: RefugioColorPalette, row: AnimalAdoptadoTableRow) {
    var expanded by remember { mutableStateOf(false) }
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

fun getAnimalesAdoptadosTableRows(animalesAdoptados: List<AnimalAdoptado>): List<AnimalAdoptadoTableRow> {
    return animalesAdoptados.map { animalAdoptado ->
        AnimalAdoptadoTableRow(
            id = animalAdoptado.codigo.toString(),
            nombre = animalAdoptado.nombre,
            especie = animalAdoptado.especie,
            raza = animalAdoptado.raza,
            edad = animalAdoptado.edad.toString(),
            peso = animalAdoptado.peso.toString(),
            fecha_ingreso = animalAdoptado.fecha_ingreso.toString(),
            precioAdop = animalAdoptado.precioAdop.toString(),
            nombreAdoptante = animalAdoptado.nombreAdoptante ?: "No adoptado",
            mainAttributes = mapOf(
                "Código" to "${animalAdoptado.codigo}",
                "Nombre" to animalAdoptado.nombre,
                "Especie" to animalAdoptado.especie
            ),
            expandedAttributes = mapOf(
                "Raza" to animalAdoptado.raza,
                "Edad" to "${animalAdoptado.edad}",
                "Peso" to "${animalAdoptado.peso}",
                "Fecha de ingreso" to "${animalAdoptado.fecha_ingreso}",
                "Precio de adopción" to "${animalAdoptado.precioAdop}",
                "Nombre del Adoptante" to (animalAdoptado.nombreAdoptante ?: "No adoptado")
            )
        )
    }
}