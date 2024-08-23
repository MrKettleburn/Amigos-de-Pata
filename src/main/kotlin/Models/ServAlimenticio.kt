package Models

data class ServAlimenticio (
    val codigo: Int,
    val precioUni: Double,
    val tipoAlimento: String
){
    override fun toString(): String {
        return "C:$codigo PU(kg):$$precioUni A:$tipoAlimento"

    }
}