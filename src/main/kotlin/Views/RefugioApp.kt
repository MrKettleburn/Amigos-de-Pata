package Views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RefugioApp(
    isLoggedIn: Boolean,
    onLogout: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val colorPalette = RefugioColorPalette()

    MaterialTheme(
        colors = colorPalette.toMaterialColors()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorPalette.background)
        ) {
            if (isLoggedIn) {
                Column {
                    Header(colorPalette)
                    MainContent(colors = colorPalette, onLogout = onLogout)
                }
                AnimatedPawPrints(colorPalette)
            } else {
                LoginScreen(colors = colorPalette, onLoginSuccess = onLoginSuccess)
            }
        }
    }
}
