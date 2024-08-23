package Models

data class Transporte (
    val codigo: Int,
    val nombre: String,
    val email: String,
    val provincia: String,
    val direccion: String,
    val telefono: String
){
    override fun toString(): String {
        return "C:$codigo P:$provincia"
    }
}