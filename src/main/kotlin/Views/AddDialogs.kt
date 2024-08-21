package Views

import Class_DB.ActividadDB
import Models.Actividad
import Models.Animal
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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

                OutlinedTextField(
                    value = edad?.toString() ?: "",
                    onValueChange = { edad = it.toIntOrNull() },
                    label = { Text("Edad") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = peso?.toString() ?: "",
                    onValueChange = { peso = it.toDoubleOrNull() },
                    label = { Text("Peso (kg)") },
                    modifier = Modifier.fillMaxWidth()
                )

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









//---------------------------ACTIVIDAD-----------------------------
