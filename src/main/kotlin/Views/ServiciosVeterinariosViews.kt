package Views

import Class_DB.ContratoDB
import Class_DB.ServiciosDB
import Models.Contrato
import Models.ServVeterinario
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun ServiciosVeterinariosMostrar(colors: RefugioColorPalette, selectedItem: String, selectedSubItem: String) {
    val coroutineScope = rememberCoroutineScope()
    var servicios by remember { mutableStateOf<List<ServVeterinario>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }

    // Estados para los filtros
    var codigo by remember { mutableStateOf<String?>(null) }
    var precioInf by remember { mutableStateOf<Double?>(null) }
    var precioSup by remember { mutableStateOf<Double?>(null) }
    var modalidad by remember { mutableStateOf<String?>(null) }

    // Cargar los datos iniciales
    LaunchedEffect(Unit) {
        servicios = ServiciosDB.getServiciosVeterinariosFilter(null, null, null, null)
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xDCFFFFFF)).padding(16.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(1.dp)
                .background(Color(255, 251, 242, 0))
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
                    coroutineScope.launch {
                        servicios = ServiciosDB.getServiciosVeterinariosFilter(
                            codigo?.toIntOrNull(),
                            precioInf,
                            precioSup,
                            modalidad
                        )
                    }
                },
                codigo = codigo,
                onCodigoChange = { codigo = it },
                precioInf = precioInf,
                onPrecioInfChange = { precioInf = it },
                precioSup = precioSup,
                onPrecioSupChange = { precioSup = it },
                modalidad = modalidad,
                onModalidadChange = { modalidad = it }
            )

            // Tabla de servicios
            ServiciosTable(colors, getServiciosTableRows(servicios))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            FloatingActionButton(
                onClick = { coroutineScope.launch { servicios = ServiciosDB.getServiciosVeterinariosFilter(null, null, null, null)}}
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
            AddServicioVeterinarioDialog(
                colors = colors,
                onDismissRequest = { showDialog = false },
                onServicioAdded = { newServicioV ->
                    coroutineScope.launch {
                        ServiciosDB.createServicioVeterinario(newServicioV)
                        servicios = ServiciosDB.getServiciosVeterinariosFilter(null, null, null, null)
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
    precioInf: Double?,
    onPrecioInfChange: (Double?) -> Unit,
    precioSup: Double?,
    onPrecioSupChange: (Double?) -> Unit,
    modalidad: String?,
    onModalidadChange: (String?) -> Unit
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

        Spinner(
            value = precioInf ?: 0.0,
            onValueChange = { onPrecioInfChange(it) },
            label = { Text("Precio Inferior") },
            modifier = Modifier.width(160.dp),
            step = 0.5
        )

        Spinner(
            value = precioSup ?: 0.0,
            onValueChange = { onPrecioSupChange(it) },
            label = { Text("Precio Superior") },
            modifier = Modifier.width(160.dp),
            step = 0.5
        )

        OutlinedTextField(
            value = modalidad.orEmpty(),
            onValueChange = { onModalidadChange(if (it.isEmpty()) null else it) },
            label = { Text("Modalidad") },
            modifier = Modifier.width(160.dp)
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
fun ServiciosTable(colors: RefugioColorPalette, data: List<ServicioVeterinarioTableRow>) {
    LazyColumn {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val headersWithIcons = listOf(
                    "Código" to getIconForAttribute("Código"),
                    "Modalidad" to getIconForAttribute("Modalidad"),
                    "Precio" to getIconForAttribute("Precio")
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
            }
            Divider(color = colors.primary, thickness = 1.5.dp)
        }

        items(data) { row ->
            ServiciosVeterinariosRow(colors, row)
            Divider(color = colors.primary, thickness = 1.5.dp)
        }
    }
}

@Composable
fun ServiciosVeterinariosRow(colors: RefugioColorPalette, row: ServicioVeterinarioTableRow) {
    val coroutineScope = rememberCoroutineScope()
    var showUpdateDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var checkPoder by remember { mutableStateOf<List<Contrato>?>(null) }
    var showError by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = row.id, modifier = Modifier.weight(1f))
        Text(text = row.modalidad, modifier = Modifier.weight(1f))
        Text(text = "$${row.precioUnit}", modifier = Modifier.weight(1f))
        Row {
            IconButton(onClick = { showUpdateDialog= true }) {
                Icon(Icons.Default.Edit, contentDescription = "Modificar")
            }
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }

        if (showUpdateDialog) {
            UpdateServicioVeterinarioDialog(
                colors = colors,
                codigo = row.id.toInt(),
                precioInicial = row.precioUnit,
                modalidadInicial = row.modalidad,
                onDismissRequest = { showUpdateDialog = false },
                onServicioUpdated = { codigo, modalidad, precio  ->
                    coroutineScope.launch {
                        if(ServiciosDB.updateServicioVeterinario(codigo, precio, modalidad))
                            showUpdateDialog = false
                        else
                            println("Revise los datos")
                    }
                }
            )
        }

        if(showDeleteDialog)
        {
            ConfirmDeleteContratadoServicioDialog(
                colors = colors,
                title = "Eliminar Servicio Veterinario",
                text = "servicio",
                contrId = row.id.toInt(),
                onDismissRequest = { showDeleteDialog = false },
                onConfirmDelete = {
                    coroutineScope.launch {
                        val contratos = ContratoDB.getContratosPorServicioID(row.id.toInt())

                        if(contratos!=null) {
                            checkPoder = contratos
                            if (contratos.isEmpty()) {
                                try {
                                    ServiciosDB.deleteServVet(row.id.toInt())
                                    showDeleteDialog = false
                                } catch (e: Exception) {
                                    println("Error al eliminar el servicio: ${e.message}")
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
                message = "El servicio veterinario no puede borrarse porque está vinculado a ${checkPoder?.size} contratos",
                onDismissRequest = { showError = false }
            )
        }
    }
}

fun getServiciosTableRows(servicios: List<ServVeterinario>): List<ServicioVeterinarioTableRow> {
    return servicios.map { servicio ->
        ServicioVeterinarioTableRow(
            id = servicio.codigo.toString(),
            precioUnit = servicio.precioUni,
            modalidad = servicio.modalidad,
            mainAttributes = mapOf(
                "Código" to "${servicio.codigo}",
                "Modalidad" to servicio.modalidad,
                "Precio" to "\$${servicio.precioUni}"
            )
        )
    }
}
