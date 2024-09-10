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
            "SELECT * FROM buscar_usuarios(?)"
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

}