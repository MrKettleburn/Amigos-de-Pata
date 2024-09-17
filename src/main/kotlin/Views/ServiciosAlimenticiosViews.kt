package Views

import Class_DB.ContratoDB
import Class_DB.ServiciosDB
import Models.Contrato
import Models.ServAlimenticio
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
fun ServiciosAlimenticioMostrar(colors: RefugioColorPalette, selectedItem: String, selectedSubItem: String) {
    val coroutineScope = rememberCoroutineScope()
    var servicios by remember { mutableStateOf<List<ServAlimenticio>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }

    // Estados para los filtros
    var codigo by remember { mutableStateOf<String?>(null) }
    var precioInf by remember { mutableStateOf<Double?>(null) }
    var precioSup by remember { mutableStateOf<Double?>(null) }
    var tipoAlimento by remember { mutableStateOf<String?>(null) }

    // Cargar los datos iniciales
    LaunchedEffect(Unit) {
        servicios = ServiciosDB.getServiciosAlimenticiosFilter(null, null, null, null)
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
            FilterComponentsAlimenticio(
                colors,
                onFilterApplied = {
                    coroutineScope.launch {
                        servicios = ServiciosDB.getServiciosAlimenticiosFilter(
                            codigo?.toIntOrNull(),
                            precioInf,
                            precioSup,
                            tipoAlimento
                        )
                    }
                },
                codigo = codigo,
                onCodigoChange = { codigo = it },
                precioInf = precioInf,
                onPrecioInfChange = { precioInf = it },
                precioSup = precioSup,
                onPrecioSupChange = { precioSup = it },
                tipoAlimento = tipoAlimento,
                onTipoAlimentoChange = { tipoAlimento = it }
            )

            // Tabla de servicios
            ServiciosTableAlimenticio(colors, getServiciosTableRowsAlimenticio(servicios))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            FloatingActionButton(
                onClick = { coroutineScope.launch { servicios = ServiciosDB.getServiciosAlimenticiosFilter(null, null, null, null)}}
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
            AddServicioAlimenticioDialog(
                colors = colors,
                onDismissRequest = { showDialog = false },
                onServicioAdded = { newServicioA ->
                    coroutineScope.launch {
                        ServiciosDB.createServicioAlimenticio(newServicioA)
                        servicios = ServiciosDB.getServiciosAlimenticiosFilter(null, null, null, null)
                        showDialog = false
                    }
                }
            )
        }
    }
}

@Composable
fun FilterComponentsAlimenticio(
    colors: RefugioColorPalette,
    onFilterApplied: () -> Unit,
    codigo: String?,
    onCodigoChange: (String?) -> Unit,
    precioInf: Double?,
    onPrecioInfChange: (Double?) -> Unit,
    precioSup: Double?,
    onPrecioSupChange: (Double?) -> Unit,
    tipoAlimento: String?,
    onTipoAlimentoChange: (String?) -> Unit
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
            value = tipoAlimento.orEmpty(),
            onValueChange = { onTipoAlimentoChange(if (it.isEmpty()) null else it) },
            label = { Text("Tipo de Alimento") },
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
fun ServiciosTableAlimenticio(colors: RefugioColorPalette, data: List<ServicioTableRowAlimenticio>) {
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
                    "Tipo de Alimento" to getIconForAttribute("Tipo de Alimento"),
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
            ServiciosRowAlimenticio(colors, row)
            Divider(color = colors.primary, thickness = 1.5.dp)
        }
    }
}

@Composable
fun ServiciosRowAlimenticio(colors: RefugioColorPalette, row: ServicioTableRowAlimenticio) {
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
        Text(text = row.tipoAlim, modifier = Modifier.weight(1f))
        Text(text = "$${row.precioUnit}", modifier = Modifier.weight(1f))
        Row {
            IconButton(onClick = { showUpdateDialog=true }) {
                Icon(Icons.Default.Edit, contentDescription = "Modificar")
            }
            IconButton(onClick = { showDeleteDialog=true }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }

        if (showUpdateDialog) {
            UpdateServicioAlimenticioDialog(
                colors = colors,
                codigo = row.id.toInt(),
                precioInicial = row.precioUnit,
                tipoAlimInicial = row.tipoAlim,
                onDismissRequest = { showUpdateDialog = false },
                onServicioUpdated = { codigo, tipoAlim, precio ->
                    coroutineScope.launch {
                        if(ServiciosDB.updateServicioAlimenticio(codigo, precio, tipoAlim))
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
                title = "Eliminar Servicio Alimenticio",
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
                                    ServiciosDB.deleteServAlim(row.id.toInt())
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
                message = "El servicio alimenticio no puede borrarse porque está vinculado a ${checkPoder?.size} contratos",
                onDismissRequest = { showError = false }
            )
        }
    }
}

fun getServiciosTableRowsAlimenticio(servicios: List<ServAlimenticio>): List<ServicioTableRowAlimenticio> {
    return servicios.map { servicio ->
        ServicioTableRowAlimenticio(
            id = servicio.codigo.toString(),
            precioUnit = servicio.precioUni,
            tipoAlim = servicio.tipoAlimento,
            mainAttributes = mapOf(
                "Código" to "${servicio.codigo}",
                "Tipo de Alimento" to servicio.tipoAlimento,
                "Precio" to "\$${servicio.precioUni}"
            )
        )
    }
}

