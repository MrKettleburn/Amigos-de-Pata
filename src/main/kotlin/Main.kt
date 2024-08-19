import Views.LoginScreen
import Views.MainScreen
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import Views.RefugioApp
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import java.awt.Dimension
import java.awt.GraphicsDevice
import java.awt.GraphicsEnvironment

@Composable
@Preview
fun App(window: java.awt.Window) {
    var isLoggedIn by remember { mutableStateOf(false) }

    if (isLoggedIn) {
        // Cambia la ventana a pantalla completa
        LaunchedEffect(Unit) {
            setFullScreen(window)
        }
        RefugioApp()
    } else {
        LoginScreen(onLoginSuccess = { isLoggedIn = true })
    }
}

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title = "Login",
        state = androidx.compose.ui.window.WindowState(width = 800.dp, height = 600.dp)
    ) {
        App(window)
    }
}
fun setFullScreen(window: java.awt.Window) {
    val graphicsDevice: GraphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice
    graphicsDevice.fullScreenWindow = window // Configurar la ventana a pantalla completa
}

