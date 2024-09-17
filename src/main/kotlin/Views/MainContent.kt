package Views

import ReportesPDF.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.LocalDateTime

@Composable
fun MainContent(colors: RefugioColorPalette, onLogout: () -> Unit) {
    var selectedItem by remember { mutableStateOf("") }
    var selectedSubItem by remember { mutableStateOf("") }
    var showClinicaProvinciaDialog by remember { mutableStateOf(false) }
    var clinica by remember { mutableStateOf<String?>(null) }
    var provincia by remember { mutableStateOf<String?>(null) }
    var generarReporte by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Row(modifier = Modifier.fillMaxSize()) {
        // Sidebar con el menú expandible
        AnimatedSideMenu(colors, selectedItem, selectedSubItem, onSelectionChanged = { item, subItem ->
            selectedItem = item
            selectedSubItem = subItem
        }, onLogout = onLogout)

        // Main Content del Dashboard
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(0.dp)
        ) {
            //if (selectedItem.isEmpty() || (selectedItem in listOf("Contratos", "Contratados", "Servicios") && selectedSubItem.isEmpty())) {
                // Contenido predeterminado para ambientar cuando no hay selección
                DefaultContent(colors)
//            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp)
                ) {
                    when (selectedItem) {
                        "Contratos" -> {
                            when (selectedSubItem) {
                                "Veterinarios" -> ContratosVeterinariosMostrar(colors, selectedItem, selectedSubItem)
                                "Transporte" -> ContratosTransporteMostrar(colors, selectedItem, selectedSubItem)
                                "Proveedor de alimentos" -> ContratosProvAlimentosMostrar(colors, selectedItem, selectedSubItem)
                            }
                        }

                        "Contratados" -> {
                            when (selectedSubItem) {
                                "Veterinarios" -> VeterinariosMostrar(colors, selectedItem, selectedSubItem)
                                "Transporte" -> TransportistasMostrar(colors, selectedItem, selectedSubItem)
                                "Proveedor de alimentos" -> ProveedoresDeAlimentosMostrar(colors, selectedItem, selectedSubItem)
                            }
                        }

                        "Servicios" -> {
                            when (selectedSubItem) {
                                "Veterinarios" ->ServiciosVeterinariosMostrar(colors, selectedItem, selectedSubItem)
                                "Transporte" -> ServiciosTransporteMostrar(colors, selectedItem, selectedSubItem)
                                "Proveedor de alimentos" -> ServiciosAlimenticioMostrar(colors, selectedItem, selectedSubItem)
                            }
                        }

                        "Animales" -> {
                            when (selectedSubItem){
                                "En Refugio" -> AnimalesEnRefugioMostrar(colors, selectedItem, selectedSubItem)
                                "En Adopción" -> AnimalesAdoptadosMostrar(colors, selectedItem, selectedSubItem)
                            }
                        }
                        "Usuarios" -> UsuariosMostrar(colors, selectedItem, selectedSubItem)

                        "Donaciones" -> DonacionesMostrar(colors, selectedItem, selectedSubItem)

                        "Informes" -> {
                            when(selectedSubItem){
                                "Listado de Contratos Conciliados con Veterinarios" -> generarReporteContratosVeterinarios(coroutineScope,LocalDateTime.now())
                                "Listado de Contratos de Proveedores de Alimentos" -> generarReporteContratosProveedoresAlim(coroutineScope,LocalDateTime.now())
                                "Listado de Contratos de Servicios Complementarios" -> generarReporteContratosTransporte(coroutineScope,LocalDateTime.now())
                                "Listado de Veterinarios Activos" -> showClinicaProvinciaDialog = true
                                "Plan de Ingresos por Concepto de Adopciones y Donaciones" -> generarReportePlanDeIngresos(coroutineScope,LocalDateTime.now())
                            }
                        }
                    }

                    if (showClinicaProvinciaDialog) {
                        ClinicaYProvinciaReporteDialog(
                            colors = colors,
                            onDismissRequest = { selectedSubItem=""
                                showClinicaProvinciaDialog = false },
                            onReporteHecho = { clinic, prov ->
                                clinica = clinic
                                provincia = prov
                                generarReporte = true
                                selectedSubItem=""
                                showClinicaProvinciaDialog = false
                            }
                        )
                    }
                    if (generarReporte) {
                        generarReporteVeterinariosActivos(coroutineScope, clinica, provincia, LocalDateTime.now())
                        generarReporte = false
                    }
                }
            }
        }
    }

@Composable
fun ClinicaYProvinciaReporteDialog(
    colors: RefugioColorPalette,
    onDismissRequest: () -> Unit,
    onReporteHecho: (String?, String?) -> Unit // Permitir nulos
){
    var clinica by remember { mutableStateOf("") }
    var provincia by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Reporte Veterinarios Activos", style = MaterialTheme.typography.h6)

                OutlinedTextField(
                    value = clinica,
                    onValueChange = { clinica = it },
                    label = { Text("Clínica") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = provincia,
                    onValueChange = { provincia = it },
                    label = { Text("Provincia") },
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
                        // Si están vacíos, se convierten en nulos
                        val clinicTrimmed = clinica.trim().ifEmpty { null }
                        val provinciaTrimmed = provincia.trim().ifEmpty { null }
                        onReporteHecho(clinicTrimmed, provinciaTrimmed)
                    }) {
                        Text("Generar Reporte")
                    }
                }
            }
        }
    }
}

@Composable
    fun DefaultContent(colors: RefugioColorPalette) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Cargar la imagen desde los recursos
            Image(
                painter = painterResource("FondoAmigosDePata.jpg"),
                contentDescription = "Fondo de refugio de animales",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Contenido encima de la imagen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "",
                    style = MaterialTheme.typography.h4,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }