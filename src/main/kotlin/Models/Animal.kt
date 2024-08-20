package Models

import java.time.LocalDate

data class Animal (
    val codigo: Int,
    val nombre: String,
    val especie: String,
    val raza: String,
    val edad: Int,
    val peso: Double,
    val cantDias: Int,
    val fecha_ingreso: LocalDate
)