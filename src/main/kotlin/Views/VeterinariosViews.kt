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

            Button(
                onClick = { coroutineScope.launch {
                    veterinarios = ContratadosDB.getVeterinariosFilter(null,null,null,null,null)
                } },
            ) {
                Text("Recargar")
            }

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

fun getVeterinariosTableRows(veterinarios: List<Veterinario>): List<VeterinarioTableRow> {
    return veterinarios.map { veterinario ->
        VeterinarioTableRow(
            id = veterinario.codigo.toString(),
            nombre = veterinario.nombre,
            email = veterinario.email,
            provincia = veterinario.provincia,
            direccion = veterinario.direccion,
            telefono = veterinario.telefono,
            especialidad = veterinario.especialidad,
            clinica = veterinario.clinica,
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
