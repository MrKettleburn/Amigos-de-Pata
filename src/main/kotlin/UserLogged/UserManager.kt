package UserLogged

object UserManager {
    private lateinit var user: User


    fun getUserPermission(): String{
        return user.permiso
    }
}