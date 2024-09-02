package Views

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun LoginScreen(colors: RefugioColorPalette, onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(colors.primary, colors.secondary)
                )
            )
    ) {
        KotlinWavePatternLogin(colors, Modifier.fillMaxSize())

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            AnimatedLogo(colors)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Amigos de Pata",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier
                    .width(300.dp)
                    .clip(RoundedCornerShape(16.dp)),
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Usuario") },
                        leadingIcon = { Icon(getIconForAttribute("Nombre"), contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.menuItemSelected,
                            cursorColor = colors.primary
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(getIconForAttribute("Contraseña"), contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.menuItemSelected,
                            cursorColor = colors.primary
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (username == "admin" && password == "password") {
                                onLoginSuccess()
                            } else {
                                errorMessage = "Usuario o contraseña inválidos"
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(backgroundColor = colors.secondary)
                    ) {
                        Text("Iniciar Sesión", color = colors.onMenuItemSelected)
                    }

                    if (errorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = errorMessage, color = MaterialTheme.colors.error)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Loguearse Aquí :D",
                        color = colors.secondary,
                        modifier = Modifier.clickable { showDialog = true }
                    )
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(
                        text = "Introducir Datos",
                        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold) // Más negrito y marcado
                    )
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Usuario") },
                            leadingIcon = { Icon(getIconForAttribute("Nombre"), contentDescription = null) },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = colors.primary,
                                unfocusedBorderColor = colors.menuItemSelected,
                                cursorColor = colors.primary
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Contraseña") },
                            leadingIcon = { Icon(getIconForAttribute("Contraseña"), contentDescription = null) },
                            visualTransformation = PasswordVisualTransformation(),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = colors.primary,
                                unfocusedBorderColor = colors.menuItemSelected,
                                cursorColor = colors.primary
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            if (username == "admin" && password == "password") {
                                onLoginSuccess()
                            } else {
                                errorMessage = "Usuario o contraseña inválidos"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = colors.primary)
                    ) {
                        Text("Aceptar", color = colors.onMenuItemSelected)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancelar", color = colors.primary)
                    }
                },
                backgroundColor = colors.background
            )
        }
    }
}

@Composable
fun AnimatedLogo(colors: RefugioColorPalette) {
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
        modifier = Modifier
            .size(100.dp)
            .rotate(rotation),
        tint = Color.White
    )
}

@Composable
fun KotlinWavePatternLogin(colors: RefugioColorPalette, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val path = Path()
        val amplitude = height / 8
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
            color = colors.onMenuItemSelected.copy(alpha = 0.3f),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
        )
    }
}
