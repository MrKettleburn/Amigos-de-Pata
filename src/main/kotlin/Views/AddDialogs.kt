package Views

import Class_DB.*
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
import java.time.LocalTime
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

@Composable
fun UpdateAnimalDialog(
    colors: RefugioColorPalette,
    codigo: Int,
    nombreInicial: String,
    especieInicial: String,
    razaInicial: String,
    edadInicial: Int,
    pesoInicial: Double,
    fechaIngresoInicial: LocalDate,
    onDismissRequest: () -> Unit,
    onAnimalUpdated: (Int, String, String, String, Int, Double, LocalDate) -> Unit
) {
    var nombre by remember { mutableStateOf(nombreInicial) }
    var especie by remember { mutableStateOf(especieInicial) }
    var raza by remember { mutableStateOf(razaInicial) }
    var edad by remember { mutableStateOf(edadInicial) }
    var peso by remember { mutableStateOf(pesoInicial) }
    var fechaIngreso by remember { mutableStateOf(fechaIngresoInicial) }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Actualizar Información de $nombreInicial", style = MaterialTheme.typography.h6)

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

                Row(modifier = Modifier.padding(0.dp)) {
                    OutlinedTextField(
                        value = edad.toString(),
                        onValueChange = { edad = it.toIntOrNull() ?: edad },
                        label = { Text("Edad") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    OutlinedTextField(
                        value = peso.toString(),
                        onValueChange = { peso = it.toDoubleOrNull() ?: peso },
                        label = { Text("Peso (kg)") },
                        modifier = Modifier.weight(1f)
                    )
                }

                DatePicker(
                    label = { Text("Fecha de Ingreso") },
                    selectedDate = fechaIngreso,
                    onDateChange = {
                        if (it != null) {
                            fechaIngreso = it
                        }
                    },
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
                            onAnimalUpdated(
                                codigo,
                                nombre,
                                especie,
                                raza,
                                edad,
                                peso,
                                fechaIngreso
                            )
                        }
                    }) {
                        Text("Actualizar")
                    }
                }
            }
        }
    }
}

///////////////----------------------ACTIVIDAD-------------------------////////////////////////

