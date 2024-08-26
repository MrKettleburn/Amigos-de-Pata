package Models

import java.time.LocalDate
import java.time.LocalTime

data class ActividadReporte(
    val codigo: Int,
    val codigoAnim: Int,
    val fecha: LocalDate,
    val hora: LocalTime,
    val tipo: String,
    val codigoContr: Int,
    val detalles: String,
    val descrip: String,
    val costo: Double
)
