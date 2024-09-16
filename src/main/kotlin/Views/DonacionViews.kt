package Views

import Class_DB.DonacionesDB
import UserLogged.UsuarioSingleton
import Models.Donacion
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.window.Dialog

data class Donacion(
    val id: Int,
    val monto: Double,
    val nombreAdopt: String
)

@Composable
fun DonacionesMostrar(colors: RefugioColorPalette, selectedItem: String, selectedSubItem: String) {
    val coroutineScope = rememberCoroutineScope()
    var donaciones by remember { mutableStateOf<List<Donacion>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var filtroAdoptante by remember { mutableStateOf<String?>(null) }
    var montoInf by remember { mutableStateOf<Double?>(null) }
    var montoSup by remember { mutableStateOf<Double?>(null) }

    // Cargar los datos iniciales sin filtros
    LaunchedEffect(Unit) {
        donaciones = DonacionesDB.getDonacionesFilter(null, null, null)
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

            FilterComponentsD(
                colors,
                onFilterApplied = {
                    coroutineScope.launch {
                        // Aplicamos los filtros solo si están presentes
                        donaciones = DonacionesDB.getDonacionesFilter(
                            montoInf,
                            montoSup,
                            filtroAdoptante
                        )
                    }
                },
                filtroAdoptante = filtroAdoptante,
                onFiltroChange = { filtroAdoptante = it },
                montoInf = montoInf,
                onMontoInfChange = { montoInf = it },
                montoSup = montoSup,
                onMontoSupChange = { montoSup = it }
            )

            DonacionesTable(colors, donaciones)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        donaciones = DonacionesDB.getDonacionesFilter(null, null, null)
                    }
                }
            ) {
                Icon(Icons.Default.ArrowCircleDown, contentDescription = "Recargar")
            }

            FloatingActionButton(
                onClick = { showDialog = true },
            ) {
                Icon(Icons.Default.Add, contentDescription = "Donar")
            }
        }

        if (showDialog) {
            AddDonacionDialog(
                colors = colors,
                onDismissRequest = { showDialog = false },
                onDonacionAdded = { monto ->
                    coroutineScope.launch {
                        DonacionesDB.createDonacion(monto, UsuarioSingleton.id ?: 0)
                        donaciones = DonacionesDB.getDonacionesFilter(null , null, null)
                        showDialog = false
                    }
                }
            )
        }
    }
}

@Composable
fun FilterComponentsD(
    colors: RefugioColorPalette,
    onFilterApplied: () -> Unit,
    filtroAdoptante: String?,
    onFiltroChange: (String?) -> Unit,
    montoInf: Double?,
    onMontoInfChange: (Double?) -> Unit,
    montoSup: Double?,
    onMontoSupChange: (Double?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = filtroAdoptante.orEmpty(),
            onValueChange = { onFiltroChange(if (it.isEmpty()) null else it) },
            label = { Text("Nombre Adoptante") },
            modifier = Modifier.weight(1f)
        )

        Spinner(
            value = montoInf ?: 0.0,
            onValueChange = { onMontoInfChange(it) },
            label = { Text("Monto Inferior") },
            modifier = Modifier.width(160.dp),
            step = 0.5
        )

        Spinner(
            value = montoSup ?: 0.0,
            onValueChange = { onMontoSupChange(it) },
            label = { Text("Monto Superior") },
            modifier = Modifier.width(160.dp),
            step = 0.5
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
fun DonacionesTable(colors: RefugioColorPalette, donaciones: List<Donacion>) {
    LazyColumn {
        // Encabezados de la tabla con íconos
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val headersWithIcons = listOf(
                    "Código" to getIconForAttribute("Código"),
                    "Monto" to getIconForAttribute("Precio"),
                    "Nombre Adoptante" to getIconForAttribute("Nombre del Adoptante")
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

        // Filas con datos
        items(donaciones) { donacion ->
            DonacionRow(donacion, colors)
            Divider(color = colors.primary, thickness = 1.5.dp)
        }
    }
}

@Composable
fun DonacionRow(donacion: Donacion, colors: RefugioColorPalette) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = donacion.id.toString(), modifier = Modifier.weight(1f))
        Text(text = "$${donacion.monto}", modifier = Modifier.weight(1f))
        Text(text = donacion.nombreAdopt, modifier = Modifier.weight(1f))
    }
}

@Composable
fun AddDonacionDialog(
    colors: RefugioColorPalette,
    onDismissRequest: () -> Unit,
    onDonacionAdded: (Double) -> Unit
) {
    var monto by remember { mutableStateOf(0.0) }
    var cantidadInput by remember { mutableStateOf(TextFieldValue("0")) }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("¿Cuánto desea donar?", style = MaterialTheme.typography.h5)

                Spacer(modifier = Modifier.height(16.dp))

                NumberPickerComponent(
                    value = cantidadInput,
                    onValueChange = { value ->
                        cantidadInput = value
                        monto = value.text.toDoubleOrNull() ?: 0.0
                    }
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
                        if (monto > 0.0) {
                            onDonacionAdded(monto)
                        } else {
                            println("ERROR: El monto debe ser mayor a 0")
                        }
                    }) {
                        Text("Donar")
                    }
                }
            }
        }
    }
}

@Composable
fun NumberPickerComponent(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        IconButton(onClick = {
            val newValue = (value.text.toIntOrNull() ?: 0) - 100
            onValueChange(TextFieldValue(newValue.toString()))
        }) {
            Icon(Icons.Default.ArrowCircleDown, contentDescription = "Restar 100")
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = {
            val newValue = (value.text.toIntOrNull() ?: 0) + 100
            onValueChange(TextFieldValue(newValue.toString()))
        }) {
            Icon(Icons.Default.Add, contentDescription = "Sumar 100")
        }
    }
}