@Composable
fun AddActividadDialog(
    colors: RefugioColorPalette,
    onDismissRequest: () -> Unit,
    onActividadAdded: (Actividad) -> Unit,
    animalId: Int // ID del animal para la actividad
) {
    val coroutineScope = rememberCoroutineScope()

    // Estados para los campos
    var tipoActividad by remember { mutableStateOf<String?>(null) }
    var selectedContratoVet by remember { mutableStateOf<ContratoVeterinario?>(null) }
    var selectedContratoProveedor by remember { mutableStateOf<ContratoProveedorAlim?>(null) }
    var selectedContratoTransporte by remember { mutableStateOf<ContratoTransporte?>(null) }
    var fechaActividad by remember { mutableStateOf<LocalDate?>(null) }
    var horaActividad by remember { mutableStateOf<LocalTime?>(null) }
    var descripcion by remember { mutableStateOf("") }

    // Tipos de actividad disponibles
    val tiposActividad = listOf("Atención Médica", "Socialización y Entrenamiento", "Alimentación")

    // Cargar contratos dependiendo del tipo de actividad seleccionado
    val contratosVet = remember { mutableStateOf<List<ContratoVeterinario>>(emptyList()) }
    val contratosProveedor = remember { mutableStateOf<List<ContratoProveedorAlim>>(emptyList()) }
    val contratosTransporte = remember { mutableStateOf<List<ContratoTransporte>>(emptyList()) }

    LaunchedEffect(tipoActividad) {
        if (tipoActividad != null) {
            coroutineScope.launch {
                when (tipoActividad) {
                    "Atención Médica" -> {
                        contratosVet.value = ContratoDB.getContratosVeterinariosForComboBox()
                        selectedContratoVet = null
                        selectedContratoTransporte = null
                        selectedContratoProveedor = null
                    }
                    "Socialización y Entrenamiento" -> {
                        contratosTransporte.value = ContratoDB.getContratosTransporteForComboBox()
                        selectedContratoVet = null
                        selectedContratoTransporte = null
                        selectedContratoProveedor = null
                    }
                    "Alimentación" -> {
                        contratosProveedor.value = ContratoDB.getContratosProveedoresAlimentosForComboBox()
                        selectedContratoVet = null
                        selectedContratoTransporte = null
                        selectedContratoProveedor = null
                    }
                }
            }
        }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp).width(800.dp),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Agregar Actividad", style = MaterialTheme.typography.h6)

                // ComboBox para seleccionar el tipo de actividad
                DropdownMenu(
                    label = { Text("Tipo de Actividad") },
                    items = tiposActividad,
                    selectedItem = tipoActividad,
                    onItemSelected = { tipoActividad = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // ComboBox para seleccionar el contrato (habilitado solo si se seleccionó un tipo de actividad)
                when (tipoActividad) {
                    "Atención Médica" -> {
                        DropdownMenu(
                            label = { Text("Contrato Veterinario") },
                            items = contratosVet.value,
                            selectedItem = selectedContratoVet,
                            onItemSelected = { selectedContratoVet = it },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                    "Socialización y Entrenamiento" -> {
                        DropdownMenu(
                            label = { Text("Contrato de Transporte") },
                            items = contratosTransporte.value,
                            selectedItem = selectedContratoTransporte,
                            onItemSelected = { selectedContratoTransporte = it },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                    "Alimentación" -> {
                        DropdownMenu(
                            label = { Text("Contrato de Proveedor de Alimentos") },
                            items = contratosProveedor.value,
                            selectedItem = selectedContratoProveedor,
                            onItemSelected = { selectedContratoProveedor = it },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción de la Actividad") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row (modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    // Campo DatePicker para la fecha de la actividad
                    DatePicker(
                        label = { Text("Fecha de la Actividad") },
                        selectedDate = fechaActividad,
                        onDateChange = { fechaActividad = it },
                        modifier = Modifier.weight(1f)
                    )

                    // Campo TimePicker para la hora de la actividad
                    TimePicker(
                        label = { Text("Hora de la Actividad") },
                        selectedTime = horaActividad,
                        onTimeChange = { horaActividad = it },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botones de acción
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        if (tipoActividad != null && fechaActividad != null && horaActividad != null && descripcion.isNotBlank()) {
                            val nuevaActividad = when (tipoActividad) {
                                "Atención Médica" -> {
                                    if (selectedContratoVet != null) {
                                        Actividad(
                                            codigo = 0,
                                            tipo = tipoActividad!!,
                                            codigoContr = selectedContratoVet!!.codigo,
                                            fecha = fechaActividad!!,
                                            hora = horaActividad!!,
                                            codigoAnim = animalId,
                                            costo = selectedContratoVet!!.costoUnit,
                                            descrip = descripcion,
                                            tipoContrato = "Veterinario"
                                        )
                                    } else null
                                }
                                "Socialización y Entrenamiento" -> {
                                    if (selectedContratoTransporte != null) {
                                        Actividad(
                                            codigo = 0,
                                            tipo = tipoActividad!!,
                                            codigoContr = selectedContratoTransporte!!.codigo,
                                            fecha = fechaActividad!!,
                                            hora = horaActividad!!,
                                            codigoAnim = animalId,
                                            costo = selectedContratoTransporte!!.costoUnit,
                                            descrip = descripcion,
                                            tipoContrato = "Transporte"
                                        )
                                    } else null
                                }
                                "Alimentación" -> {
                                    if (selectedContratoProveedor != null) {
                                        Actividad(
                                            codigo = 0,
                                            tipo = tipoActividad!!,
                                            codigoContr = selectedContratoProveedor!!.codigo,
                                            fecha = fechaActividad!!,
                                            hora = horaActividad!!,
                                            codigoAnim = animalId,
                                            costo = selectedContratoProveedor!!.costoUnit,
                                            descrip = descripcion,
                                            tipoContrato = "Proveedor de alimentos"
                                        )
                                    } else null
                                }
                                else -> null
                            }
                            if (nuevaActividad != null) {
                                onActividadAdded(nuevaActividad)
                            }
                        }
                        else
                        {
                            println("ERROR")
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
    var recargo by remember { mutableStateOf<Double>(0.0) }
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
                Row() {
                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción del Contrato") },
                        modifier = Modifier.width(300.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Spinner(
                        value = recargo,
                        onValueChange = { recargo = it },
                        label = { Text("Recargo") },
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
                        if (selectedVet != null && selectedServ != null && descripcion.isNotBlank() && fechaInicio != null && fechaFin != null && fechaConcil != null) {
                            val newContrato = ContratoVeterinario(
                                codigo = 0,
                                precio = ChronoUnit.DAYS.between(fechaInicio,fechaFin).toInt() * selectedServ!!.precioUni + selectedServ!!.precioUni +recargo,
                                costoUnit = selectedServ!!.precioUni,
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
                        else
                        {
                            println("ERROR")
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
    var recargo by remember { mutableStateOf<Double>(0.0) }
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
                        value = recargo,
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
                            coroutineScope.launch {
                                val totalAnimales = AnimalDB.totalDeAnimales()

                                val newContrato = ContratoTransporte(
                                    codigo = 0,
                                    precio = (ChronoUnit.DAYS.between(fechaInicio, fechaFin)
                                        .toInt() * (selectedServ!!.precioUni * kilometros!!)) + (selectedServ!!.precioUni * kilometros!!) + recargo,
                                    costoUnit = (selectedServ!!.precioUni * kilometros!!) / totalAnimales,
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
                        }
                    }) {
                        Text("Agregar")
                    }
                }
            }
        }
    }
}

////////////////////////-------------------------CONTRATO PROV ALIMENTO-------------------/////////////////////////

@Composable
fun AddContratoProvAlimentosDialog(
    colors: RefugioColorPalette,
    onDismissRequest: () -> Unit,
    onContratoAdded: (ContratoProveedorAlim) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    // Estados para los campos
    var selectedProv by remember { mutableStateOf<ProveedorDeAlimentos?>(null) }
    var selectedServ by remember { mutableStateOf<ServAlimenticio?>(null) }
    var kilogramos by remember { mutableStateOf<Double>(0.0) }
    var descripcion by remember { mutableStateOf("") }
    var recargo by remember { mutableStateOf<Double>(0.0) }
    var fechaInicio by remember { mutableStateOf<LocalDate?>(null) }
    var fechaFin by remember { mutableStateOf<LocalDate?>(null) }
    var fechaConcil by remember { mutableStateOf<LocalDate?>(null) }

    // Cargar datos de veterinarios y servicios
    val proveedores = remember { mutableStateOf<List<ProveedorDeAlimentos>>(emptyList()) }
    val servicios = remember { mutableStateOf<List<ServAlimenticio>>(emptyList()) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            proveedores.value = ContratadosDB.getProveedoresAlimForComboBox()
            servicios.value = ServiciosDB.getServiciosAlimenticiosForComboBox()
        }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp).width(800.dp),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Agregar Contrato de Alimentacion", style = MaterialTheme.typography.h6)

                // ComboBox para seleccionar un veterinario
                Row(modifier = Modifier.padding(0.dp)) {
                    DropdownMenu(
                        label = { Text("Proveedores") },
                        items = proveedores.value,
                        selectedItem = selectedProv,
                        onItemSelected = { selectedProv = it },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    // ComboBox para seleccionar un servicio veterinario
                    DropdownMenu(
                        label = { Text("Servicios Alimenticios") },
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
                        value = recargo,
                        onValueChange = { recargo = it },
                        label = { Text("Recargo") },
                        modifier = Modifier.weight(1f),
                        step = 0.5
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Spinner(
                        value = kilogramos?:0.0,
                        onValueChange = { kilogramos = it },
                        label = { Text("Kg") },
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
                        if (selectedProv != null && selectedServ != null && descripcion.isNotBlank() && kilogramos > 0.0 && fechaInicio != null && fechaFin != null && fechaConcil != null && recargo>0.0) {
                            coroutineScope.launch {
                                val totalAnimales = AnimalDB.totalDeAnimales()
                                val newContrato = ContratoProveedorAlim(
                                    codigo = 0,
                                    precio = ChronoUnit.DAYS.between(fechaInicio, fechaFin)
                                        .toInt() * (selectedServ!!.precioUni * kilogramos) + (selectedServ!!.precioUni * kilogramos) + recargo,
                                    costoUnit = (selectedServ!!.precioUni * kilogramos) /totalAnimales,
                                    descripcion = descripcion,
                                    nombreProv = selectedProv!!.nombre,
                                    provinciaProv = selectedProv!!.provincia,
                                    direccProv = selectedProv!!.direccion,
                                    precioUnit = selectedServ!!.precioUni,
                                    fechaInicio = fechaInicio!!,
                                    fechaFin = fechaFin!!,
                                    fechaConcil = fechaConcil!!,
                                    idProv = selectedProv!!.codigo,
                                    idServ = selectedServ!!.codigo,
                                    tipoAlim = selectedServ!!.tipoAlimento
                                )
                                onContratoAdded(newContrato)
                            }
                        }
                        else{
                            print("ERROR")
                        }
                    }) {
                        Text("Agregar")
                    }
                }
            }
        }
    }
}

/////////////////---------------------------SERVICIO ALIMENTICIO-------------//////////////////////


@Composable
fun AddServicioAlimenticioDialog(
    colors: RefugioColorPalette,
    onDismissRequest: () -> Unit,
    onServicioAdded: (ServAlimenticio) -> Unit
) {
    var precio by remember { mutableStateOf(0.0) }
    var tipoAlimento by remember { mutableStateOf<String>("") }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Agregar Servicio Alimenticio", style = MaterialTheme.typography.h6)

                OutlinedTextField(
                    value = tipoAlimento,
                    onValueChange = { tipoAlimento = it },
                    label = { Text("Tipo de Alimento") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spinner(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    step = 0.5
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
                        if (tipoAlimento.isNotBlank() && precio > 0.0) {
                            onServicioAdded(
                                ServAlimenticio(
                                    codigo = 0,
                                    tipoAlimento = tipoAlimento,
                                    precioUni = precio
                                )
                            )
                        }
                        else{
                            print("ERROR")
                        }
                    }) {
                        Text("Agregar")
                    }
                }
            }
        }
    }
}

@Composable
fun UpdateServicioAlimenticioDialog(
    colors: RefugioColorPalette,
    codigo: Int,
    precioInicial: Double,
    tipoAlimInicial: String,
    onDismissRequest: () -> Unit,
    onServicioUpdated: (Int, String, Double) -> Unit
) {
    var precio by remember { mutableStateOf(precioInicial) }
    var vehiculo by remember { mutableStateOf(tipoAlimInicial) }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Actualizar Servicio Alimenticio", style = MaterialTheme.typography.h6)

                OutlinedTextField(
                    value = vehiculo,
                    onValueChange = { vehiculo = it },
                    label = { Text("Tipo de Alimento") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spinner(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    step = 0.5
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
                        if (vehiculo.isNotBlank() && precio > 0.0) {
                            onServicioUpdated(
                                codigo,
                                vehiculo,
                                precio
                            )
                        }
                        else{
                            print("ERROR")
                        }
                    }) {
                        Text("Actualizar")
                    }
                }
            }
        }
    }
}


//////////////////---------------------------SERVICIOS TRANSPORTE--------------/////////////////////

@Composable
fun AddServicioTransporteDialog(
    colors: RefugioColorPalette,
    onDismissRequest: () -> Unit,
    onServicioAdded: (ServTransporte) -> Unit
) {
    var precio by remember { mutableStateOf(0.0) }
    var vehiculo by remember { mutableStateOf<String>("") }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Agregar Servicio de Transporte", style = MaterialTheme.typography.h6)

                OutlinedTextField(
                    value = vehiculo,
                    onValueChange = { vehiculo = it },
                    label = { Text("Vehículo") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spinner(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    step = 0.5
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
                        if (vehiculo.isNotBlank() && precio > 0.0) {
                            onServicioAdded(
                                ServTransporte(
                                    codigo = 0,
                                    vehiculo = vehiculo,
                                    precioUni = precio
                                )
                            )
                        }
                        else{
                            print("ERROR")
                        }
                    }) {
                        Text("Agregar")
                    }
                }
            }
        }
    }
}

