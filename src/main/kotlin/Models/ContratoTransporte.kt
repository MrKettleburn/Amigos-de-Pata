package Models

data class ContratoTransporte(
    val codigo: Int,
    val precio: Double,
    val descripcion: String,
    val nombreTrans: String,
    val provincia: String,
    val fechaInicio: String, //TIPO DE DATO FECHA
    val fechaFin: String, //TIPO DE DATO FECHA
    val fechaConcil: String //TIPO DE DATO FECHA
)
