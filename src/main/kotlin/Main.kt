import Class_DB.AnimalDB
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {
    var message by remember { mutableStateOf("") }

    // Estructura de la UI
    MaterialTheme {
        // Layout básico
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Botón que actualiza el mensaje
            Button(onClick = {
                message = "¡Hola! Este es tu mensaje."
            }) {
                Text("Mostrar Mensaje")
            }

            // Espacio vertical entre el botón y el mensaje
            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar el mensaje si está definido
            if (message.isNotEmpty()) {
                Text(text = message)
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }

//    val animales = AnimalDB.getAnimalesFilter("Rex",null,null,null,null,null)
////    val animales = AnimalDB.getAnimales()
//    for (animal in animales) {
//        println("Código: ${animal.codigo}, Nombre: ${animal.nombre}, Especie: ${animal.especie}, Raza: ${animal.raza}, Edad: ${animal.edad}, Peso: ${animal.peso}, Cantidad de Días: ${animal.cantDias}")
//    }
}
