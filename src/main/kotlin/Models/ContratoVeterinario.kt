package Models

data class ContratoVeterinario(
    val codigo: Int,
    val precio: Double,
    val descripcion: String,
    val nombreVet: String,
    val clinicaVet: String,
    val provinciaVet: String,
    val direccVet: String,
    val especialidad: String,
    val fechaInicio: String, //TIPO DE DATO FECHA
    val fechaFin: String, //TIPO DE DATO FECHA
    val fechaConcil: String //TIPO DE DATO FECHA
)
