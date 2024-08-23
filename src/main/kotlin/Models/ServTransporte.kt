package Models

data class ServTransporte(
    val codigo: Int,
    val precioUni: Double,
    val vehiculo: String
){
    override fun toString(): String {
        return "C:$codigo PU(km):$$precioUni V:$vehiculo"

    }
}
