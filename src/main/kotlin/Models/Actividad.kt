package Models

import java.time.LocalDate
import java.time.LocalTime

data class Actividad(
    val codigo: Int,
    val codigoAnim: Int,
    val fecha: LocalDate,
    val hora: LocalTime,
    val tipo: String,
    val codigoContr: Int,
    val tipoContrato: String,
    val descrip: String,
    val costo: Double
)
