package Models

data class Veterinario (
    val codigo: Int,
    val nombre: String,
    val email: String,
    val provincia: String,
    val direccion: String,
    val telefono: String,
    val especialidad: String,
    val clinica: String
){
    override fun toString(): String {
        return "C:$codigo E:$especialidad P:$provincia"
    }
}