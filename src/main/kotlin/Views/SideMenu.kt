package Views

import UserLogged.UsuarioSingleton
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@Composable
fun AnimatedSideMenu(
    colors: RefugioColorPalette,
    selectedItem: String,
    selectedSubItem: String,
    onSelectionChanged: (String, String) -> Unit,
    onLogout: () -> Unit
) {
    val scrollState = rememberScrollState()

    // Controlar la visibilidad del diálogo de donación
    var showDonacionDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .width(250.dp)
            .fillMaxHeight()
            .background(colors.menuBackground)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {

        Text(
            "Gestión",
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            color = colors.onMenuItem,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ExpandableMenuItem(
            "Contratos",
            colors,
            selectedItem,
            selectedSubItem,
            onSelectionChanged,
            Icons.Default.Description
        )
        ExpandableMenuItem(
            "Contratados",
            colors,
            selectedItem,
            selectedSubItem,
            onSelectionChanged,
            Icons.Default.People
        )
        ExpandableMenuItem(
            "Servicios",
            colors,
            selectedItem,
            selectedSubItem,
            onSelectionChanged,
            Icons.Default.Build
        )
        ExpandableMenuItem(
            "Animales",
            colors,
            selectedItem,
            selectedSubItem,
            onSelectionChanged,
            Icons.Default.Pets
        )

        MenuItem("Donaciones", colors, selectedItem, selectedSubItem, Icons.Default.AttachMoney) {
            onSelectionChanged("Donaciones", "")
        }

        MenuItem("Usuarios", colors, selectedItem, selectedSubItem, Icons.Default.Person) {
            onSelectionChanged("Usuarios", "")
        }

        Spacer(modifier = Modifier.height(16.dp))

        ExpandableMenuItem("Informes", colors, selectedItem, selectedSubItem, onSelectionChanged, Icons.Default.Assessment)

        Spacer(modifier = Modifier.weight(1f))

        // Botón de Donar
        Button(
            onClick = { showDonacionDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Icon(Icons.Default.Favorite, contentDescription = "Donar") // Icono sugerente para donaciones
            Spacer(modifier = Modifier.width(8.dp))
            Text("Donar")
        }

        // Botón de Cerrar Sesión
        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar Sesión")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cerrar Sesión")
        }
    }

    // Mostrar diálogo de donación si showDonacionDialog es true
    if (showDonacionDialog) {
        AddDonacionDialog(
            colors = colors,
            onDismissRequest = { showDonacionDialog = false },
            onDonacionAdded = { monto ->
                // Manejar la donación agregada
                println("Donación añadida: $monto")
                showDonacionDialog = false
            }
        )
    }
}
@Composable
fun AddDonacionDialog(
    colors: RefugioColorPalette,
    onDismissRequest: () -> Unit,
    onDonacionAdded: (Double) -> Unit
) {
    var monto by remember { mutableStateOf(0.0) }
    var cantidadInput by remember { mutableStateOf(TextFieldValue("0")) }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("¿Cuánto desea donar?", style = MaterialTheme.typography.h5)

                Spacer(modifier = Modifier.height(16.dp))

                NumberPickerComponent(
                    value = cantidadInput,
                    onValueChange = { value ->
                        cantidadInput = value
                        monto = value.text.toDoubleOrNull() ?: 0.0
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        if (monto > 0.0) {
                            onDonacionAdded(monto)
                        } else {
                            println("ERROR: El monto debe ser mayor a 0")
                        }
                    }) {
                        Text("Donar")
                    }
                }
            }
        }
    }
}

