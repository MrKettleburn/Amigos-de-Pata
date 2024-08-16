import Class_DB.AnimalDB
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Button(onClick = {
            text = "Hello, Desktop!"
        }) {
            Text(text)
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
//    val animales = AnimalDB.getAnimales()
//    for (animal in animales) {
//        println("Código: ${animal.codigo}, Nombre: ${animal.nombre}, Especie: ${animal.especie}, Raza: ${animal.raza}, Edad: ${animal.edad}, Peso: ${animal.peso}, Cantidad de Días: ${animal.cantDias}")
//    }
}
