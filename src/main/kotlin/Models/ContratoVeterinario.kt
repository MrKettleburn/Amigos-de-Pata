package Models

import java.time.LocalDate

data class ContratoVeterinario(
    val codigo: Int,
    val precio: Double,
    val costoUnit: Double,
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
    val fechaInicio: LocalDate,
    val fechaFin: LocalDate,
    val fechaConcil: LocalDate,
    val estado: String
){
    override fun toString(): String{
        return "C:$codigo N:$nombreVet M:$modalidadServVet EV:$especialidad"
    }
}