@Composable
fun UpdateServicioTransporteDialog(
    colors: RefugioColorPalette,
    codigo: Int,
    precioInicial: Double,
    vehiculoInicial: String,
    onDismissRequest: () -> Unit,
    onServicioUpdated: (Int, String, Double) -> Unit
) {
    var precio by remember { mutableStateOf(precioInicial) }
    var vehiculo by remember { mutableStateOf(vehiculoInicial) }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Actualizar Servicio Veterinario", style = MaterialTheme.typography.h6)

                OutlinedTextField(
                    value = vehiculo,
                    onValueChange = { vehiculo = it },
                    label = { Text("Vehículo") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spinner(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    step = 0.5
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
                        if (vehiculo.isNotBlank() && precio > 0.0) {
                            onServicioUpdated(
                                codigo,
                                vehiculo,
                                precio
                            )
                        }
                        else{
                            print("ERROR")
                        }
                    }) {
                        Text("Actualizar")
                    }
                }
            }
        }
    }
}


/////////////---------------------SERVICIOS VETERINARIOS--------------------///////////////
@Composable
fun AddServicioVeterinarioDialog(
    colors: RefugioColorPalette,
    onDismissRequest: () -> Unit,
    onServicioAdded: (ServVeterinario) -> Unit
) {
    var precio by remember { mutableStateOf(0.0) }
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

                Spinner(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    step = 0.5
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
                        if (modalidad.isNotBlank() && precio > 0.0) {
                            onServicioAdded(
                                ServVeterinario(
                                    codigo = 0,
                                    modalidad = modalidad,
                                    precioUni = precio
                                )
                            )
                        }
                        else{
                            print("ERROR")
                        }
                    }) {
                        Text("Agregar")
                    }
                }
            }
        }
    }
}

