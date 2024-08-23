package Models

data class ServVeterinario (
    val codigo: Int,
    val precioUni: Double,
    val modalidad: String
){
    override fun toString(): String {
        return "C:$codigo PU:$$precioUni M:$modalidad"

    }
}