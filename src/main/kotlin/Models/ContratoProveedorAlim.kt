package Models

data class ContratoProveedorAlim(
    val codigo: Int,
    val precio: Double,
    val descripcion: String, //Aqui vamos a meter el tipo de alimento
    val nombreProv: String,
    val provinciaProv: String,
    val direccProv: String,
    val fechaInicio: String, //TIPO DE DATO FECHA
    val fechaFin: String, //TIPO DE DATO FECHA
    val fechaConcil: String //TIPO DE DATO FECHA
)