@Composable
fun NumberPickerComponent(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        IconButton(onClick = {
            val newValue = (value.text.toIntOrNull() ?: 0) - 100
            onValueChange(TextFieldValue(newValue.toString()))
        }) {
            Icon(Icons.Default.ArrowCircleDown, contentDescription = "Restar 100")
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = {
            val newValue = (value.text.toIntOrNull() ?: 0) + 100
            onValueChange(TextFieldValue(newValue.toString()))
        }) {
            Icon(Icons.Default.Add, contentDescription = "Sumar 100")
        }
    }
}
@Composable
fun ExpandableMenuItem(
    title: String,
    colors: RefugioColorPalette,
    selectedItem: String,
    selectedSubItem: String,
    onItemSelected: (String, String) -> Unit,
    icon: ImageVector
) {
    var expanded by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(if (expanded) 180f else 0f)

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    expanded = !expanded
                    onItemSelected(title, "")
                }
                .background(if (selectedItem == title) colors.menuItemSelected else Color.Transparent)
                .padding(vertical = 8.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selectedItem == title) colors.onMenuItemSelected else colors.onMenuItem,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = title,
                color = if (selectedItem == title) colors.onMenuItemSelected else colors.onMenuItem,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = "Expand",
                modifier = Modifier.rotate(rotationState),
                tint = if (selectedItem == title) colors.onMenuItemSelected else colors.onMenuItem
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column {
                when (title) {
                    "Animales" -> {
                        SubMenuItem("En Refugio", colors, selectedItem, selectedSubItem, title, Icons.Default.Home) { onItemSelected(title, it) }
                        SubMenuItem("En Adopción", colors, selectedItem, selectedSubItem, title, Icons.Default.Favorite) { onItemSelected(title, it) }
                    }
                    "Informes" -> {
                        SubMenuItem("Listado de Contratos Conciliados con Veterinarios", colors, selectedItem, selectedSubItem, title, Icons.Default.List) { onItemSelected(title, it) }
                        SubMenuItem("Listado de Contratos de Proveedores de Alimentos", colors, selectedItem, selectedSubItem, title, Icons.Default.List) { onItemSelected(title, it) }
                        SubMenuItem("Listado de Contratos de Servicios Complementarios", colors, selectedItem, selectedSubItem, title, Icons.Default.List) { onItemSelected(title, it) }
                        SubMenuItem("Listado de Veterinarios Activos", colors, selectedItem, selectedSubItem, title, Icons.Default.List) { onItemSelected(title, it) }
                        SubMenuItem("Programa de Actividades de Cuidado para un Animal", colors, selectedItem, selectedSubItem, title, Icons.Default.List) { onItemSelected(title, it) }
                        SubMenuItem("Plan de Ingresos por Concepto de Adopciones y Donaciones", colors, selectedItem, selectedSubItem, title, Icons.Default.List) { onItemSelected(title, it) }
                    }
                    "Contratos", "Contratados", "Servicios" -> {
                        SubMenuItem("Veterinarios", colors, selectedItem, selectedSubItem, title, Icons.Default.MedicalServices) { onItemSelected(title, it) }
                        SubMenuItem("Transporte", colors, selectedItem, selectedSubItem, title, Icons.Default.LocalShipping) { onItemSelected(title, it) }
                        SubMenuItem("Proveedor de alimentos", colors, selectedItem, selectedSubItem, title, Icons.Default.Fastfood) { onItemSelected(title, it) }
                    }
                }
            }
        }
    }
}

@Composable
fun SubMenuItem(
    title: String,
    colors: RefugioColorPalette,
    selectedItem: String,
    selectedSubItem: String,
    parentTitle: String,
    icon: ImageVector,
    onItemSelected: (String) -> Unit
) {
    var showTooltip by remember { mutableStateOf(false) }

    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemSelected(title) }
                .background(if (selectedItem == parentTitle && selectedSubItem == title) colors.menuItemSelected else Color.Transparent)
                .padding(vertical = 8.dp, horizontal = 24.dp)
                .onPointerEnter { showTooltip = true }
                .onPointerExit { showTooltip = false }
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selectedItem == parentTitle && selectedSubItem == title) colors.onMenuItemSelected else colors.onMenuItem,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = title.take(20),
                color = if (selectedItem == parentTitle && selectedSubItem == title) colors.onMenuItemSelected else colors.onMenuItem
            )
        }

        if (showTooltip) {
            Popup(
                alignment = Alignment.TopStart,
                offset = IntOffset(0, -40),
                properties = PopupProperties(focusable = false)
            ) {
                Surface(
                    color = MaterialTheme.colors.surface,
                    shape = MaterialTheme.shapes.small,
                    elevation = 4.dp,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(
                        text = title,
                        modifier = Modifier.padding(8.dp),
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun MenuItem(
    title: String,
    colors: RefugioColorPalette,
    selectedItem: String,
    selectedSubItem: String,
    icon: ImageVector,
    onItemSelected: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemSelected(title) }
            .background(if (selectedItem == title && selectedSubItem.isEmpty()) colors.menuItemSelected else Color.Transparent)
            .padding(vertical = 8.dp, horizontal = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selectedItem == title && selectedSubItem.isEmpty()) colors.onMenuItemSelected else colors.onMenuItem,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = title,
            color = if (selectedItem == title && selectedSubItem.isEmpty()) colors.onMenuItemSelected else colors.onMenuItem
        )
    }
}

@Composable
fun Modifier.onPointerEnter(onEnter: () -> Unit): Modifier = pointerInput(Unit) {
    awaitPointerEventScope {
        while (true) {
            val event = awaitPointerEvent()
            if (event.type == PointerEventType.Enter) {
                onEnter()
            }
        }
    }
}

@Composable
fun Modifier.onPointerExit(onExit: () -> Unit): Modifier = pointerInput(Unit) {
    awaitPointerEventScope {
        while (true) {
            val event = awaitPointerEvent()
            if (event.type == PointerEventType.Exit) {
                onExit()
            }
        }
    }
}
