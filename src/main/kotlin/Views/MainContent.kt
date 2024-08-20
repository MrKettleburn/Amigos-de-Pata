package Views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun MainContent(colors: RefugioColorPalette) {
    var selectedItem by remember { mutableStateOf("") }
    var selectedSubItem by remember { mutableStateOf("") }

    Row(modifier = Modifier.fillMaxSize()) {
        // Sidebar con el menú expandible
        AnimatedSideMenu(colors, selectedItem, selectedSubItem) { item, subItem ->
            selectedItem = item
            selectedSubItem = subItem
        }

        // Main Content del Dashboard
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(255, 249, 234))
                .padding(16.dp)
        ) {
            if (selectedItem.isEmpty() || (selectedItem in listOf("Contratos", "Contratados", "Servicios") && selectedSubItem.isEmpty())) {
                // Contenido predeterminado para ambientar cuando no hay selección
                DefaultContent(colors)
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    when (selectedItem) {
                        "Contratos" -> {
                            when (selectedSubItem) {
                                "Veterinarios" -> Text("CONTRATOS VETERINARIOS")
                                "Transporte" -> Text("CONTRATOS TRANSPORTES")
                                "Proveedor de alimentos" -> Text("CONTRATOS PROVEEDOR DE ALIM")
                            }
                        }

                        "Contratados" -> {
                            when (selectedSubItem) {
                                "Veterinarios" -> Text("VETERINARIOS")
                                "Transporte" -> Text("TRANSPORTES")
                                "Proveedor de alimentos" -> Text("PROVEEDOR DE ALIM")
                            }
                        }

                        "Servicios" -> {
                            when (selectedSubItem) {
                                "Veterinarios" -> Text("Selecciona una opción dentro de Contratados")
                                "Transporte" -> Text("SERVICIOS TRANSPORTE")
                                "Proveedor de alimentos" -> Text("SERVICIOS PROVEEDOR DE ALIM")
                            }
                        }

                        "Animales" -> AnimalesMostrar(colors, selectedItem, selectedSubItem)
                    }
                }
            }
        }
    }
    }

    @Composable
    fun DefaultContent(colors: RefugioColorPalette) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Cargar la imagen desde los recursos
            Image(
                painter = painterResource("photo_2024-08-20_19-13-28.jpg"),
                contentDescription = "Fondo de refugio de animales",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Contenido encima de la imagen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Bienvenido a la Gestión de Refugio",
                    style = MaterialTheme.typography.h4,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }