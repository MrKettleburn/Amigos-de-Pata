package Models

import java.time.LocalDate

data class Contrato(
    val codigo: Int,
    val precio: Double,
    val fechaInicio: LocalDate, //TIPO DE DATO FECHA
    val fechaFin: LocalDate, //TIPO DE DATO FECHA
    val fechaConcil: LocalDate //TIPO DE DATO FECHA
)
