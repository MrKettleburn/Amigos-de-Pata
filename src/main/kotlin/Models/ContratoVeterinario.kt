package Models

import java.time.LocalDate

data class ContratoVeterinario(
    val codigo: Int,
    val precio: Double,
    val descripcion: String,
    val idVet: Int,
    val nombreVet: String,
    val clinicaVet: String,
    val provinciaVet: String,
    val direccVet: String,
    val especialidad: String,
    val idServ: Int,
    val modalidadServVet: String,
    val precioUnit: Double,
    val fechaInicio: LocalDate, //TIPO DE DATO FECHA
    val fechaFin: LocalDate, //TIPO DE DATO FECHA
    val fechaConcil: LocalDate //TIPO DE DATO FECHA
)
