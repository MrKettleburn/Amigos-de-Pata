package Models

import java.time.LocalDate

data class ReporteVeterinariosActivosObj(
    val fechaInicio: LocalDate,
    val fechaFin: LocalDate,
    val idVet: Int,
    val nombre: String,
    val clinica: String,
    val provincia: String,
    val especialidad: String,
    val telefono: String,
    val email: String,
    val modalidad: String
)
