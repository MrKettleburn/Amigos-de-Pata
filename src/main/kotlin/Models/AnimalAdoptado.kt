package Models

import java.time.LocalDate

data class AnimalAdoptado(
    val codigo: Int,
    val nombre: String,
    val especie: String,
    val raza: String,
    val edad: Int,
    val peso: Double,
    val cantDias: Int,
    val fecha_ingreso: LocalDate,
    val precioAdop: Double
    //DATOS DEL ADOPTANTE TAL VEZ
)