@Composable
fun UpdateServicioVeterinarioDialog(
    colors: RefugioColorPalette,
    codigo: Int,
    precioInicial: Double,
    modalidadInicial: String,
    onDismissRequest: () -> Unit,
    onServicioUpdated: (Int, String, Double) -> Unit
) {
    var precio by remember { mutableStateOf(precioInicial) }
    var modalidad by remember { mutableStateOf(modalidadInicial) }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Actualizar Servicio Veterinario", style = MaterialTheme.typography.h6)

                OutlinedTextField(
                    value = modalidad,
                    onValueChange = { modalidad = it },
                    label = { Text("Modalidad") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spinner(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    step = 0.5
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
                        if (modalidad.isNotBlank() && precio > 0.0) {
                            onServicioUpdated(
                                codigo,
                                modalidad,
                                precio
                            )
                        }
                        else{
                            print("ERROR")
                        }
                    }) {
                        Text("Actualizar")
                    }
                }
            }
        }
    }
}

///////////////----------------------PROV ALIMENTOS------------------------//////////////////

@Composable
fun AddProveedorDialog(
    colors: RefugioColorPalette,
    onDismissRequest: () -> Unit,
    onProveedorAdded: (ProveedorDeAlimentos) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var provincia by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Agregar Proveedor de Alimentos", style = MaterialTheme.typography.h6)

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = provincia,
                    onValueChange = { provincia = it },
                    label = { Text("Provincia") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono") },
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
                        if (nombre.isNotBlank() && email.isNotBlank() && provincia.isNotBlank() &&
                            direccion.isNotBlank() && telefono.isNotBlank()
                        ) {
                            onProveedorAdded(
                                ProveedorDeAlimentos(
                                    codigo = 0, // El código se generará automáticamente en la base de datos
                                    nombre = nombre,
                                    email = email,
                                    provincia = provincia,
                                    direccion = direccion,
                                    telefono = telefono
                                )
                            )
                        }
                        else{
                            print("ERROR")
                        }
                    }) {
                        Text("Agregar")
                    }
                }
            }
        }
    }
}


