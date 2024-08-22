package Views

import Class_DB.ServiciosDB
import Models.ServVeterinario
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
import java.util.*

// Esta es la función completa con los ajustes:
@Composable
fun ServiciosVeterinariosMostrar(colors: RefugioColorPalette, selectedItem: String, selectedSubItem: String) {
    val coroutineScope = rememberCoroutineScope()
    var servicios by remember { mutableStateOf<List<ServVeterinario>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }

    // Estados para los filtros
    var codigo by remember { mutableStateOf<String?>(null) }
    var precioInf by remember { mutableStateOf<String?>(null) }
    var precioSup by remember { mutableStateOf<String?>(null) }
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
                            precioInf?.toDoubleOrNull(),
                            precioSup?.toDoubleOrNull(),
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

            // Tabla expandible
            ServiciosTable(colors, getServiciosTableRows(servicios))
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
            AddServicioDialog(
                colors = colors,
                onDismissRequest = { showDialog = false },
                onServicioAdded = { newServicioV -> // Corregido a `newServicio`
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
    precioInf: String?,
    onPrecioInfChange: (String?) -> Unit,
    precioSup: String?,
    onPrecioSupChange: (String?) -> Unit,
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
        OutlinedTextField(
            value = precioInf.orEmpty(),
            onValueChange = { onPrecioInfChange(if (it.isEmpty()) null else it) },
            label = { Text("Precio Inferior") },
            modifier = Modifier.width(160.dp)
        )
        OutlinedTextField(
            value = precioSup.orEmpty(),
            onValueChange = { onPrecioSupChange(if (it.isEmpty()) null else it) },
            label = { Text("Precio Superior") },
            modifier = Modifier.width(160.dp)
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
fun ServiciosTable(colors: RefugioColorPalette, data: List<ServicioTableRow>) {
    LazyColumn {
        items(data) { row ->
            ServiciosRow(colors, row)
            Divider(color = colors.primary, thickness = 1.5.dp)
        }
    }
}


@Composable
fun ServiciosRow(colors: RefugioColorPalette, row: ServicioTableRow) {
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
                    imageVector = getIconForAttributeSV(key),
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
    }
}

// Función para asignar íconos a cada atributo
fun getIconForAttributeSV(attribute: String): ImageVector {
    return when (attribute) {
        "Código" -> Icons.Default.Badge
        "Modalidad" -> Icons.Default.Work
        "Precio" -> Icons.Default.AttachMoney
        else -> Icons.Default.Info
    }
}

data class ServicioTableRow(
    val id: String,
    val mainAttributes: Map<String, String>
)

fun getServiciosTableRows(servicios: List<ServVeterinario>): List<ServicioTableRow> {
    return servicios.map { servicio ->
        ServicioTableRow(
            id = servicio.codigo.toString(),
            mainAttributes = mapOf(
                "Código" to "${servicio.codigo}",
                "Modalidad" to servicio.modalidad,
                "Precio" to "\$${servicio.precioUni}"
            )
        )
    }
}


@Composable
fun AddServicioDialog(
    colors: RefugioColorPalette,
    onDismissRequest: () -> Unit,
    onServicioAdded: (ServVeterinario) -> Unit
) {
    var precio by remember { mutableStateOf<Double?>(null) }
    var modalidad by remember { mutableStateOf<String>("") }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Agregar Servicio Veterinario", style = MaterialTheme.typography.h6)

                OutlinedTextField(
                    value = modalidad,
                    onValueChange = { modalidad = it },
                    label = { Text("Modalidad") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = precio?.toString() ?: "",
                    onValueChange = { precio = it.toDoubleOrNull() },
                    label = { Text("Precio") },
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
                        if (modalidad.isNotBlank() && precio != null && precio != 0.0) {
                            onServicioAdded(
                                ServVeterinario(
                                    codigo = 0,
                                    modalidad = modalidad,
                                    precioUni = precio!!
                                )
                            )
                        }
                        else{
                            //CARTEL DE VALIDACION
                            print("No esa talla")
                        }
                    }) {
                        Text("Agregar")
                    }
                }
            }
        }
    }
}