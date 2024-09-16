package Class_DB

import Database.Database
import Models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.SQLException

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

    suspend fun getProveedoresAlimForComboBox(): List<ProveedorDeAlimentos> = withContext(Dispatchers.IO) {

        val proveedores = mutableListOf<ProveedorDeAlimentos>()
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT * FROM contratado c WHERE c.tipo_contratado='Proveedor de Alimentos' ORDER BY c.nombre_contratado ASC"
        )

        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            proveedores.add(
                ProveedorDeAlimentos(
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
        proveedores
    }

    //-------------------------------------INSERCCIONES----------------------------------------------------------------

    suspend fun createVeterinario(veterinario: Veterinario): Int = withContext(Dispatchers.IO) {
        var nuevoId = -1
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT insertar_veterinario(?, ?, ?, ?, ?, ?, ?)",
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
            "SELECT insertar_contratado(?, ?, ?, ?, ?, ?)",
        )

        statement.setString(1,"Proveedor de Alimentos")
        statement.setString(2, proveedorA.nombre)
        statement.setString(3, proveedorA.email)
        statement.setString(4, proveedorA.provincia)
        statement.setString(5, proveedorA.direccion)
        statement.setString(6, proveedorA.telefono)

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
            "SELECT insertar_contratado(?, ?, ?, ?, ?, ?)",
        )

        statement.setString(1,"Transporte")
        statement.setString(2, transportista.nombre)
        statement.setString(3, transportista.email)
        statement.setString(4, transportista.provincia)
        statement.setString(5, transportista.direccion)
        statement.setString(6, transportista.telefono)

        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            nuevoId = resultSet.getInt(1)
        }

        resultSet.close()
        statement.close()
        dbConnection.close()
        nuevoId
    }

    ///////////////////------------------------ACTUALIZACIONES-------------/////////////////////

    suspend fun updateVeterinario(codigo:Int, nombre:String, email:String, provincia:String, direccion:String, telefono:String, especialidad:String, clinica:String): Boolean = withContext(Dispatchers.IO){
        val dbConnection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT  actualizar_veterinario(?,?,?,?,?,?,?,?)"
        )

        statement.setInt(1,codigo)
        statement.setString(2,nombre)
        statement.setString(3,email)
        statement.setString(4,provincia)
        statement.setString(5,direccion)
        statement.setString(6,telefono)
        statement.setString(7,especialidad)
        statement.setString(8,clinica)

        var retorno: Boolean = true
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            val resultado: Boolean = resultSet.getBoolean(1)
            if (!resultado) {
                println("Error al actualizar el servicio. Intente nuevamente.")
                retorno=false
            }
        }

        dbConnection.close()
        retorno
    }

    suspend fun updateTransporte(codigo:Int, nombre:String, email:String, provincia:String, direccion:String, telefono:String): Boolean = withContext(Dispatchers.IO){
        val dbConnection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT  actualizar_transporte(?,?,?,?,?,?)"
        )

        statement.setInt(1,codigo)
        statement.setString(2,nombre)
        statement.setString(3,email)
        statement.setString(4,provincia)
        statement.setString(5,direccion)
        statement.setString(6,telefono)

        var retorno: Boolean = true
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            val resultado: Boolean = resultSet.getBoolean(1)
            if (!resultado) {
                println("Error al actualizar el servicio. Intente nuevamente.")
                retorno=false
            }
        }

        dbConnection.close()
        retorno
    }

    suspend fun updateProveedorAlim(codigo:Int, nombre:String, email:String, provincia:String, direccion:String, telefono:String): Boolean = withContext(Dispatchers.IO){
        val dbConnection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT  actualizar_proveedoresalim(?,?,?,?,?,?)"
        )

        statement.setInt(1,codigo)
        statement.setString(2,nombre)
        statement.setString(3,email)
        statement.setString(4,provincia)
        statement.setString(5,direccion)
        statement.setString(6,telefono)

        var retorno: Boolean = true
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            val resultado: Boolean = resultSet.getBoolean(1)
            if (!resultado) {
                println("Error al actualizar el servicio. Intente nuevamente.")
                retorno=false
            }
        }

        dbConnection.close()
        retorno
    }

    suspend fun deleteVeterinario(idVet: Int): Boolean = withContext(Dispatchers.IO){
        val dbConnection: Connection? = null
        try {
            var success=true
            val dbConnection = Database.connect()
            val statement = dbConnection.prepareStatement(
                "SELECT eliminar_veterinario(?)"
            )

            statement.setInt(1, idVet)

            val resultSet = statement.executeQuery()

            if (resultSet.next()) {
                success = resultSet.getBoolean(1)
            }

            return@withContext success

        } catch (e: SQLException) {
            e.printStackTrace()
            return@withContext false
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext false
        } finally {
            try {
                dbConnection?.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    suspend fun deleteTransporte(idTrans: Int): Boolean = withContext(Dispatchers.IO){
        val dbConnection: Connection? = null
        try {
            var success=true
            val dbConnection = Database.connect()
            val statement = dbConnection.prepareStatement(
                "SELECT eliminar_transporte(?)"
            )

            statement.setInt(1, idTrans)

            val resultSet = statement.executeQuery()

            if (resultSet.next()) {
                success = resultSet.getBoolean(1)
            }

            return@withContext success

        } catch (e: SQLException) {
            e.printStackTrace()
            return@withContext false
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext false
        } finally {
            try {
                dbConnection?.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    suspend fun deleteProveedorAlim(idProv: Int): Boolean = withContext(Dispatchers.IO){
        val dbConnection: Connection? = null
        try {
            var success=true
            val dbConnection = Database.connect()
            val statement = dbConnection.prepareStatement(
                "SELECT eliminar_proveedor_alimentos(?)"
            )

            statement.setInt(1, idProv)

            val resultSet = statement.executeQuery()

            if (resultSet.next()) {
                success = resultSet.getBoolean(1)
            }

            return@withContext success

        } catch (e: SQLException) {
            e.printStackTrace()
            return@withContext false
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext false
        } finally {
            try {
                dbConnection?.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getVeterinariosActivosForReport(clinica:String?, provincia: String?): List<ReporteVeterinariosActivosObj> = withContext(Dispatchers.IO)
    {
        var lista = mutableListOf<ReporteVeterinariosActivosObj>()
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT * FROM reporte_veterinarios_activos(?,?)"
        )

        statement.setString(1,clinica)
        statement.setString(2,provincia)

        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            lista.add(
                ReporteVeterinariosActivosObj(
                    fechaInicio = resultSet.getDate("fecha_inicio").toLocalDate(),
                    fechaFin = resultSet.getDate("fecha_fin").toLocalDate(),
                    idVet = resultSet.getInt("id_contratado"),
                    nombre = resultSet.getString("nombre_veterinario"),
                    clinica = resultSet.getString("clinica"),
                    provincia = resultSet.getString("provincia"),
                    especialidad = resultSet.getString("especialidad"),
                    telefono = resultSet.getString("telefono"),
                    email = resultSet.getString("email"),
                    modalidad = resultSet.getString("modalidad")
                )
            )
        }
        resultSet.close()
        statement.close()
        dbConnection.close()
        lista
    }
}