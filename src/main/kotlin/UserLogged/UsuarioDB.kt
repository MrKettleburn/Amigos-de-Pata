package UserLogged

import Database.Database
import Models.Adoptante
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.Types

object UsuarioDB {
    suspend fun getUsuariosFilter(
        permiso: String?
    ): List<Usuario> = withContext(Dispatchers.IO) {
        val usuarios = mutableListOf<Usuario>()
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT * FROM buscar_usuarios_por_permiso_filter(?)"
        )

        statement.setString(1, permiso)

        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            usuarios.add(
                Usuario(
                    codigo = resultSet.getInt("id_usuario"),
                    usuario = resultSet.getString("nombre_usuario"),
                    contrasenia = resultSet.getString("contrasena"),
                    permiso = resultSet.getString("permiso")
                )
            )
        }
        resultSet.close()
        statement.close()
        dbConnection.close()
        usuarios
    }

    suspend fun createUsuario(user: String, pass: String, rol:String): Int = withContext(Dispatchers.IO) {
        var nuevoId = -1
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT insertar_usuario(?, ?, ?)"
        )

        statement.setString(1, user)
        statement.setString(2, pass)
        statement.setString(3, rol)

        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            nuevoId = resultSet.getInt(1)
        }

        resultSet.close()
        statement.close()
        dbConnection.close()
        nuevoId
    }

    suspend fun updateUsuario(idUsuario: Int, nombreUsuario: String, contrasena: String, permiso: String): Boolean = withContext(Dispatchers.IO) {

        val dbConnection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT actualizar_usuario(?,?,?,?)"
        )

        statement.setInt(1, idUsuario)
        statement.setString(2, nombreUsuario)
        statement.setString(3, contrasena)
        statement.setString(4, permiso)

        var retorno: Boolean = true
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            val resultado: Boolean = resultSet.getBoolean(1)
            if (!resultado) {
                println("Error al actualizar el usuario. Intente nuevamente.")
                retorno = false
            }
        }

        dbConnection.close()
        retorno
    }

    suspend fun createUsuarioAdoptante(name: String, user: String, pass: String): Int = withContext(Dispatchers.IO) {
        var nuevoId = -1
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT insertar_usuario_adoptante(?, ?, ?)"
        )

        statement.setString(1, name)
        statement.setString(2, user)
        statement.setString(3, pass)

        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            nuevoId = resultSet.getInt(1)
        }

        resultSet.close()
        statement.close()
        dbConnection.close()
        nuevoId
    }
    suspend fun verificarCredenciales(
        username: String,
        password: String
    ): Pair<Int, String>? = withContext(Dispatchers.IO) {
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT * FROM verificar_usuario(?, ?)"
        )

        statement.setString(1, username)
        statement.setString(2, password)

        val resultSet = statement.executeQuery()

        var result: Pair<Int, String>? = null
        if (resultSet.next()) {
            val id = resultSet.getInt("id")
            val permiso = resultSet.getString("permiso")
            result = Pair(id, permiso)
        }

        resultSet.close()
        statement.close()
        dbConnection.close()

        result
    }

    suspend fun verificarUsuarioyContrasenia(
        username: String,
        password: String
    ): Boolean = withContext(Dispatchers.IO) {
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT verificar_usuario_existente(?, ?)"
        )

        statement.setString(1, username)
        statement.setString(2, password)

        val resultSet = statement.executeQuery()

        var result = false
        if (resultSet.next()) {
            result = resultSet.getBoolean(1)
        }

        resultSet.close()
        statement.close()
        dbConnection.close()

        result
    }
    suspend fun verificarUsuarioyContraseniaDialog(
        username: String,
        password: String
    ): Boolean = withContext(Dispatchers.IO) {
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT verificar_usuario_existente_dialog(?, ?)"
        )

        statement.setString(1, username)
        statement.setString(2, password)

        val resultSet = statement.executeQuery()

        var result = false
        if (resultSet.next()) {
            result = resultSet.getBoolean(1)
        }

        resultSet.close()
        statement.close()
        dbConnection.close()

        result
    }

    suspend fun deleteUsuario(idUsuario: Int): Boolean = withContext(Dispatchers.IO) {
        var eliminado = false
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT borrar_usuario(?)"
        )

        statement.setInt(1, idUsuario)
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            eliminado = resultSet.getBoolean(1)
        }

        resultSet.close()
        statement.close()
        dbConnection.close()
        eliminado
    }

}