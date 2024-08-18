package Views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import kotlin.math.PI
import kotlin.math.sin

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
                "Refugio \"Amigos de la Pata\"",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(Modifier.width(16.dp))
            KotlinWavePattern(colors, Modifier.weight(1f))
            Spacer(Modifier.width(16.dp))
            AnimatedLogo()
        }
    }
}

@Composable
fun KotlinWavePattern(colors: RefugioColorPalette, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = modifier.height(50.dp)) {
        val width = size.width
        val height = size.height
        val path = Path()
        val amplitude = height / 4
        val step = width / 40

        for (x in 0..width.toInt() step step.toInt()) {
            val y = sin(x / width * 4 * PI + phase) * amplitude + height / 2
            if (x == 0) {
                path.moveTo(x.toFloat(), y.toFloat())
            } else {
                path.lineTo(x.toFloat(), y.toFloat())
            }
        }

        drawPath(
            path = path,
            color = colors.onMenuItemSelected,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
        )
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