@Composable
fun UpdateProveedorAlimDialog(
    colors: RefugioColorPalette,
    codigo: Int,
    nombreInicial: String,
    emailInicial: String,
    provinciaInicial: String,
    direccionInicial: String,
    telefonoInicial: String,
    onDismissRequest: () -> Unit,
    onProveedorUpdated: (Int, String, String, String, String, String) -> Unit
) {
    var nombre by remember { mutableStateOf(nombreInicial) }
    var email by remember { mutableStateOf(emailInicial) }
    var provincia by remember { mutableStateOf(provinciaInicial) }
    var direccion by remember { mutableStateOf(direccionInicial) }
    var telefono by remember { mutableStateOf(telefonoInicial) }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Actualizar Proveedor de Alimentos", style = MaterialTheme.typography.h6)

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = provincia,
                    onValueChange = { provincia = it },
                    label = { Text("Provincia") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono") },
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
                        if (nombre.isNotBlank() && email.isNotBlank() && provincia.isNotBlank() &&
                            direccion.isNotBlank() && telefono.isNotBlank()
                        ) {
                            onProveedorUpdated(
                                codigo, // Se usa el código existente para la actualización
                                nombre,
                                email,
                                provincia,
                                direccion,
                                telefono,
                            )
                        }
                        else{
                            print("ERROR")
                        }
                    }) {
                        Text("Actualizar")
                    }
                }
            }
        }
    }
}

