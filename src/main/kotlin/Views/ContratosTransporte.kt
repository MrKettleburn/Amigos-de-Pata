package Views


import Class_DB.ActividadDB
import Class_DB.AnimalDB
import Class_DB.ContratoDB
import Models.Actividad
import Models.Animal
import Models.ContratoTransporte
import Models.ContratoVeterinario
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*


@Composable
fun ContratosTransporteMostrar(colors: RefugioColorPalette, selectedItem: String, selectedSubItem: String) {
    val coroutineScope = rememberCoroutineScope()
    var contratos by remember { mutableStateOf<List<ContratoTransporte>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }


    // Estados para los filtros
    var codigo by remember { mutableStateOf<String?>(null) }
    var precioLI by remember { mutableStateOf<Double?>(null) }
    var precioLS by remember { mutableStateOf<Double?>(null) }
    var nombreTrans by remember { mutableStateOf<String?>(null) }
    var provinciaTrans by remember { mutableStateOf<String?>(null) }
    var fechaInicioLI by remember { mutableStateOf<LocalDate?>(null) }
    var fechaInicioLS by remember { mutableStateOf<LocalDate?>(null) }
    var fechaFinLI by remember { mutableStateOf<LocalDate?>(null) }
    var fechaFinLS by remember { mutableStateOf<LocalDate?>(null) }
    var fechaConcilLI by remember { mutableStateOf<LocalDate?>(null) }
    var fechaConcilLS by remember { mutableStateOf<LocalDate?>(null) }


    // Cargar los datos iniciales
    LaunchedEffect(Unit) {
        contratos = ContratoDB.getContratosTransporteFilter(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
          )
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xDCFFFFFF)).padding(16.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(1.dp)
                .background(Color(255,251,242,0))
        ) {
            // Título de la sección
            Text(
                text = "$selectedItem - $selectedSubItem",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Componentes de filtrado
            FilterComponentsTransporte(
                colors,
                onFilterApplied = {
                    // Convertir los valores de los filtros a los tipos correctos y aplicar el filtro
                    coroutineScope.launch {
                        contratos = ContratoDB.getContratosTransporteFilter(
                            codigo?.toIntOrNull(),
                            precioLI,
                            precioLS,
                            nombreTrans,
                            provinciaTrans,
                            fechaInicioLI?.format(DateTimeFormatter.ISO_DATE),
                            fechaInicioLS?.format(DateTimeFormatter.ISO_DATE),
                            fechaFinLI?.format(DateTimeFormatter.ISO_DATE),
                            fechaFinLS?.format(DateTimeFormatter.ISO_DATE),
                            fechaConcilLI?.format(DateTimeFormatter.ISO_DATE),
                            fechaConcilLS?.format(DateTimeFormatter.ISO_DATE)
                        )
                    }
                },
                codigo = codigo,
                onCodigoChange = { codigo = it },
                precioLI = precioLI,
                onPrecioLIChange = { precioLI = it },
                precioLS = precioLS,
                onPrecioLSChange = { precioLS = it },
                nombreTrans = nombreTrans,
                onNombreTransChange = { nombreTrans = it },
                provinciaTrans = provinciaTrans,
                onProvinciaTransChange = { provinciaTrans = it },
                fechaInicioLI = fechaInicioLI,
                onFechaInicioLIChange = { fechaInicioLI = it },
                fechaInicioLS = fechaInicioLS,
                onFechaInicioLSChange = { fechaInicioLS = it },
                fechaFinLI = fechaFinLI,
                onFechaFinLIChange = { fechaFinLI = it },
                fechaFinLS = fechaFinLS,
                onFechaFinLSChange = { fechaFinLS = it },
                fechaConcilLI = fechaConcilLI,
                onFechaConcilLIChange = { fechaConcilLI = it },
                fechaConcilLS = fechaConcilLS,
                onFechaConcilLSChange = { fechaConcilLS = it }
            )

            // Tabla expandible
            ContratosTransporteExpandableTable(colors, getContratosTransporteTableRows(contratos))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            FloatingActionButton(
                onClick = { coroutineScope.launch { contratos = ContratoDB.getContratosTransporteFilter(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                ) } },
                modifier = Modifier.padding(bottom = 16.dp)
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
            AddContratoTransporteDialog(
                colors = colors,
                onDismissRequest = { showDialog = false },
                onContratoAdded = { newContrato ->
                    coroutineScope.launch {

                        print(newContrato)
                        val success = ContratoDB.createContratoTransporte(newContrato)

                        if(success!=-1) {
                            contratos = ContratoDB.getContratosTransporteFilter(
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                            )
                            showDialog = false
                        }
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
    precioLI: Double?,
    onPrecioLIChange: (Double?) -> Unit,
    precioLS: Double?,
    onPrecioLSChange: (Double?) -> Unit,
    nombreTrans: String?,
    onNombreTransChange: (String?) -> Unit,
    provinciaTrans: String?,
    onProvinciaTransChange: (String?) -> Unit,
    fechaInicioLI: LocalDate?,
    onFechaInicioLIChange: (LocalDate?) -> Unit,
    fechaInicioLS: LocalDate?,
    onFechaInicioLSChange: (LocalDate?) -> Unit,
    fechaFinLI: LocalDate?,
    onFechaFinLIChange: (LocalDate?) -> Unit,
    fechaFinLS: LocalDate?,
    onFechaFinLSChange: (LocalDate?) -> Unit,
    fechaConcilLI: LocalDate?,
    onFechaConcilLIChange: (LocalDate?) -> Unit,
    fechaConcilLS: LocalDate?,
    onFechaConcilLSChange: (LocalDate?) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Primera fila de filtros
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = codigo.orEmpty(),
                onValueChange = { onCodigoChange(if (it.isEmpty()) null else it) },
                label = { Text("Código") },
                modifier = Modifier.width(100.dp)
            )
            Spinner(
                value = precioLI ?: 0.0,
                onValueChange = { onPrecioLIChange(it) },
                label = { Text("Precio Min") },
                modifier = Modifier.weight(1f),
                step = 0.5
            )
            Spinner(
                value = precioLS ?: 0.0,
                onValueChange = { onPrecioLSChange(it) },
                label = { Text("Precio Max") },
                modifier = Modifier.weight(1f),
                step = 0.5
            )
            OutlinedTextField(
                value = nombreTrans.orEmpty(),
                onValueChange = { onNombreTransChange(if (it.isEmpty()) null else it) },
                label = { Text("Nombre Trans") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = provinciaTrans.orEmpty(),
                onValueChange = { onProvinciaTransChange(if (it.isEmpty()) null else it) },
                label = { Text("Provincia") },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = { onFilterApplied() },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text("Filtrar")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Segunda fila de filtros
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            DatePicker(
                label = { Text("Fecha Inicio LI") },
                selectedDate = fechaInicioLI,
                onDateChange = { onFechaInicioLIChange(it) },
                modifier = Modifier.weight(1f)
            )
            DatePicker(
                label = { Text("Fecha Inicio LS") },
                selectedDate = fechaInicioLS,
                onDateChange = { onFechaInicioLSChange(it) },
                modifier = Modifier.weight(1f)
            )
            DatePicker(
                label = { Text("Fecha Fin LI") },
                selectedDate = fechaFinLI,
                onDateChange = { onFechaFinLIChange(it) },
                modifier = Modifier.weight(1f)
            )
            DatePicker(
                label = { Text("Fecha Fin LS") },
                selectedDate = fechaFinLS,
                onDateChange = { onFechaFinLSChange(it) },
                modifier = Modifier.weight(1f)
            )
            DatePicker(
                label = { Text("Fecha Concil. LI") },
                selectedDate = fechaConcilLI,
                onDateChange = { onFechaConcilLIChange(it) },
                modifier = Modifier.weight(1f)
            )
            DatePicker(
                label = { Text("Fecha Concil. LS") },
                selectedDate = fechaConcilLS,
                onDateChange = { onFechaConcilLSChange(it) },
                modifier = Modifier.weight(1f)
            )
        }

    }
}


@Composable
fun ContratosTransporteExpandableTable(colors: RefugioColorPalette, data: List<ContratoTableRow>) {
    LazyColumn {
        items(data) { row ->
            ContratosTransporteExpandableRow(colors, row)
            Divider(color = colors.primary, thickness = 1.5.dp)
        }
    }
}


@Composable
fun ContratosTransporteExpandableRow(colors: RefugioColorPalette, row: ContratoTableRow) {
    var expanded by remember { mutableStateOf(false) }
    val backgroundColor = if (expanded) colors.menuBackground else Color.Transparent
    var showError by remember { mutableStateOf(false) }
    var showDeleteContratDialog by remember { mutableStateOf(false) }
    var checkPoder by remember { mutableStateOf<List<Actividad>?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var showConfirmDeleteWithAct by remember { mutableStateOf(false) }

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
                    modifier = Modifier.padding(0.dp)
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
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
            Row {

                IconButton(onClick = {  showDeleteContratDialog = true }) {
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

        if (showDeleteContratDialog) {
            ConfirmDeleteContratoDialog(
                colors = colors,
                text = "Transporte",
                contratoId = row.id.toInt(),
                onDismissRequest = { showDeleteContratDialog = false },
                onConfirmDelete = {
                    coroutineScope.launch {
                        val actividades = ActividadDB.getActividadesConContratoID(row.id.toInt())

                        if(actividades!=null) {
                            checkPoder = actividades
                            if (actividades.isEmpty()) {
                                try {
                                    ContratoDB.deleteContrato(row.id.toInt())
                                    showDeleteContratDialog = false
                                } catch (e: Exception) {
                                    println("Error al eliminar el contrato: ${e.message}")
                                }
                            } else {
                                showDeleteContratDialog = false
                                showConfirmDeleteWithAct = true
                            }
                        }
                        else
                        {
                            println("ERROR")
                        }
                    }
                }
            )
        }

        if(showConfirmDeleteWithAct)
        {
            ConfirmDeleteContratoWithActDialog(
                colors=colors,
                title = "Confirmar Eliminación",
                text = "El contrato está vinculado a ${checkPoder?.size} actividades. Si elimina el contrato se eliminarán las actividades. Desea eliminar el contrato?",
                contrId = row.id.toInt(),
                onDismissRequest = {showConfirmDeleteWithAct=false},
                onConfirmDelete = {
                    coroutineScope.launch {
                        try {
                            ContratoDB.deleteContrato(row.id.toInt())
                            showConfirmDeleteWithAct = false
                        } catch (e: Exception) {
                            println("Error al eliminar el contrato: ${e.message}")
                        }
                    }
                }
            )
        }


        if (showError) {
            showErrorDialog(
                title = "Error",
                message = "El contrato no puede borrarse porque está vinculado a ${checkPoder?.size} actividades",
                onDismissRequest = { showError = false }
            )
        }
    }
}


fun getContratosTransporteTableRows(contratos: List<ContratoTransporte>): List<ContratoTableRow> {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return contratos.map { contrato ->
        ContratoTableRow(
            id = contrato.codigo.toString(),
            mainAttributes = mapOf(
                "Código" to "${contrato.codigo}",
                "Precio" to "\$${contrato.precio}",
                "Nombre del Contratado" to contrato.nombreTrans,
            ),
            expandedAttributes = mapOf(
                "Descripción" to contrato.descripcion,
                "Vehículo" to contrato.vehiculo,
                "Provincia del Contratado" to contrato.provinciaTrans,
                "Dirección del Contratado" to contrato.direccionTrans,
                "Precio Unitario del Servicio" to "${contrato.precioUnit}/km",
                "Fecha de Inicio" to contrato.fechaInicio.format(formatter),
                "Fecha de Fin" to contrato.fechaFin.format(formatter),
                "Fecha de Conciliación" to contrato.fechaConcil.format(formatter),
            )
        )
    }
}