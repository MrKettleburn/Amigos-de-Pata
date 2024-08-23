
import ReportesPDF.generarReporteContratosVeterinarios
import Views.LoginScreen
import Views.RefugioApp
import Views.RefugioColorPalette
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import java.awt.GraphicsDevice
import java.awt.GraphicsEnvironment
import java.time.LocalDateTime

@Composable
@Preview
fun App(window: java.awt.Window) {
    var isLoggedIn by remember { mutableStateOf(false) }

    // Instancia de la paleta de colores
    val colors = RefugioColorPalette()

    if (isLoggedIn) {
        // Cambia la ventana a pantalla completa
        LaunchedEffect(Unit) {
            setFullScreen(window)
        }
        RefugioApp()
    } else {
        LoginScreen(colors = colors, onLoginSuccess = { isLoggedIn = true })
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Login",
        state = androidx.compose.ui.window.WindowState(width = 700.dp, height = 600.dp)
    ) {
        App(window)
    }

//    LaunchedEffect(Unit) {
//        generarReporteContratosVeterinarios("C:\\Users\\ruben\\IdeaProjects\\Amigos_de_Pata\\src\\main\\kotlin\\PruebaPDF.pdf", LocalDateTime.now())
//    }
}

fun setFullScreen(window: java.awt.Window) {
    val graphicsDevice: GraphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice
    graphicsDevice.fullScreenWindow = window // Configurar la ventana a pantalla completa
}
