import Visuals.LoginScreen
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import java.awt.Toolkit

@Composable
@Preview
fun App() {
    var isLoggedIn by remember { mutableStateOf(false) }

    // Si el usuario está autenticado, muestra la pantalla principal
    if (isLoggedIn) {
        MainScreen()
    } else {
        // Muestra la pantalla de login
        LoginScreen(onLoginSuccess = { isLoggedIn = true })
    }
}

@Composable
fun MainScreen() {
    // Aquí iría el contenido de la pantalla principal
    Text(text = "Welcome to the Main Screen!", style = MaterialTheme.typography.h4)
}

fun main() = application {
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val windowWidth = 400.dp // Ajustar el ancho deseado
    val windowHeight = 600.dp // Ajustar la altura deseada

    Window(
        onCloseRequest = ::exitApplication,
        title = "Login",
        state = WindowState(
            size = DpSize(windowWidth, windowHeight),
            position = WindowPosition(Alignment.Center)
        )
    ) {
        App()
    }
}