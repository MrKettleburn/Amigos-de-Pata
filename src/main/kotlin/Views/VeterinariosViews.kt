package Views

import Class_DB.ActividadDB
import Class_DB.ContratadosDB
import Class_DB.ContratoDB
import Models.Actividad
import Models.Contrato
import Models.ContratoVeterinario
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


            FilterComponentsVeterinarios(
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            FloatingActionButton(
                onClick = { coroutineScope.launch { veterinarios = ContratadosDB.getVeterinariosFilter(null, null, null, null, null)}}
            ) {
                Icon(Icons.Default.ArrowCircleDown, contentDescription = "Recargar")
            }

            FloatingActionButton(
                onClick = { showDialog = true },
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
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
fun FilterComponentsVeterinarios(
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
        // Agregar encabezados de la tabla con íconos
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val headersWithIcons = listOf(
                    "Código" to getIconForAttribute("Código"),
                    "Nombre" to getIconForAttribute("Nombre"),
                    "Provincia" to getIconForAttribute("Provincia")
                )

                headersWithIcons.forEach { (header, icon) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = header,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.width(56.dp)) // Espacio para íconos de acciones
            }
            Divider(color = colors.primary, thickness = 1.5.dp)
        }

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
    val coroutineScope = rememberCoroutineScope()
    var showUpdateDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var checkPoder by remember { mutableStateOf<List<Contrato>?>(null) }
    var showError by remember { mutableStateOf(false) }

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
            listOf(row.id, row.nombre, row.provincia).forEach { value ->
                Text(
                    text = value,
                    modifier = Modifier.weight(1f)
                )
            }

            Row {
                IconButton(onClick = { showUpdateDialog = true }) {
                    Icon(Icons.Default.Edit, contentDescription = "Modificar")
                }
                IconButton(onClick = { showDeleteDialog=true }) {
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

        if (showUpdateDialog) {
            UpdateVeterinarioDialog(
                colors = colors,
                codigo = row.id.toInt(),
                nombreInicial = row.nombre,
                emailInicial = row.email,
                provinciaInicial = row.provincia,
                direccionInicial = row.direccion,
                telefonoInicial = row.telefono,
                especialidadInicial = row.especialidad,
                clinicaInicial = row.clinica,
                onDismissRequest = { showUpdateDialog = false },
                onVeterinarioUpdated = { codigo, nombre, email, provincia, direccion, telefono, especialidad, clinica ->
                    coroutineScope.launch {
                        val success = ContratadosDB.updateVeterinario(codigo, nombre, email, provincia, direccion, telefono, especialidad, clinica)
                        if (success) {
                            showUpdateDialog = false
                        } else {
                            print("ERROR")
                        }
                    }
                }
            )
        }

        if(showDeleteDialog)
        {
            ConfirmDeleteContratadoServicioDialog(
                colors = colors,
                title = "Eliminar Veterinario",
                text = "Veterinario",
                contrId = row.id.toInt(),
                onDismissRequest = { showDeleteDialog = false },
                onConfirmDelete = {
                    coroutineScope.launch {
                        val contratos = ContratoDB.getContratosPorContratadoID(row.id.toInt())

                        if(contratos!=null) {
                            checkPoder = contratos
                            if (contratos.isEmpty()) {
                                try {
                                    ContratadosDB.deleteVeterinario(row.id.toInt())
                                    showDeleteDialog = false
                                } catch (e: Exception) {
                                    println("Error al eliminar el vet: ${e.message}")
                                }
                            } else {

                                showError = true
                            }
                        }
                        else
                        {
                            println("ERROR")
                        }
                    }
                }
            )
        }
        if (showError) {
            showErrorDialog(
                title = "Error",
                message = "El veterinario no puede borrarse porque está vinculado a ${checkPoder?.size} contratos",
                onDismissRequest = { showError = false }
            )
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
                "Provincia" to veterinario.provincia
            ),
            expandedAttributes = mapOf(
                "Especialidad" to veterinario.especialidad,
                "Clínica" to veterinario.clinica,
                "Email" to veterinario.email,
                "Dirección" to veterinario.direccion,
                "Teléfono" to veterinario.telefono
            )
        )
    }
}
