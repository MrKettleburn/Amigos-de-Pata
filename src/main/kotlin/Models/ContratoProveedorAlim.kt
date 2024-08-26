package Models

import java.time.LocalDate

data class ContratoProveedorAlim(
    val codigo: Int,
    val precio: Double,
    val costoUnit: Double,
    val descripcion: String,
    val idProv: Int,
    val nombreProv: String,
    val provinciaProv: String,
    val direccProv: String,
    val idServ: Int,
    val tipoAlim: String,
    val precioUnit: Double,
    val fechaInicio: LocalDate, //TIPO DE DATO FECHA
    val fechaFin: LocalDate, //TIPO DE DATO FECHA
    val fechaConcil: LocalDate //TIPO DE DATO FECHA
){
    override fun toString(): String{
        return "C:$codigo N:$nombreProv A:$tipoAlim"
    }
}
