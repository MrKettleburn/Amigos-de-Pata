package Views

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Offset

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
            .offset(x = pawPrint.initialPosition.x.dp, y = yPosition.dp)
    )
}

class PawPrint {
    val id = Math.random()
    val initialPosition = Offset(Math.random().toFloat() * 1000, -50f)
    val targetPosition = Offset(initialPosition.x, 1050f)
}