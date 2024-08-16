package Models

data class Contrato(
    val codigo: Int,
    val precio: Double,
    val fechaInicio: String, //TIPO DE DATO FECHA
    val fechaFin: String, //TIPO DE DATO FECHA
    val fechaConcil: String //TIPO DE DATO FECHA
)
