package Models

data class ProveedorDeAlimentos(
    val codigo: Int,
    val nombre: String,
    val email: String,
    val provincia: String,
    val direccion: String,
    val telefono: String
){
    override fun toString(): String {
        return "C:$codigo N:$nombre P:$provincia"
    }
}
