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
    val fechaInicio: LocalDate, //TIPO DE DATO FECHA
    val fechaFin: LocalDate, //TIPO DE DATO FECHA
    val fechaConcil: LocalDate //TIPO DE DATO FECHA
){
    override fun toString(): String{
        return "C:$codigo N:$nombreTrans V:$vehiculo"
    }
}
