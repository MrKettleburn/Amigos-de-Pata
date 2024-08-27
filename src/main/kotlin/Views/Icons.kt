package Views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

fun getIconForAttribute(attribute: String): ImageVector {
    return when (attribute) {
        "Clínica" -> Icons.Default.LocalHospital
        "Clínica del Veterinario" -> Icons.Default.LocalHospital
        "Código" -> Icons.Default.Badge
        "Código Contrato" -> Icons.Default.Description
        "Contraseña" -> Icons.Default.Lock
        "Descripción" -> Icons.Default.Textsms
        "Días en adopción" -> Icons.Default.HourglassEmpty
        "Días en Refugio" -> Icons.Default.Numbers
        "Dirección" -> Icons.Default.LocationOn
        "Dirección del Contratado" -> Icons.Default.LocationOn
        "Edad" -> Icons.Default.Cake
        "Email" -> Icons.Default.Email
        "Especialidad" -> Icons.Default.MedicalServices
        "Especialidad del Veterinario" -> Icons.Default.MedicalServices
        "Especie" -> Icons.Default.Pets
        "Fecha" -> Icons.Default.CalendarToday
        "Fecha de Ingreso" -> Icons.Default.CalendarToday
        "Fecha de Inicio" -> Icons.Default.CalendarMonth
        "Fecha de Fin" -> Icons.Default.CalendarMonth
        "Fecha de Conciliación" -> Icons.Default.CalendarMonth
        "Hora" -> Icons.Default.Timer
        "Modalidad" -> Icons.Default.Mode
        "Modalidad del Servicio" -> Icons.Default.Mode
        "Nombre" -> Icons.Default.Person
        "Nombre del Adoptante" -> Icons.Default.Person
        "Nombre del Contratado" -> Icons.Default.Person
        "Peso" -> Icons.Default.FitnessCenter
        "Precio" -> Icons.Default.AttachMoney
        "Precio de adopción" -> Icons.Default.AttachMoney
        "Precio Unitario del Servicio" -> Icons.Default.AttachMoney
        "Provincia" -> Icons.Default.LocationCity
        "Provincia del Contratado" -> Icons.Default.LocationCity
        "Raza" -> Icons.Default.Pets
        "Teléfono" -> Icons.Default.Phone
        "Tipo de Alimento" -> Icons.Default.Fastfood
        "Tipo" -> Icons.Default.Bloodtype
        "Vehículo" -> Icons.Default.LocalShipping

        else -> Icons.Default.Info
    }
}
