//package Views
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//
//@Composable
//fun MainContent(colors: RefugioColorPalette) {
//    var selectedItem by remember { mutableStateOf("") }
//    var selectedSubItem by remember { mutableStateOf("") }
//
//    Row(modifier = Modifier.fillMaxSize()) {
//        // Sidebar con el menú expandible
//        AnimatedSideMenu(RefugioColorPalette(), selectedItem, selectedSubItem) { item, subItem ->
//            selectedItem = item
//            selectedSubItem = subItem
//        }
//
//        // Main Content del Dashboard
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.White)
//                .padding(16.dp)
//        ) {
//            when (selectedItem) {
//                "Contratos" -> {
//                    when (selectedSubItem) {
//                        "Veterinarios" -> Text("CONTRATOS VETERINARIOS")
//                        "Transporte" -> Text("CONTRATOS TRANSPORTES")
//                        "Proveedor de alimentos" -> Text("CONTRATOS PROVEEDOR DE ALIM")
//
//                    }
//                }
//                "Contratados" -> {
//                    when (selectedSubItem) {
//                        "Veterinarios" -> Text(" VETERINARIOS")
//                        "Transporte" -> Text(" TRANSPORTES")
//                        "Proveedor de alimentos" -> Text("PROVEEDOR DE ALIM")
//
//                    }
//                }
//                "Servicios" -> {
//                    when (selectedSubItem) {
//                        "Veterinarios" -> Text("Selecciona una opción dentro de Contratados")
//                        "Transporte" -> Text("SERVICIOS TRANSPORTE")
//                        "Proveedor de alimentos" -> Text("SERVICIOS PROVEEDOR DE ALIM")
//
//                    }
//                }
//                "Animales" -> Text("ANIMALES")
//
//            }
//        }
//    }
//}