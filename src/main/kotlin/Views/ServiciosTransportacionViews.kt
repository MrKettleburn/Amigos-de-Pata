package Views

import Class_DB.ServiciosDB
import Models.ServTransporte
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
fun ServiciosTransporteMostrar(colors: RefugioColorPalette, selectedItem: String, selectedSubItem: String) {
    val coroutineScope = rememberCoroutineScope()
    var servicios by remember { mutableStateOf<List<ServTransporte>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }

    // Estados para los filtros
    var codigo by remember { mutableStateOf<String?>(null) }
    var precioInf by remember { mutableStateOf<Double?>(null) }
    var precioSup by remember { mutableStateOf<Double?>(null) }
    var vehiculo by remember { mutableStateOf<String?>(null) }

    // Cargar los datos iniciales
    LaunchedEffect(Unit) {
        servicios = ServiciosDB.getServiciosTransportacionFilter(null, null, null, null)
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

            Button(
                onClick = { coroutineScope.launch {
                    servicios = ServiciosDB.getServiciosTransportacionFilter(null,null,null,null)
                } },
            ) {
                Text("Recargar")
            }
            // Componentes de filtrado
            FilterComponentsTransporte(
                colors,
                onFilterApplied = {
                    coroutineScope.launch {
                        servicios = ServiciosDB.getServiciosTransportacionFilter(
                            codigo?.toIntOrNull(),
                            precioInf,
                            precioSup,
                            vehiculo
                        )
                    }
                },
                codigo = codigo,
                onCodigoChange = { codigo = it },
                precioInf = precioInf,
                onPrecioInfChange = { precioInf = it },
                precioSup = precioSup,
                onPrecioSupChange = { precioSup = it },
                vehiculo = vehiculo,
                onVehiculoChange = { vehiculo = it }
            )

            // Tabla de servicios
            ServiciosTableTransporte(colors, getServiciosTableRowsTransporte(servicios))
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
            AddServicioTransporteDialog(
                colors = colors,
                onDismissRequest = { showDialog = false },
                onServicioAdded = { newServicioT ->
                    coroutineScope.launch {
                        ServiciosDB.createServicioTransporte(newServicioT)
                        servicios = ServiciosDB.getServiciosTransportacionFilter(null, null, null, null)
                        showDialog = false
                    }
                }
            )
        }
    }
}

@Composable
fun FilterComponentsTransporte(
    colors: RefugioColorPalette,
    onFilterApplied: () -> Unit,
    codigo: String?,
    onCodigoChange: (String?) -> Unit,
    precioInf: Double?,
    onPrecioInfChange: (Double?) -> Unit,
    precioSup: Double?,
    onPrecioSupChange: (Double?) -> Unit,
    vehiculo: String?,
    onVehiculoChange: (String?) -> Unit
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
            value = vehiculo.orEmpty(),
            onValueChange = { onVehiculoChange(if (it.isEmpty()) null else it) },
            label = { Text("Vehículo") },
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
fun ServiciosTableTransporte(colors: RefugioColorPalette, data: List<ServicioTableRowTransporte>) {
    LazyColumn {
        items(data) { row ->
            ServiciosRowTransporte(colors, row)
            Divider(color = colors.primary, thickness = 1.5.dp)
        }
    }
}

@Composable
fun ServiciosRowTransporte(colors: RefugioColorPalette, row: ServicioTableRowTransporte) {
    var showUpdateDialog by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
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
        }

        if (showUpdateDialog) {
            UpdateServicioTransporteDialog(
                colors = colors,
                codigo = row.id.toInt(),
                precioInicial = row.precioUnit,
                vehiculoInicial = row.vehiculo,
                onDismissRequest = { showUpdateDialog = false },
                onServicioUpdated = { codigo, vehiculo, precio ->

                    //ServiciosDB.updateServicioTransporte(codigo, vehiculo, precio)

                    showUpdateDialog = false
                }
            )
        }
    }
}

fun getServiciosTableRowsTransporte(servicios: List<ServTransporte>): List<ServicioTableRowTransporte> {
    return servicios.map { servicio ->
        ServicioTableRowTransporte(
            id = servicio.codigo.toString(),
            precioUnit = servicio.precioUni,
            vehiculo = servicio.vehiculo,
            mainAttributes = mapOf(
                "Código" to "${servicio.codigo}",
                "Vehiculo" to servicio.vehiculo,
                "Precio" to "\$${servicio.precioUni}"
            )
        )
    }
}


