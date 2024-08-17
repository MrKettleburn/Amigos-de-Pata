import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.geometry.Offset // Importación corregida

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Refugio Animal") {
        RefugioApp()
    }
}

@Composable
fun RefugioApp() {
    val colorPalette = RefugioColorPalette()

    MaterialTheme(
        colors = colorPalette.toMaterialColors()
    ) {
        Box(modifier = Modifier.fillMaxSize().background(colorPalette.background)) {
            Column {
                Header(colorPalette)
                MainContent(colorPalette)
            }
            AnimatedPawPrints(colorPalette)
        }
    }
}

@Composable
fun Header(colors: RefugioColorPalette) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(
                Brush.horizontalGradient(
                    colors = listOf(colors.primary, colors.secondary)
                )
            )
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Refugio Feliz",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(Modifier.weight(1f))
            AnimatedLogo()
        }
    }
}

@Composable
fun AnimatedLogo() {
    var rotation by remember { mutableStateOf(0f) }
    LaunchedEffect(Unit) {
        while(true) {
            animate(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = tween(2000, easing = LinearEasing)
            ) { value, _ -> rotation = value }
        }
    }
    Icon(
        Icons.Default.Pets,
        contentDescription = "Logo",
        modifier = Modifier.size(50.dp).rotate(rotation),
        tint = Color.White
    )
}

@Composable
fun MainContent(colors: RefugioColorPalette) {
    Row(modifier = Modifier.fillMaxSize()) {
        AnimatedSideMenu(colors)
        MainArea(colors)
    }
}

@Composable
fun AnimatedSideMenu(colors: RefugioColorPalette) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Inicio", "Animales", "Adopciones", "Voluntarios", "Donaciones")

    Column(
        modifier = Modifier
            .width(200.dp)
            .fillMaxHeight()
            .background(colors.menuBackground)
            .padding(16.dp)
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = index == selectedItem
            Button(
                onClick = { selectedItem = index },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isSelected) colors.menuItemSelected else colors.menuItem
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .animateContentSize()
            ) {
                Text(
                    item,
                    color = if (isSelected) colors.onMenuItemSelected else colors.onMenuItem
                )
            }
        }
    }
}

@Composable
fun MainArea(colors: RefugioColorPalette) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Contenido principal aquí", color = colors.onBackground)
    }
}

@Composable
fun AnimatedPawPrints(colors: RefugioColorPalette) {
    val pawPrints = remember { List(5) { PawPrint() } }

    pawPrints.forEach { pawPrint ->
        key(pawPrint.id) {
            AnimatedPawPrint(pawPrint, colors)
        }
    }
}
@Composable
fun AnimatedPawPrint(pawPrint: PawPrint, colors: RefugioColorPalette) {
    // Utilizamos animateFloatAsState para animar la posición vertical de la huella
    val yPosition by animateFloatAsState(
        targetValue = pawPrint.targetPosition.y,
        animationSpec = tween(durationMillis = 10000, easing = LinearEasing)
    )

    Icon(
        Icons.Default.Pets,
        contentDescription = "Paw Print",
        tint = colors.pawPrint.copy(alpha = 0.3f),
        modifier = Modifier
            .size(30.dp)
            .offset(x = pawPrint.initialPosition.x.dp, y = yPosition.dp) // Mueve la huella a lo largo de Y
    )
}


class PawPrint {
    val id = Math.random()
    val initialPosition = Offset(Math.random().toFloat() * 1000, -50f)
    val targetPosition = Offset(initialPosition.x, 1050f) // Mantenemos X fija y animamos solo Y
}

class RefugioColorPalette {
    val primary = Color(0xFF4CAF50)
    val secondary = Color(0xFF8BC34A)
    val background = Color(0xFFF1F8E9)
    val menuBackground = Color(0xFFE8F5E9)
    val menuItem = Color(0xFFC8E6C9)
    val menuItemSelected = Color(0xFF81C784)
    val onMenuItem = Color(0xFF33691E)
    val onMenuItemSelected = Color.White
    val onBackground = Color(0xFF33691E)
    val pawPrint = Color(0xFF33691E)

    fun toMaterialColors(): Colors {
        return lightColors(
            primary = primary,
            primaryVariant = primary,
            secondary = secondary,
            background = background,
            surface = background,
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = onBackground,
            onSurface = onBackground
        )
    }
}
