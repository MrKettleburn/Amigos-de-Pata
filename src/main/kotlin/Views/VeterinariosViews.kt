package Views

import Class_DB.ContratadosDB
import Models.Veterinario
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

@Composable
fun VeterinariosMostrar(colors: RefugioColorPalette, selectedItem: String, selectedSubItem: String) {
    val coroutineScope = rememberCoroutineScope()
    var veterinarios by remember { mutableStateOf<List<Veterinario>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }

    // Estados para los filtros
    var codigo by remember { mutableStateOf<String?>(null) }
    var nombre by remember { mutableStateOf<String?>(null) }
    var provincia by remember { mutableStateOf<String?>(null) }
    var especialidad by remember { mutableStateOf<String?>(null) }
    var clinica by remember { mutableStateOf<String?>(null) }

    // Cargar los datos iniciales
    LaunchedEffect(Unit) {
        veterinarios = ContratadosDB.getVeterinariosFilter(null, null, null, null, null)
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

            FilterComponents(
                colors,
                onFilterApplied = {
                    coroutineScope.launch {
                        veterinarios = ContratadosDB.getVeterinariosFilter(
                            codigo?.toIntOrNull(),
                            nombre,
                            provincia,
                            especialidad,
                            clinica
                        )
                    }
                },
                codigo = codigo,
                onCodigoChange = { codigo = it },
                nombre = nombre,
                onNombreChange = { nombre = it },
                provincia = provincia,
                onProvinciaChange = { provincia = it },
                especialidad = especialidad,
                onEspecialidadChange = { especialidad = it },
                clinica = clinica,
                onClinicaChange = { clinica = it }
            )

            VeterinariosExpandableTable(colors, getVeterinariosTableRows(veterinarios))
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Agregar")
        }

        if (showDialog) {
            AddVeterinarioDialog(
                colors = colors,
                onDismissRequest = { showDialog = false },
                onVeterinarioAdded = { newVeterinario ->
                    coroutineScope.launch {
                        ContratadosDB.createVeterinario(newVeterinario)
                        veterinarios = ContratadosDB.getVeterinariosFilter(null, null, null, null, null)
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
    provincia: String?,
    onProvinciaChange: (String?) -> Unit,
    especialidad: String?,
    onEspecialidadChange: (String?) -> Unit,
    clinica: String?,
    onClinicaChange: (String?) -> Unit
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
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = nombre.orEmpty(),
            onValueChange = { onNombreChange(if (it.isEmpty()) null else it) },
            label = { Text("Nombre") },
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = provincia.orEmpty(),
            onValueChange = { onProvinciaChange(if (it.isEmpty()) null else it) },
            label = { Text("Provincia") },
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = especialidad.orEmpty(),
            onValueChange = { onEspecialidadChange(if (it.isEmpty()) null else it) },
            label = { Text("Especialidad") },
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = clinica.orEmpty(),
            onValueChange = { onClinicaChange(if (it.isEmpty()) null else it) },
            label = { Text("Clínica") },
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = onFilterApplied,
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text("Filtrar")
        }
    }
}

@Composable
fun VeterinariosExpandableTable(colors: RefugioColorPalette, data: List<VeterinarioTableRow>) {
    LazyColumn {
        items(data) { row ->
            VeterinarioExpandableRow(colors, row)
            Divider(color = colors.primary, thickness = 1.5.dp)
        }
    }
}

@Composable
fun VeterinarioExpandableRow(colors: RefugioColorPalette, row: VeterinarioTableRow) {
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
                        imageVector = getIconForAttributeVet(key),
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
                        imageVector = getIconForAttributeVet(key),
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

fun getIconForAttributeVet(attribute: String): ImageVector {
    return when (attribute) {
        "Código" -> Icons.Default.Badge
        "Nombre" -> Icons.Default.Person
        "Email" -> Icons.Default.Email
        "Provincia" -> Icons.Default.LocationOn
        "Dirección" -> Icons.Default.Home
        "Teléfono" -> Icons.Default.Phone
        "Especialidad" -> Icons.Default.MedicalServices
        "Clínica" -> Icons.Default.LocalHospital
        else -> Icons.Default.Info
    }
}

data class VeterinarioTableRow(
    val id: String,
    val mainAttributes: Map<String, String>,
    val expandedAttributes: Map<String, String>
)

fun getVeterinariosTableRows(veterinarios: List<Veterinario>): List<VeterinarioTableRow> {
    return veterinarios.map { veterinario ->
        VeterinarioTableRow(
            id = veterinario.codigo.toString(),
            mainAttributes = mapOf(
                "Código" to "${veterinario.codigo}",
                "Nombre" to veterinario.nombre,
                "Provincia" to veterinario.provincia,
                "Especialidad" to veterinario.especialidad,
                "Clínica" to veterinario.clinica
            ),
            expandedAttributes = mapOf(
                "Email" to veterinario.email,
                "Dirección" to veterinario.direccion,
                "Teléfono" to veterinario.telefono
            )
        )
    }
}

@Composable
fun AddVeterinarioDialog(
    colors: RefugioColorPalette,
    onDismissRequest: () -> Unit,
    onVeterinarioAdded: (Veterinario) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var provincia by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var especialidad by remember { mutableStateOf("") }
    var clinica by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Agregar Veterinario", style = MaterialTheme.typography.h6)

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = provincia,
                    onValueChange = { provincia = it },
                    label = { Text("Provincia") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = especialidad,
                    onValueChange = { especialidad = it },
                    label = { Text("Especialidad") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = clinica,
                    onValueChange = { clinica = it },
                    label = { Text("Clínica") },
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
                        if (nombre.isNotBlank() && email.isNotBlank() && provincia.isNotBlank() &&
                            direccion.isNotBlank() && telefono.isNotBlank() && especialidad.isNotBlank() && clinica.isNotBlank()
                        ) {
                            onVeterinarioAdded(
                                Veterinario(
                                    codigo = 0, // El código se generará automáticamente en la base de datos
                                    nombre = nombre,
                                    email = email,
                                    provincia = provincia,
                                    direccion = direccion,
                                    telefono = telefono,
                                    especialidad = especialidad,
                                    clinica = clinica
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