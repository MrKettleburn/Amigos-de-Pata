package Models

import java.time.LocalDate

data class ContratoTransporte(
    val codigo: Int,
    val precio: Double,
    val descripcion: String,
    val nombreTrans: String,
    val provinciaTrans: String,
    val direccionTrans: String,
    val vehiculo: String,
    val precioUnit: Double,
    val fechaInicio: LocalDate, //TIPO DE DATO FECHA
    val fechaFin: LocalDate, //TIPO DE DATO FECHA
    val fechaConcil: LocalDate //TIPO DE DATO FECHA
)