//////////////////-----------------------TRANSPORTE---------------///////////////////////

@Composable
fun AddTransportistaDialog(
    colors: RefugioColorPalette,
    onDismissRequest: () -> Unit,
    onTransportistaAdded: (Transporte) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var provincia by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Agregar Transportista", style = MaterialTheme.typography.h6)

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = provincia,
                    onValueChange = { provincia = it },
                    label = { Text("Provincia") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono") },
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
                        if (nombre.isNotBlank() && email.isNotBlank() && provincia.isNotBlank() &&
                            direccion.isNotBlank() && telefono.isNotBlank()
                        ) {
                            onTransportistaAdded(
                                Transporte(
                                    codigo = 0, // El código se generará automáticamente en la base de datos
                                    nombre = nombre,
                                    email = email,
                                    provincia = provincia,
                                    direccion = direccion,
                                    telefono = telefono
                                )
                            )
                        }
                        else{
                            print("ERROR")
                        }
                    }) {
                        Text("Agregar")
                    }
                }
            }
        }
    }
}


@Composable
fun UpdateTransporteDialog(
    colors: RefugioColorPalette,
    codigo: Int,
    nombreInicial: String,
    emailInicial: String,
    provinciaInicial: String,
    direccionInicial: String,
    telefonoInicial: String,
    onDismissRequest: () -> Unit,
    onTransporteUpdated: (Int, String, String, String, String, String) -> Unit
) {
    var nombre by remember { mutableStateOf(nombreInicial) }
    var email by remember { mutableStateOf(emailInicial) }
    var provincia by remember { mutableStateOf(provinciaInicial) }
    var direccion by remember { mutableStateOf(direccionInicial) }
    var telefono by remember { mutableStateOf(telefonoInicial) }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Actualizar Transporte", style = MaterialTheme.typography.h6)

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = provincia,
                    onValueChange = { provincia = it },
                    label = { Text("Provincia") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono") },
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
                        if (nombre.isNotBlank() && email.isNotBlank() && provincia.isNotBlank() &&
                            direccion.isNotBlank() && telefono.isNotBlank()
                        ) {
                            onTransporteUpdated(
                                codigo, // Se usa el código existente para la actualización
                                nombre,
                                email,
                                provincia,
                                direccion,
                                telefono,
                            )
                        }
                        else{
                            print("ERROR")
                        }
                    }) {
                        Text("Actualizar")
                    }
                }
            }
        }
    }
}

