package Views

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainContent(colors: RefugioColorPalette) {
    Row(modifier = Modifier.fillMaxSize()) {
        AnimatedSideMenu(colors)
        MainArea(colors)
    }
}

@Composable
fun MainArea(colors: RefugioColorPalette) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Contenido principal aquí", color = colors.onBackground)
    }
}