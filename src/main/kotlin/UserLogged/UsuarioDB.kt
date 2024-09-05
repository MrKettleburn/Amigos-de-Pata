package UserLogged

import Database.Database
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

    suspend fun createUsuario(usuarioAdopt: Usuario): Int = withContext(Dispatchers.IO) {
        var nuevoId = -1
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT insertar_usuario_adoptante(?, ?, ?)",  // Consulta SQL que invoca la función almacenada
        )

        statement.setString(1, usuarioAdopt.usuario)
        statement.setString(2, usuarioAdopt.contrasenia)

        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            nuevoId = resultSet.getInt(1)
        }

        resultSet.close()
        statement.close()
        dbConnection.close()
        nuevoId
    }
}