/////////////////------------------VETERINARIOS---------------------///////////////////////



@Composable
fun AddVeterinarioDialog(
    colors: RefugioColorPalette,
    onDismissRequest: () -> Unit,
    onVeterinarioAdded: (Veterinario) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var provincia by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var especialidad by remember { mutableStateOf("") }
    var clinica by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Agregar Veterinario", style = MaterialTheme.typography.h6)

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = provincia,
                    onValueChange = { provincia = it },
                    label = { Text("Provincia") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = especialidad,
                    onValueChange = { especialidad = it },
                    label = { Text("Especialidad") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = clinica,
                    onValueChange = { clinica = it },
                    label = { Text("Clínica") },
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
                        if (nombre.isNotBlank() && email.isNotBlank() && provincia.isNotBlank() &&
                            direccion.isNotBlank() && telefono.isNotBlank() && especialidad.isNotBlank() && clinica.isNotBlank()
                        ) {
                            onVeterinarioAdded(
                                Veterinario(
                                    codigo = 0, // El código se generará automáticamente en la base de datos
                                    nombre = nombre,
                                    email = email,
                                    provincia = provincia,
                                    direccion = direccion,
                                    telefono = telefono,
                                    especialidad = especialidad,
                                    clinica = clinica
                                )
                            )
                        }
                        else{
                            print("ERROR")
                        }
                    }) {
                        Text("Agregar")
                    }
                }
            }
        }
    }
}

@Composable
fun UpdateVeterinarioDialog(
    colors: RefugioColorPalette,
    codigo: Int,
    nombreInicial: String,
    emailInicial: String,
    provinciaInicial: String,
    direccionInicial: String,
    telefonoInicial: String,
    especialidadInicial: String,
    clinicaInicial: String,
    onDismissRequest: () -> Unit,
    onVeterinarioUpdated: (Int, String, String, String, String, String, String, String) -> Unit
) {
    var nombre by remember { mutableStateOf(nombreInicial) }
    var email by remember { mutableStateOf(emailInicial) }
    var provincia by remember { mutableStateOf(provinciaInicial) }
    var direccion by remember { mutableStateOf(direccionInicial) }
    var telefono by remember { mutableStateOf(telefonoInicial) }
    var especialidad by remember { mutableStateOf(especialidadInicial) }
    var clinica by remember { mutableStateOf(clinicaInicial) }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Actualizar Veterinario", style = MaterialTheme.typography.h6)

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = provincia,
                    onValueChange = { provincia = it },
                    label = { Text("Provincia") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = especialidad,
                    onValueChange = { especialidad = it },
                    label = { Text("Especialidad") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = clinica,
                    onValueChange = { clinica = it },
                    label = { Text("Clínica") },
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
                        if (nombre.isNotBlank() && email.isNotBlank() && provincia.isNotBlank() &&
                            direccion.isNotBlank() && telefono.isNotBlank() && especialidad.isNotBlank() && clinica.isNotBlank()
                        ) {
                            onVeterinarioUpdated(
                                    codigo, // Se usa el código existente para la actualización
                                    nombre,
                                    email,
                                    provincia,
                                    direccion,
                                    telefono,
                                    especialidad,
                                    clinica
                                )
                        }
                        else{
                            print("ERROR")
                        }
                    }) {
                        Text("Actualizar")
                    }
                }
            }
        }
    }
}