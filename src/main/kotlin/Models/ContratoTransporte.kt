package Models

import java.time.LocalDate

data class ContratoTransporte(
    val codigo: Int,
    val precio: Double,
    val costoUnit: Double,
    val descripcion: String,
    val idTrans: Int,
    val nombreTrans: String,
    val provinciaTrans: String,
    val direccionTrans: String,
    val idServ: Int,
    val vehiculo: String,
    val precioUnit: Double,
    val fechaInicio: LocalDate,
    val fechaFin: LocalDate,
    val fechaConcil: LocalDate,
    val estado: String
){
    override fun toString(): String{
        return "C:$codigo N:$nombreTrans V:$vehiculo"
    }
}
