package Views

import Class_DB.ContratadosDB
import Class_DB.ServiciosDB
import Models.Transporte
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
fun TransportistasMostrar(colors: RefugioColorPalette, selectedItem: String, selectedSubItem: String) {
    val coroutineScope = rememberCoroutineScope()
    var transportistas by remember { mutableStateOf<List<Transporte>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }

    // Estados para los filtros
    var codigo by remember { mutableStateOf<String?>(null) }
    var nombre by remember { mutableStateOf<String?>(null) }
    var provincia by remember { mutableStateOf<String?>(null) }

    // Cargar los datos iniciales
    LaunchedEffect(Unit) {
        transportistas = ContratadosDB.getTransportistasFilter(null, null, null)
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

            FilterComponentsT(
                colors,
                onFilterApplied = {
                    coroutineScope.launch {
                        transportistas = ContratadosDB.getTransportistasFilter(
                            codigo?.toIntOrNull(),
                            nombre,
                            provincia
                        )
                    }
                },
                codigo = codigo,
                onCodigoChange = { codigo = it },
                nombre = nombre,
                onNombreChange = { nombre = it },
                provincia = provincia,
                onProvinciaChange = { provincia = it }
            )

            TransportistasExpandableTable(colors, getTransportistasTableRows(transportistas))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            FloatingActionButton(
                onClick = { coroutineScope.launch { transportistas = ContratadosDB.getTransportistasFilter(null, null, null)}}
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
            AddTransportistaDialog(
                colors = colors,
                onDismissRequest = { showDialog = false },
                onTransportistaAdded = { newTransportista ->
                    coroutineScope.launch {
                        ContratadosDB.createTransportista(newTransportista)
                        transportistas = ContratadosDB.getTransportistasFilter(null, null, null)
                        showDialog = false
                    }
                }
            )
        }
    }
}
@Composable
fun FilterComponentsT(
    colors: RefugioColorPalette,
    onFilterApplied: () -> Unit,
    codigo: String?,
    onCodigoChange: (String?) -> Unit,
    nombre: String?,
    onNombreChange: (String?) -> Unit,
    provincia: String?,
    onProvinciaChange: (String?) -> Unit
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
        Button(
            onClick = onFilterApplied,
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text("Filtrar")
        }
    }
}
@Composable
fun TransportistasExpandableTable(colors: RefugioColorPalette, data: List<TransportistaTableRow>) {
    LazyColumn {
        // Agregar encabezados de la tabla con íconos
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Lista de atributos principales y sus íconos correspondientes
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
                Spacer(modifier = Modifier.width(56.dp)) // Espacio para íconos de acciones (Modificar, Eliminar, Expandir)
            }
            Divider(color = colors.primary, thickness = 1.5.dp)
        }

        // Iterar sobre las filas de datos
        items(data) { row ->
            TransportistaExpandableRow(colors, row)
            Divider(color = colors.primary, thickness = 1.5.dp)
        }
    }
}

@Composable
fun TransportistaExpandableRow(colors: RefugioColorPalette, row: TransportistaTableRow) {
    var expanded by remember { mutableStateOf(false) }
    val backgroundColor = if (expanded) colors.menuBackground else Color.Transparent
    val coroutineScope = rememberCoroutineScope()
    var showUpdateDialog by remember { mutableStateOf(false) }

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
            // Mostrar valores de los atributos principales en la fila sin los subtítulos
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

        // Atributos expandidos solo si está expandido
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
            UpdateTransporteDialog(
                colors = colors,
                codigo = row.id.toInt(),
                nombreInicial = row.nombre,
                emailInicial = row.email,
                provinciaInicial = row.provincia,
                direccionInicial = row.direccion,
                telefonoInicial = row.telefono,
                onDismissRequest = { showUpdateDialog = false },
                onTransporteUpdated = { codigo, nombre, email, provincia, direccion, telefono ->
                    coroutineScope.launch {
                        if (ContratadosDB.updateTransporte(codigo, nombre, email, provincia, direccion, telefono))
                            showUpdateDialog = false
                        else
                            println("Revise los datos")
                    }
                }
            )
        }
    }
}

fun getTransportistasTableRows(transportistas: List<Transporte>): List<TransportistaTableRow> {
    return transportistas.map { transportista ->
        TransportistaTableRow(
            id = transportista.codigo.toString(),
            nombre = transportista.nombre,
            email = transportista.email,
            provincia = transportista.provincia,
            direccion = transportista.direccion,
            telefono = transportista.telefono,
            mainAttributes = mapOf(
                "Código" to "${transportista.codigo}",
                "Nombre" to transportista.nombre,
                "Provincia" to transportista.provincia
            ),
            expandedAttributes = mapOf(
                "Email" to transportista.email,
                "Dirección" to transportista.direccion,
                "Teléfono" to transportista.telefono
            )
        )
    }
}
