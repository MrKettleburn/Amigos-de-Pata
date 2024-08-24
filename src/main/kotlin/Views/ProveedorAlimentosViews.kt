package Views

import Class_DB.ContratadosDB
import Class_DB.ContratoDB
import Models.ProveedorDeAlimentos
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
fun ProveedoresDeAlimentosMostrar(colors: RefugioColorPalette, selectedItem: String, selectedSubItem: String) {
    val coroutineScope = rememberCoroutineScope()
    var proveedores by remember { mutableStateOf<List<ProveedorDeAlimentos>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }

    // Estados para los filtros
    var codigo by remember { mutableStateOf<String?>(null) }
    var nombre by remember { mutableStateOf<String?>(null) }
    var provincia by remember { mutableStateOf<String?>(null) }

    // Cargar los datos iniciales
    LaunchedEffect(Unit) {
        proveedores = ContratadosDB.getProveedoresAlimentosFilter(null, null, null)
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
                    proveedores = ContratadosDB.getProveedoresAlimentosFilter(null,null,null)
                } },
            ) {
                Text("Recargar")
            }

            FilterComponentsPA(
                colors,
                onFilterApplied = {
                    coroutineScope.launch {
                        proveedores = ContratadosDB.getProveedoresAlimentosFilter(
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

            ProveedoresExpandableTable(colors, getProveedoresTableRows(proveedores))
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
            AddProveedorDialog(
                colors = colors,
                onDismissRequest = { showDialog = false },
                onProveedorAdded = { newProveedor ->
                    coroutineScope.launch {
                        ContratadosDB.createProveedorAlimentos(newProveedor)
                        proveedores = ContratadosDB.getProveedoresAlimentosFilter(null, null, null)
                        showDialog = false
                    }
                }
            )
        }
    }
}

@Composable
fun FilterComponentsPA(
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
fun ProveedoresExpandableTable(colors: RefugioColorPalette, data: List<ProveedorTableRow>) {
    LazyColumn {
        items(data) { row ->
            ProveedorExpandableRow(colors, row)
            Divider(color = colors.primary, thickness = 1.5.dp)
        }
    }
}

@Composable
fun ProveedorExpandableRow(colors: RefugioColorPalette, row: ProveedorTableRow) {
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


fun getProveedoresTableRows(proveedores: List<ProveedorDeAlimentos>): List<ProveedorTableRow> {
    return proveedores.map { proveedor ->
        ProveedorTableRow(
            id = proveedor.codigo.toString(),
            nombre = proveedor.nombre,
            email = proveedor.email,
            provincia = proveedor.provincia,
            direccion = proveedor.direccion,
            telefono = proveedor.telefono,
            mainAttributes = mapOf(
                "Código" to "${proveedor.codigo}",
                "Nombre" to proveedor.nombre,
                "Provincia" to proveedor.provincia
            ),
            expandedAttributes = mapOf(
                "Email" to proveedor.email,
                "Dirección" to proveedor.direccion,
                "Teléfono" to proveedor.telefono
            )
        )
    }
}

