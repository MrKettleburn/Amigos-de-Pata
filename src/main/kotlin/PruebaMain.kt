import Views.LoginScreen
import Views.MainScreen
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
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
        MainScreen()
    } else {
        LoginScreen(onLoginSuccess = { isLoggedIn = true })
    }
}


fun Dimension.toDpSize(): DpSize {
    val density = java.awt.Toolkit.getDefaultToolkit().screenResolution / 96.0 // Obtén la densidad de pantalla
    val widthDp: Dp = (width / density).dp
    val heightDp: Dp = (height / density).dp
    return DpSize(widthDp, heightDp)
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

// Función para poner la ventana a pantalla completa
fun setFullScreen(window: java.awt.Window) {
    val graphicsDevice: GraphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice
    graphicsDevice.fullScreenWindow = window // Configurar la ventana a pantalla completa
}