package Views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

fun getIconForAttribute(attribute: String): ImageVector {
    return when (attribute) {
        "Código" -> Icons.Default.Badge
        "Nombre" -> Icons.Default.Person
        "Nombre del Contratado" -> Icons.Default.Person
        "Especie" -> Icons.Default.Pets
        "Raza" -> Icons.Default.Pets
        "Edad" -> Icons.Default.Cake
        "Peso" -> Icons.Default.FitnessCenter
        "Días en Refugio" -> Icons.Default.Numbers
        "Fecha de Ingreso" -> Icons.Default.CalendarToday
        "Fecha" -> Icons.Default.CalendarToday
        "Tipo" -> Icons.Default.Bloodtype
        "Hora" -> Icons.Default.Timer
        "Descripción" -> Icons.Default.Textsms
        "Código Contrato" -> Icons.Default.Description
        "Precio" -> Icons.Default.AttachMoney
        "Precio Unitario del Servicio" -> Icons.Default.AttachMoney
        "Modalidad del Servicio" -> Icons.Default.Mode
        "Especialidad del Veterinario" -> Icons.Default.TypeSpecimen
        "Clínica del Veterinario" -> Icons.Default.LocalHospital
        "Provincia del Contratado" -> Icons.Default.LocationCity
        "Dirección del Contratado" -> Icons.Default.LocationOn
        "Fecha de Inicio" -> Icons.Default.CalendarMonth
        "Fecha de Fin" -> Icons.Default.CalendarMonth
        "Fecha de Conciliación" -> Icons.Default.CalendarMonth
        else -> Icons.Default.Info
    }
}
