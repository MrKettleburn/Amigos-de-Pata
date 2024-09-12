package Views

import UserLogged.UsuarioDB
import UserLogged.UsuarioDB.verificarCredenciales
import UserLogged.UsuarioDB.verificarUsuarioyContrasenia
import UserLogged.UsuarioDB.verificarUsuarioyContraseniaDialog
import UserLogged.UsuarioSingleton
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.input.key.*
import androidx.compose.ui.focus.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.*
import java.time.format.TextStyle

@Composable
fun LoginScreen(colors: RefugioColorPalette, onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var nombre by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var dialogPasswordVisible by remember { mutableStateOf(false) }
    var notificationState by remember { mutableStateOf<Pair<String, Boolean>?>(null) }
    var errorMessage by remember { mutableStateOf("") }
    var dialogErrorMessage by remember { mutableStateOf("") }
    var nombreError by remember { mutableStateOf(false) }
    var usernameError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var dialogGeneralError by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current
    val dialogFocusRequester = remember { FocusRequester() }

    @Composable
    fun AnimatedNotification(message: String, isSuccess: Boolean) {
        var isVisible by remember { mutableStateOf(false) }

        LaunchedEffect(message) {
            isVisible = true
            delay(3000)
            isVisible = false
            notificationState = null
        }

        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
        ) {
            Card(
                shape = RoundedCornerShape(8.dp),
                elevation = 8.dp,
                backgroundColor = if (isSuccess) colors.secondary else Color.Red.copy(alpha = 0.8f),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.8f)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isSuccess) Icons.Default.Check else Icons.Default.Warning,
                        contentDescription = if (isSuccess) "Éxito" else "Error",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = message,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    fun showNotification(message: String, isSuccess: Boolean) {
        notificationState = message to isSuccess
    }

    fun attemptLogin() {
        coroutineScope.launch(Dispatchers.IO) {
            val usuarioExiste = verificarUsuarioyContrasenia(username, password)
            if (usuarioExiste) {
                val resultado = verificarCredenciales(username, password)
                if (resultado != null) {
                    val (id, permiso) = resultado
                    UsuarioSingleton.iniciarSesion(id, permiso)
                    showNotification("Inicio de sesión exitoso", true)
                    delay(1800)
                    onLoginSuccess()
                } else {
                    showNotification("Usuario o contraseña incorrectos", false)
                }
            } else {
                showNotification("El usuario no existe. Regístrese primero.", false)
            }
        }
    }

    fun attemptRegister() {
        coroutineScope.launch(Dispatchers.IO) {
            val usuarioExiste = verificarUsuarioyContraseniaDialog(username, password)
            if (usuarioExiste) {
                withContext(Dispatchers.IO) {
                    showDialog = false // Cerrar el diálogo antes de mostrar la notificación
                    delay(100) // Pequeña pausa para asegurar que el diálogo se cierre
                    showNotification("El usuario ya existe. Intente con otras credenciales.", false)
                    delay(1500)
                    showDialog = true
                }
            } else {
                UsuarioDB.createUsuarioAdoptante(nombre, username, password)
                withContext(Dispatchers.IO) {
                    showDialog = false
                    showNotification("Cuenta creada con éxito. Bienvenido al refugio Amigos de Pata.", true)
                }
                delay(1500)
                withContext(Dispatchers.IO) {
                    username = username
                    password = password
                }
            }
        }
    }

    fun validateAndRegister() {
        nombreError = nombre.isBlank()
        usernameError = username.isBlank()
        passwordError = password.isBlank()

        if (nombre.isBlank() || username.isBlank() || password.isBlank()) {
            dialogGeneralError = "Deben llenarse todos los campos"
        } else {
            dialogGeneralError = ""
            attemptRegister()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(colors.primary, colors.secondary)
                )
            )
            .onKeyEvent { keyEvent ->
                if (keyEvent.key == Key.Enter && keyEvent.type == KeyEventType.KeyDown) {
                    if (!showDialog) {
                        attemptLogin()
                    }
                    true
                } else {
                    false
                }
            }
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
                        leadingIcon = { Icon(getIconForAttribute("Usuario"), contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.menuItemSelected,
                            cursorColor = colors.primary
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(getIconForAttribute("Contraseña"), contentDescription = null) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.menuItemSelected,
                            cursorColor = colors.primary
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { attemptLogin() })
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { attemptLogin() },
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
                        text = "Registrarse Aquí :D",
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
                        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
                    )
                },
                text = {
                    Column(
                        modifier = Modifier.onKeyEvent { keyEvent ->
                            if (keyEvent.key == Key.Enter && keyEvent.type == KeyEventType.KeyDown) {
                                validateAndRegister()
                                true
                            } else {
                                false
                            }
                        }
                    ) {
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = {
                                nombre = it
                                nombreError = false
                                if (dialogGeneralError.isNotEmpty()) dialogGeneralError = ""
                            },
                            label = { Text("Nombre Completo") },
                            leadingIcon = { Icon(getIconForAttribute("Nombre"), contentDescription = null) },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = if (nombreError) Color.Red else colors.primary,
                                unfocusedBorderColor = if (nombreError) Color.Red else colors.menuItemSelected,
                                cursorColor = colors.primary
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                            singleLine = true,
                            isError = nombreError
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = username,
                            onValueChange = {
                                username = it
                                usernameError = false
                                if (dialogGeneralError.isNotEmpty()) dialogGeneralError = ""
                            },
                            label = { Text("Usuario") },
                            leadingIcon = { Icon(getIconForAttribute("Usuario"), contentDescription = null) },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = if (usernameError) Color.Red else colors.primary,
                                unfocusedBorderColor = if (usernameError) Color.Red else colors.menuItemSelected,
                                cursorColor = colors.primary
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                            singleLine = true,
                            isError = usernameError
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                passwordError = false
                                if (dialogGeneralError.isNotEmpty()) dialogGeneralError = ""
                            },
                            label = { Text("Contraseña") },
                            leadingIcon = { Icon(getIconForAttribute("Contraseña"), contentDescription = null) },
                            trailingIcon = {
                                IconButton(onClick = { dialogPasswordVisible = !dialogPasswordVisible }) {
                                    Icon(
                                        imageVector = if (dialogPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = if (dialogPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                                    )
                                }
                            },
                            visualTransformation = if (dialogPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = if (passwordError) Color.Red else colors.primary,
                                unfocusedBorderColor = if (passwordError) Color.Red else colors.menuItemSelected,
                                cursorColor = colors.primary
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = { validateAndRegister() }),
                            singleLine = true,
                            isError = passwordError
                        )

                        if (dialogGeneralError.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = dialogGeneralError, color = Color.Red)
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { validateAndRegister() },
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

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(Float.MAX_VALUE)
        ) {
            notificationState?.let { (message, isSuccess) ->
                AnimatedNotification(message, isSuccess)
            }
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
