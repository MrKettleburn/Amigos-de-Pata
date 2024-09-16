package Views

import UserLogged.Usuario
import UserLogged.UsuarioDB
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.window.Dialog

@Composable
fun UsuariosMostrar(colors: RefugioColorPalette, selectedItem: String, selectedSubItem: String) {
    val coroutineScope = rememberCoroutineScope()
    var usuarios by remember { mutableStateOf<List<Usuario>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var permiso by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }

    // Cargar los datos iniciales
    LaunchedEffect(Unit) {
        usuarios = UsuarioDB.getUsuariosFilter(null) // Cargar usuarios filtrados por permiso
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

            FilterComponentsU(
                colors,
                onFilterApplied = {
                    coroutineScope.launch {
                        usuarios = UsuarioDB.getUsuariosFilter(permiso)
                    }
                },
                permiso = permiso,
                onPermisoChange = { permiso = it }
            )

            UsuariosExpandableTable(colors, getUsuariosTableRows(usuarios))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            FloatingActionButton(
                onClick = { coroutineScope.launch { usuarios = UsuarioDB.getUsuariosFilter(null) } }
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
            AddUsuarioDialog(
                colors = colors,
                onDismissRequest = { showDialog = false },
                onUsuarioAdded = { usuario, contrasenia, permiso ->
                    coroutineScope.launch {
                        // Ahora pasa los parámetros individuales a createUsuario
                        UsuarioDB.createUsuario(usuario, contrasenia, permiso)
                        usuarios = UsuarioDB.getUsuariosFilter(null)
                        showDialog = false
                    }
                }
            )
        }
    }
}

@Composable
fun FilterComponentsU(
    colors: RefugioColorPalette,
    onFilterApplied: () -> Unit,
    permiso: String?,
    onPermisoChange: (String?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = permiso.orEmpty(),
            onValueChange = { onPermisoChange(if (it.isEmpty()) null else it) },
            label = { Text("Permiso") },
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
fun UsuariosExpandableTable(colors: RefugioColorPalette, data: List<UsuarioTableRow>) {
    LazyColumn {
        // Encabezados de la tabla con iconos
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val headersWithIcons = listOf(
                    "Código" to getIconForAttribute("Código"),
                    "Usuario" to getIconForAttribute("Usuario"),
                    "Permiso" to getIconForAttribute("Permiso")
                )

                headersWithIcons.forEach { (header, icon) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = header, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.width(56.dp))
            }
            Divider(color = colors.primary, thickness = 1.5.dp)
        }

        items(data) { row ->
            UsuarioExpandableRow(colors, row)
            Divider(color = colors.primary, thickness = 1.5.dp)
        }
    }
}

@Composable
fun UsuarioExpandableRow(colors: RefugioColorPalette, row: UsuarioTableRow) {
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
            listOf(row.codigo, row.usuario, row.permiso).forEach { value ->
                Text(text = value, modifier = Modifier.weight(1f))
            }

            Row {
                IconButton(onClick = { showUpdateDialog = true }) {
                    Icon(Icons.Default.Edit, contentDescription = "Modificar")
                }
                IconButton(onClick = { /* TODO: Implementar eliminar */ }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, contentDescription = if (expanded) "Colapsar" else "Expandir")
                }
            }
        }

        if (expanded) {
            row.expandedAttributes.forEach { (key, value) ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 16.dp, top = 8.dp)) {
                    Icon(imageVector = getIconForAttribute(key), contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "$key: ", fontWeight = FontWeight.Bold)
                    Text(text = value)
                }
            }
        }

        if (showUpdateDialog) {
            UpdateUsuarioDialog(
                colors = colors,
                codigo = row.codigo.toInt(),
                usuarioInicial = row.usuario,
                contraseniaInicial = row.contrasenia,
                permisoInicial = row.permiso,
                onDismissRequest = { showUpdateDialog = false },
                onUsuarioUpdated = { codigo, usuario, contrasenia, permiso ->
                    coroutineScope.launch {
                        if (UsuarioDB.updateUsuario(codigo, usuario, contrasenia, permiso))
                            showUpdateDialog = false
                        else
                            println("Revise los datos")
                    }
                }
            )
        }
    }
}

@Composable
fun AddUsuarioDialog(
    colors: RefugioColorPalette,
    onDismissRequest: () -> Unit,
    onUsuarioAdded: (String, String, String) -> Unit
) {
    var usuario by remember { mutableStateOf("") }
    var contrasenia by remember { mutableStateOf("") }
    var permiso by remember { mutableStateOf("Gestion") }
    var expanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Agregar Usuario", style = MaterialTheme.typography.h6)

                OutlinedTextField(
                    value = usuario,
                    onValueChange = { usuario = it },
                    label = { Text("Usuario") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = contrasenia,
                    onValueChange = { contrasenia = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Permiso")
                Box {
                    OutlinedButton(onClick = { expanded = true }) {
                        Text(permiso)
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            permiso = "Gestion"
                            expanded = false
                        }) {
                            Text("Gestion")
                        }
                        DropdownMenuItem(onClick = {
                            permiso = "Cuidador"
                            expanded = false
                        }) {
                            Text("Cuidador")
                        }
                    }
                }

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
                        if (usuario.isNotBlank() && contrasenia.isNotBlank()) {
                            onUsuarioAdded(usuario, contrasenia, permiso)
                        } else {
                            println("ERROR: Los campos no pueden estar vacíos")
                        }
                    }) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}

fun getUsuariosTableRows(usuarios: List<Usuario>): List<UsuarioTableRow> {
    return usuarios.map { usuario ->
        UsuarioTableRow(
            codigo = usuario.codigo.toString(),
            usuario = usuario.usuario,
            contrasenia = usuario.contrasenia,
            permiso = usuario.permiso,
            expandedAttributes = mapOf(
                "Contraseña" to usuario.contrasenia
            )
        )
    }
}

data class UsuarioTableRow(
    val codigo: String,
    val usuario: String,
    val contrasenia: String,
    val permiso: String,
    val expandedAttributes: Map<String, String>
)
@Composable
fun UpdateUsuarioDialog(
    colors: RefugioColorPalette,
    codigo: Int,
    usuarioInicial: String,
    contraseniaInicial: String,
    permisoInicial: String,
    onDismissRequest: () -> Unit,
    onUsuarioUpdated: (Int, String, String, String) -> Unit
) {
    var usuario by remember { mutableStateOf(usuarioInicial) }
    var contrasenia by remember { mutableStateOf(contraseniaInicial) }
    var permiso by remember { mutableStateOf(permisoInicial) }
    var expanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Modificar Usuario", style = MaterialTheme.typography.h6)

                OutlinedTextField(
                    value = usuario,
                    onValueChange = { usuario = it },
                    label = { Text("Usuario") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = contrasenia,
                    onValueChange = { contrasenia = it },
                    label = { Text("Nueva Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Permiso")
                Box {
                    OutlinedButton(onClick = { expanded = true }) {
                        Text(permiso)
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            permiso = "Gestion"
                            expanded = false
                        }) {
                            Text("Gestion")
                        }
                        DropdownMenuItem(onClick = {
                            permiso = "Cuidador"
                            expanded = false
                        }) {
                            Text("Cuidador")
                        }
                    }
                }

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
                        if (usuario.isNotBlank()) {
                            onUsuarioUpdated(
                                codigo,
                                usuario,
                                contrasenia,
                                permiso
                            )
                        } else {
                            println("ERROR: El campo de usuario no puede estar vacío")
                        }
                    }) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}

