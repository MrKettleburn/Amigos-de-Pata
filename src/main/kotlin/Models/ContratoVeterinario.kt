package Models

import java.time.LocalDate

data class ContratoVeterinario(
    val codigo: Int,
    val precio: Double,
    val descripcion: String,
    val nombreVet: String,
    val clinicaVet: String,
    val provinciaVet: String,
    val direccVet: String,
    val especialidad: String,
    val modalidadServVet: String,
    val precioUnit: Double,
    val fechaInicio: LocalDate, //TIPO DE DATO FECHA
    val fechaFin: LocalDate, //TIPO DE DATO FECHA
    val fechaConcil: LocalDate //TIPO DE DATO FECHA
)
