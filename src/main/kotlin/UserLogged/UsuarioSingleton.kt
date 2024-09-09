package UserLogged

object UsuarioSingleton {
    var id: Int? = null
    var permiso: String? = null

    fun iniciarSesion(id: Int, permiso: String) {
        this.id = id
        this.permiso = permiso
    }
}
