package UserLogged

import Database.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.mindrot.jbcrypt.BCrypt
import java.sql.Connection

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

    suspend fun createUsuario(user: String, pass: String, rol: String): Int = withContext(Dispatchers.IO) {
        var nuevoId = -1
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT insertar_usuario_admin(?, ?, ?)"
        )

        val hashedPassword = BCrypt.hashpw(pass, BCrypt.gensalt())

        statement.setString(1, user)
        statement.setString(2, hashedPassword)
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

        val hashedPassword = BCrypt.hashpw(contrasena, BCrypt.gensalt())

        statement.setInt(1, idUsuario)
        statement.setString(2, nombreUsuario)
        statement.setString(3, hashedPassword)
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

        resultSet.close()
        statement.close()
        dbConnection.close()
        retorno
    }

    suspend fun createUsuarioAdoptante(name: String, user: String, pass: String): Int = withContext(Dispatchers.IO) {
        var nuevoId = -1
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT insertar_usuario_adoptante(?, ?, ?)"
        )

        val hashedPassword = BCrypt.hashpw(pass, BCrypt.gensalt())

        statement.setString(1, name)
        statement.setString(2, user)
        statement.setString(3, hashedPassword)

        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            nuevoId = resultSet.getInt(1)
        }

        resultSet.close()
        statement.close()
        dbConnection.close()
        nuevoId
    }
    suspend fun verificarCredenciales(username: String, password: String): Pair<Int, String>? = withContext(Dispatchers.IO) {
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT * FROM verificar_usuario_hash(?)"
        )

        statement.setString(1, username)

        val resultSet = statement.executeQuery()

        var result: Pair<Int, String>? = null
        if (resultSet.next()) {
            val id = resultSet.getInt("id")
            val permiso = resultSet.getString("permiso")
            val hashedPassword = resultSet.getString("hashed_password")
            if (BCrypt.checkpw(password, hashedPassword)) {
                result = Pair(id, permiso)
            }
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
    suspend fun verificarUsuarioyContraseniaLoginDialog(username: String, password: String): Boolean = withContext(Dispatchers.IO) {
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT verificar_usuario_existente_dialog_hash(?)"
        )

        statement.setString(1, username)

        val resultSet = statement.executeQuery()

        var result = false
        if (resultSet.next()) {
            val userExists = resultSet.getBoolean(1)
            if (userExists) {
                val statementPass = dbConnection.prepareStatement(
                    "SELECT contrasena FROM usuario WHERE nombre_usuario = ?"
                )
                statementPass.setString(1, username)

                val resultSetPass = statementPass.executeQuery()
                if (resultSetPass.next()) {
                    val hashedPassword = resultSetPass.getString("contrasena")
                    result = BCrypt.checkpw(password, hashedPassword)
                }

                resultSetPass.close()
                statementPass.close()
            }
        }

        resultSet.close()
        statement.close()
        dbConnection.close()

        result
    }

    suspend fun verificarUsuarioyContraseniaRegistroDialog(username: String, password: String): Boolean = withContext(Dispatchers.IO) {
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT verificar_usuario_existente_dialog_hash(?)"
        )

        statement.setString(1, username)

        val resultSet = statement.executeQuery()

        var result = true
        if (resultSet.next()) {
            val userExists = resultSet.getBoolean(1)
            if (!userExists) {
                val statementPass = dbConnection.prepareStatement(
                    "SELECT contrasena FROM usuario WHERE nombre_usuario = ?"
                )
                statementPass.setString(1, username)

                val resultSetPass = statementPass.executeQuery()
                if (resultSetPass.next()) {
                    val hashedPassword = resultSetPass.getString("contrasena")
                    result = BCrypt.checkpw(password, hashedPassword)
                }

                resultSetPass.close()
                statementPass.close()
            }
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