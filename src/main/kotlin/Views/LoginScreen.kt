package Views

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // UI de la pantalla de login
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
                .width(300.dp) // Ajusta el ancho de la pantalla de login
                .height(600.dp) // Ajusta la altura de la pantalla de login
        ) {
            Text(text = "Bienvenido a Amigos de Pata", style = MaterialTheme.typography.h4)

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Login", style = MaterialTheme.typography.h4)

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de texto para el nombre de usuario
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de texto para la contraseña
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de login
            Button(
                onClick = {
                    if (username == "admin" && password == "password") {
                        // Lógica simple de autenticación, cambiar a la pantalla principal
                        onLoginSuccess()
                    } else {
                        errorMessage = "Invalid username or password"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Login")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Mensaje de error
            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = MaterialTheme.colors.error)
            }
        }
    }
}