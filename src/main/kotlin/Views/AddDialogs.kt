package Views

import Class_DB.ActividadDB
import Class_DB.ContratadosDB
import Class_DB.ContratoDB
import Class_DB.ServiciosDB
import Models.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


//-----------------------------ANIMAL-----------------------------
@Composable
fun AddAnimalDialog(
    colors: RefugioColorPalette,
    onDismissRequest: () -> Unit,
    onAnimalAdded: (Animal) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf<Int?>(null) }
    var peso by remember { mutableStateOf<Double?>(null) }
    var fechaIngreso by remember { mutableStateOf<LocalDate?>(null) }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Agregar Animal", style = MaterialTheme.typography.h6)

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = especie,
                    onValueChange = { especie = it },
                    label = { Text("Especie") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = raza,
                    onValueChange = { raza = it },
                    label = { Text("Raza") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(modifier = Modifier.padding(0.dp))
                {
                    OutlinedTextField(
                        value = edad?.toString() ?: "",
                        onValueChange = { edad = it.toIntOrNull() },
                        label = { Text("Edad") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    OutlinedTextField(
                        value = peso?.toString() ?: "",
                        onValueChange = { peso = it.toDoubleOrNull() },
                        label = { Text("Peso (kg)") },
                        modifier = Modifier.weight(1f)
                    )
                }
                DatePicker(
                    label = { Text("Fecha de Ingreso") },
                    selectedDate = fechaIngreso,
                    onDateChange = { fechaIngreso = it },
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
                        if (nombre.isNotBlank() && especie.isNotBlank() && raza.isNotBlank() && edad != null && peso != null && fechaIngreso != null) {
                            onAnimalAdded(
                                Animal(
                                    codigo = 0,
                                    nombre = nombre,
                                    especie = especie,
                                    raza = raza,
                                    edad = edad!!,
                                    peso = peso!!,
                                    cantDias = 0,
                                    fecha_ingreso = fechaIngreso!!
                                )
                            )
                        }
                    }) {
                        Text("Agregar")
                    }
                }
            }
        }
    }
}


///////////////----------------------CONTRATO VETERINARIO------------------///////////////////

@Composable
fun AddContratoVeterinarioDialog(
    colors: RefugioColorPalette,
    onDismissRequest: () -> Unit,
    onContratoAdded: (ContratoVeterinario) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    // Estados para los campos
    var selectedVet by remember { mutableStateOf<Veterinario?>(null) }
    var selectedServ by remember { mutableStateOf<ServVeterinario?>(null) }
    var descripcion by remember { mutableStateOf("") }
    var fechaInicio by remember { mutableStateOf<LocalDate?>(null) }
    var fechaFin by remember { mutableStateOf<LocalDate?>(null) }
    var fechaConcil by remember { mutableStateOf<LocalDate?>(null) }

    // Cargar datos de veterinarios y servicios
    val veterinarios = remember { mutableStateOf<List<Veterinario>>(emptyList()) }
    val servicios = remember { mutableStateOf<List<ServVeterinario>>(emptyList()) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            veterinarios.value = ContratadosDB.getVeterinariosForComboBox()
            servicios.value = ServiciosDB.getServiciosVeterinariosForComboBox()
        }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp).width(800.dp),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Agregar Contrato Veterinario", style = MaterialTheme.typography.h6)

                // ComboBox para seleccionar un veterinario
                Row(modifier = Modifier.padding(0.dp)) {
                    DropdownMenu(
                        label = { Text("Veterinario") },
                        items = veterinarios.value,
                        selectedItem = selectedVet,
                        onItemSelected = { selectedVet = it },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    // ComboBox para seleccionar un servicio veterinario
                    DropdownMenu(
                        label = { Text("Servicio Veterinario") },
                        items = servicios.value,
                        selectedItem = selectedServ,
                        onItemSelected = { selectedServ = it },
                        modifier = Modifier.weight(1f)
                    )
                }
                // Campo para la descripción
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción del Contrato") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Campos DatePicker para las fechas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DatePicker(
                        label = { Text("Fecha de Inicio") },
                        selectedDate = fechaInicio,
                        onDateChange = { fechaInicio = it },
                        modifier = Modifier.weight(1f)
                    )
                    DatePicker(
                        label = { Text("Fecha de Fin") },
                        selectedDate = fechaFin,
                        onDateChange = { fechaFin = it },
                        modifier = Modifier.weight(1f)
                    )
                    DatePicker(
                        label = { Text("Fecha de Conciliación") },
                        selectedDate = fechaConcil,
                        onDateChange = { fechaConcil = it },
                        modifier = Modifier.weight(1f)
                    )
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
                        if (selectedVet != null && selectedServ != null && descripcion.isNotBlank() && fechaInicio != null && fechaFin != null && fechaConcil != null) {
                            val newContrato = ContratoVeterinario(
                                codigo = 0,
                                precio = ChronoUnit.DAYS.between(fechaInicio,fechaFin).toInt() * selectedServ!!.precioUni + selectedServ!!.precioUni,
                                descripcion = descripcion,
                                nombreVet = selectedVet!!.nombre,
                                clinicaVet = selectedVet!!.clinica,
                                provinciaVet = selectedVet!!.provincia,
                                direccVet = selectedVet!!.direccion,
                                especialidad = selectedVet!!.especialidad,
                                modalidadServVet = selectedServ!!.modalidad,
                                precioUnit = selectedServ!!.precioUni,
                                fechaInicio = fechaInicio!!,
                                fechaFin = fechaFin!!,
                                fechaConcil = fechaConcil!!,
                                idVet = selectedVet!!.codigo,
                                idServ = selectedServ!!.codigo
                            )
                            onContratoAdded(newContrato)
                        }
                    }) {
                        Text("Agregar")
                    }
                }
            }
        }
    }
}

