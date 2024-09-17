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
    var filtroAdoptante by remember { mutableStateOf<String?>(null) }
    var montoInf by remember { mutableStateOf<Double?>(null) }
    var montoSup by remember { mutableStateOf<Double?>(null) }

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


