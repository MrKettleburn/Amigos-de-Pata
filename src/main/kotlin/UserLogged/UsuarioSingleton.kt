package UserLogged

// UsuarioActivo.kt (dentro de una nueva carpeta llamada "global" o "utils")
object UsuarioSingleton {
    private var usuarioActual: Usuario? = null

    fun iniciarSesion(usuario: Usuario) {
        usuarioActual = usuario
    }

    fun cerrarSesion() {
        usuarioActual = null
    }

    fun getUsuarioActual(): Usuario? = usuarioActual
}