/////////////////////------------------------CONTRATO TRANSPORTE-------------------/////////////////////////

@Composable
fun AddContratoTransporteDialog(
    colors: RefugioColorPalette,
    onDismissRequest: () -> Unit,
    onContratoAdded: (ContratoTransporte) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    // Estados para los campos
    var selectedTrans by remember { mutableStateOf<Transporte?>(null) }
    var selectedServ by remember { mutableStateOf<ServTransporte?>(null) }
    var kilometros by remember { mutableStateOf<Double?>(null) }
    var descripcion by remember { mutableStateOf("") }
    var recargo by remember { mutableStateOf<Double?>(0.0) }
    var fechaInicio by remember { mutableStateOf<LocalDate?>(null) }
    var fechaFin by remember { mutableStateOf<LocalDate?>(null) }
    var fechaConcil by remember { mutableStateOf<LocalDate?>(null) }

    // Cargar datos de veterinarios y servicios
    val transportes = remember { mutableStateOf<List<Transporte>>(emptyList()) }
    val servicios = remember { mutableStateOf<List<ServTransporte>>(emptyList()) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            transportes.value = ContratadosDB.getTransportesForComboBox()
            servicios.value = ServiciosDB.getServiciosTransporteForComboBox()
        }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp).width(800.dp),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Agregar Contrato de Transporte", style = MaterialTheme.typography.h6)

                // ComboBox para seleccionar un veterinario
                Row(modifier = Modifier.padding(0.dp)) {
                    DropdownMenu(
                        label = { Text("Transportistas") },
                        items = transportes.value,
                        selectedItem = selectedTrans,
                        onItemSelected = { selectedTrans = it },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    // ComboBox para seleccionar un servicio veterinario
                    DropdownMenu(
                        label = { Text("Servicio de Transporte") },
                        items = servicios.value,
                        selectedItem = selectedServ,
                        onItemSelected = { selectedServ = it },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row {// Campo para la descripción
                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción del Contrato") },
                        modifier = Modifier.width(300.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    Spinner(
                        value = recargo ?: 0.0,
                        onValueChange = { recargo = it },
                        label = { Text("Recargo") },
                        modifier = Modifier.weight(1f),
                        step = 0.5
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Spinner(
                        value = kilometros ?: 0.0,
                        onValueChange = { if (it == 0.0) kilometros = null else kilometros = it },
                        label = { Text("Km") },
                        modifier = Modifier.weight(1f),
                        step = 0.5
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Campos DatePicker para las fechas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DatePicker(
                        label = { Text("Fecha de Inicio") },
                        selectedDate = fechaInicio,
                        onDateChange = { fechaInicio = it },
                        modifier = Modifier.weight(1f)
                    )
                    DatePicker(
                        label = { Text("Fecha de Fin") },
                        selectedDate = fechaFin,
                        onDateChange = { fechaFin = it },
                        modifier = Modifier.weight(1f)
                    )
                    DatePicker(
                        label = { Text("Fecha de Conciliación") },
                        selectedDate = fechaConcil,
                        onDateChange = { fechaConcil = it },
                        modifier = Modifier.weight(1f)
                    )
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
                        if (selectedTrans != null && selectedServ != null && descripcion.isNotBlank() && kilometros != 0.0 && fechaInicio != null && fechaFin != null && fechaConcil != null) {
                            val newContrato = ContratoTransporte(
                                codigo = 0,
                                precio = ChronoUnit.DAYS.between(fechaInicio, fechaFin)
                                    .toInt() * selectedServ!!.precioUni + selectedServ!!.precioUni,
                                descripcion = descripcion,
                                nombreTrans = selectedTrans!!.nombre,
                                provinciaTrans = selectedTrans!!.provincia,
                                direccionTrans = selectedTrans!!.direccion,
                                precioUnit = selectedServ!!.precioUni,
                                fechaInicio = fechaInicio!!,
                                fechaFin = fechaFin!!,
                                fechaConcil = fechaConcil!!,
                                idTrans = selectedTrans!!.codigo,
                                idServ = selectedServ!!.codigo,
                                vehiculo = selectedServ!!.vehiculo
                            )
                            onContratoAdded(newContrato)
                        }
                    }) {
                        Text("Agregar")
                    }
                }
            }
        }
    }
}


//---------------------------ACTIVIDAD-----------------------------
