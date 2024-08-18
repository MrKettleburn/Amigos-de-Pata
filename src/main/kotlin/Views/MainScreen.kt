package Views

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Preview
@Composable
fun MainScreen() {
    // Aquí iría el contenido de la pantalla principal
    Text(text = "Welcome to the Main Screen!", style = MaterialTheme.typography.h4)
}