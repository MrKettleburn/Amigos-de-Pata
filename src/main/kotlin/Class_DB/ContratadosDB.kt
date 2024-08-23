package Class_DB

import Database.Database
import Models.ProveedorDeAlimentos
import Models.Transporte
import Models.Veterinario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection

object ContratadosDB {

    //-------------------------------------CONSULTAS----------------------------------------------------------------
    suspend fun getVeterinariosFilter(
        codigo: Int?,
        nombre: String?,
        provincia: String?,
        especialidad: String?,
        clinica: String?
    ): List<Veterinario> = withContext(Dispatchers.IO) {

        val veterinarios = mutableListOf<Veterinario>()
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT * FROM buscar_veterinarios(?,?,?,?,?)"
        )

        if (codigo != null) statement.setInt(1, codigo) else statement.setNull(1, java.sql.Types.INTEGER)
        statement.setString(2, nombre)
        statement.setString(3, provincia)
        statement.setString(4, especialidad)
        statement.setString(5, clinica)

        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            veterinarios.add(
                Veterinario(
                    codigo = resultSet.getInt("id_contratado"),
                    nombre = resultSet.getString("nombre_contratado"),
                    email = resultSet.getString("email"),
                    provincia = resultSet.getString("provincia"),
                    direccion = resultSet.getString("direccion"),
                    telefono = resultSet.getString("telefono"),
                    especialidad = resultSet.getString("especialidad"),
                    clinica = resultSet.getString("clinica")
                )
            )
        }

        resultSet.close()
        statement.close()
        dbConnection.close()
        veterinarios
    }

    suspend fun getVeterinariosForComboBox(): List<Veterinario> = withContext(Dispatchers.IO) {

        val veterinarios = mutableListOf<Veterinario>()
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT * FROM contratado c INNER JOIN veterinario v ON c.id_contratado=v.id_contratado ORDER BY v.especialidad ASC"
        )

        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            veterinarios.add(
                Veterinario(
                    codigo = resultSet.getInt("id_contratado"),
                    nombre = resultSet.getString("nombre_contratado"),
                    email = resultSet.getString("email"),
                    provincia = resultSet.getString("provincia"),
                    direccion = resultSet.getString("direccion"),
                    telefono = resultSet.getString("telefono"),
                    especialidad = resultSet.getString("especialidad"),
                    clinica = resultSet.getString("clinica")
                )
            )
        }

        resultSet.close()
        statement.close()
        dbConnection.close()
        veterinarios
    }

 suspend fun getTransportistasFilter(
     codigo: Int?,
     nombre: String?,
     provincia: String?,
 ): List<Transporte> = withContext(Dispatchers.IO){

     val transportistas = mutableListOf<Transporte>()
     val dbConnection: Connection = Database.connect()
     val statement = dbConnection.prepareStatement(
         "SELECT * FROM buscar_transporte(?,?,?)"
     )

     if (codigo != null) statement.setInt(1, codigo) else statement.setNull(1, java.sql.Types.INTEGER)
     statement.setString(2, nombre)
     statement.setString(3, provincia)

     val resultSet = statement.executeQuery()

     while (resultSet.next()) {
         transportistas.add(
             Transporte(
                 codigo = resultSet.getInt("id_contratado"),
                 nombre = resultSet.getString("nombre_contratado"),
                 email = resultSet.getString("email"),
                 provincia = resultSet.getString("provincia"),
                 direccion = resultSet.getString("direccion"),
                 telefono = resultSet.getString("telefono")
             )
         )
     }
     resultSet.close()
     statement.close()
     dbConnection.close()
     transportistas
 }

    suspend fun getProveedoresAlimentosFilter(
        codigo: Int?,
        nombre: String?,
        provincia: String?,
    ): List<ProveedorDeAlimentos> = withContext(Dispatchers.IO){
        val proveedores = mutableListOf<ProveedorDeAlimentos>()
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT * FROM buscar_proveedoresA(?,?,?)"
        )

        if (codigo != null) statement.setInt(1, codigo) else statement.setNull(1, java.sql.Types.INTEGER)
        statement.setString(2, nombre)
        statement.setString(3, provincia)

        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            proveedores.add(
                ProveedorDeAlimentos(
                    codigo = resultSet.getInt("id_contratado"),
                    nombre = resultSet.getString("nombre_contratado"),
                    email = resultSet.getString("email"),
                    provincia = resultSet.getString("provincia"),
                    direccion = resultSet.getString("direccion"),
                    telefono = resultSet.getString("telefono")
                )
            )
        }
        resultSet.close()
        statement.close()
        dbConnection.close()
        proveedores
    }

    //-------------------------------------INSERCCIONES----------------------------------------------------------------

    suspend fun createVeterinario(
        nombre: String,
        email: String,
        provincia: String,
        direccion: String,
        telefono: String,
        especialidad: String,
        clinica: String
    ): Int = withContext(Dispatchers.IO) {

        var nuevoId = -1
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT insertar_veterinario(?, ?, ?, ?, ?, ?, ?)",
            arrayOf("id_contratado")
        )

        statement.setString(1, nombre)
        statement.setString(2, email)
        statement.setString(3, provincia)
        statement.setString(4, direccion)
        statement.setString(5, telefono)
        statement.setString(6, especialidad)
        statement.setString(7, clinica)

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

