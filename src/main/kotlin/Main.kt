import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import Views.RefugioApp

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Refugio Animal") {
        RefugioApp()
    }
}