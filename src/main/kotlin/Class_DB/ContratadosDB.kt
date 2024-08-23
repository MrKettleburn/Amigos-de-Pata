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

    suspend fun getTransportesForComboBox(): List<Transporte> = withContext(Dispatchers.IO) {

        val transportes = mutableListOf<Transporte>()
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT * FROM contratado c WHERE tipo_contratado='Transporte' ORDER BY c.nombre_contratado ASC"
        )

        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            transportes.add(
                Transporte(
                    codigo = resultSet.getInt("id_contratado"),
                    nombre = resultSet.getString("nombre_contratado"),
                    email = resultSet.getString("email"),
                    provincia = resultSet.getString("provincia"),
                    direccion = resultSet.getString("direccion"),
                    telefono = resultSet.getString("telefono"),
                )
            )
        }

        resultSet.close()
        statement.close()
        dbConnection.close()
        transportes
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

    suspend fun createVeterinario(veterinario: Veterinario): Int = withContext(Dispatchers.IO) {
        var nuevoId = -1
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT insertar_veterinario(?, ?, ?, ?, ?, ?, ?)",  // Consulta SQL que invoca la función almacenada
            arrayOf("id_contratado")
        )

        statement.setString(1, veterinario.nombre)
        statement.setString(2, veterinario.email)
        statement.setString(3, veterinario.provincia)
        statement.setString(4, veterinario.direccion)
        statement.setString(5, veterinario.telefono)
        statement.setString(6, veterinario.especialidad)
        statement.setString(7, veterinario.clinica)

        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            nuevoId = resultSet.getInt(1)
        }

        resultSet.close()
        statement.close()
        dbConnection.close()
        nuevoId
    }
    suspend fun createProveedorAlimentos(proveedorA: ProveedorDeAlimentos): Int = withContext(Dispatchers.IO) {
        var nuevoId = -1
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT insertar_proveedorA(?, ?, ?, ?, ?,)",  // Consulta SQL que invoca la función almacenada
            arrayOf("id_contratado")
        )

        statement.setString(1, proveedorA.nombre)
        statement.setString(2, proveedorA.email)
        statement.setString(3, proveedorA.provincia)
        statement.setString(4, proveedorA.direccion)
        statement.setString(5, proveedorA.telefono)

        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            nuevoId = resultSet.getInt(1)
        }

        resultSet.close()
        statement.close()
        dbConnection.close()
        nuevoId
    }
    suspend fun createTransportista(transportista: Transporte): Int = withContext(Dispatchers.IO) {
        var nuevoId = -1
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT insertar_transportista(?, ?, ?, ?, ?,)",  // Consulta SQL que invoca la función almacenada
            arrayOf("id_contratado")
        )

        statement.setString(1, transportista.nombre)
        statement.setString(2, transportista.email)
        statement.setString(3, transportista.provincia)
        statement.setString(4, transportista.direccion)
        statement.setString(5, transportista.telefono)

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