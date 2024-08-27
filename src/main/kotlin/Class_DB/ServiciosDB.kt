package Class_DB

import Database.Database
import Models.ServTransporte
import Models.ServAlimenticio
import Models.ServVeterinario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.Date
import java.time.LocalDate

object ServiciosDB {

    //-------------------------------------CONSULTAS----------------------------------------------------------------

    suspend fun getServiciosVeterinariosFilter(
        codigo: Int?,
        precioUnitInf: Double?,
        precioUnitSup: Double?,
        modalidad: String?
    ): List<ServVeterinario> = withContext(Dispatchers.IO) {
        val servVeterinarios = mutableListOf<ServVeterinario>()
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT * FROM buscar_serviciosVeter(?,?,?,?)"
        )

        if (codigo != null) statement.setInt(1, codigo) else statement.setNull(1, java.sql.Types.INTEGER)
        if (precioUnitInf != null) statement.setDouble(2, precioUnitInf) else statement.setNull(
            2,
            java.sql.Types.DOUBLE
        )
        if (precioUnitSup != null) statement.setDouble(3, precioUnitSup) else statement.setNull(
            3,
            java.sql.Types.DOUBLE
        )
        statement.setString(4, modalidad)

        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            servVeterinarios.add(
                ServVeterinario(
                    codigo = resultSet.getInt("id_servicio"),
                    precioUni = resultSet.getDouble("precio_unitario"),
                    modalidad = resultSet.getString("modalidad")
                )
            )
        }
        resultSet.close()
        statement.close()
        dbConnection.close()
        servVeterinarios
    }

    suspend fun getServiciosVeterinariosForComboBox(): List<ServVeterinario> = withContext(Dispatchers.IO) {
        val servVeterinarios = mutableListOf<ServVeterinario>()
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT * FROM servicio s INNER JOIN servicio_veterinario sv ON s.id_servicio=sv.id_servicio ORDER BY sv.modalidad ASC"
        )

        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            servVeterinarios.add(
                ServVeterinario(
                    codigo = resultSet.getInt("id_servicio"),
                    precioUni = resultSet.getDouble("precio_unitario"),
                    modalidad = resultSet.getString("modalidad")
                )
            )
        }
        resultSet.close()
        statement.close()
        dbConnection.close()
        servVeterinarios
    }

    suspend fun getServiciosTransportacionFilter(
        codigo: Int?,
        precioUnitInf: Double?,
        precioUnitSup: Double?,
        vehiculo: String?
    ): List<ServTransporte> = withContext(Dispatchers.IO) {
        val servTransportacion = mutableListOf<ServTransporte>()
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT * FROM buscar_serviciosTransp(?,?,?,?)"
        )

        if (codigo != null) statement.setInt(1, codigo) else statement.setNull(1, java.sql.Types.INTEGER)
        if (precioUnitInf != null) statement.setDouble(2, precioUnitInf) else statement.setNull(
            2,
            java.sql.Types.DOUBLE
        )
        if (precioUnitSup != null) statement.setDouble(3, precioUnitSup) else statement.setNull(
            3,
            java.sql.Types.DOUBLE
        )
        statement.setString(4, vehiculo)

        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            servTransportacion.add(
                ServTransporte(
                    codigo = resultSet.getInt("id_servicio"),
                    precioUni = resultSet.getDouble("precio_unitario"),
                    vehiculo = resultSet.getString("vehiculo")
                )
            )
        }
        resultSet.close()
        statement.close()
        dbConnection.close()
        servTransportacion
    }

    suspend fun getServiciosTransporteForComboBox(): List<ServTransporte> = withContext(Dispatchers.IO) {
        val servTransportes = mutableListOf<ServTransporte>()
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT * FROM servicio s INNER JOIN servicio_transporte st ON s.id_servicio=st.id_servicio ORDER BY st.vehiculo ASC"
        )

        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            servTransportes.add(
                ServTransporte(
                    codigo = resultSet.getInt("id_servicio"),
                    precioUni = resultSet.getDouble("precio_unitario"),
                    vehiculo = resultSet.getString("vehiculo")
                )
            )
        }
        resultSet.close()
        statement.close()
        dbConnection.close()
        servTransportes
    }

    suspend fun getServiciosAlimenticiosFilter(
        codigo: Int?,
        precioUnitInf: Double?,
        precioUnitSup: Double?,
        tipoAlimento: String?
    ): List<ServAlimenticio> = withContext(Dispatchers.IO) {
        val servAlimentacion = mutableListOf<ServAlimenticio>()
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT * FROM buscar_serviciosAlim(?,?,?,?)"
        )

        if (codigo != null) statement.setInt(1, codigo) else statement.setNull(1, java.sql.Types.INTEGER)
        if (precioUnitInf != null) statement.setDouble(2, precioUnitInf) else statement.setNull(
            2,
            java.sql.Types.DOUBLE
        )
        if (precioUnitSup != null) statement.setDouble(3, precioUnitSup) else statement.setNull(
            3,
            java.sql.Types.DOUBLE
        )
        statement.setString(4, tipoAlimento)

        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            servAlimentacion.add(
                ServAlimenticio(
                    codigo = resultSet.getInt("id_servicio"),
                    precioUni = resultSet.getDouble("precio_unitario"),
                    tipoAlimento = resultSet.getString("tipo_alimento")
                )
            )
        }
        resultSet.close()
        statement.close()
        dbConnection.close()
        servAlimentacion
    }

    suspend fun getServiciosAlimenticiosForComboBox(): List<ServAlimenticio> = withContext(Dispatchers.IO) {
        val servicios = mutableListOf<ServAlimenticio>()
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT * FROM servicio s INNER JOIN servicio_alimentacion sa ON s.id_servicio=sa.id_servicio ORDER BY sa.tipo_alimento ASC"
        )

        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            servicios.add(
                ServAlimenticio(
                    codigo = resultSet.getInt("id_servicio"),
                    precioUni = resultSet.getDouble("precio_unitario"),
                    tipoAlimento = resultSet.getString("tipo_alimento")
                )
            )
        }
        resultSet.close()
        statement.close()
        dbConnection.close()
        servicios
    }

    //-------------------------------------INSERCCIONES----------------------------------------------------------------

    suspend fun createServicioVeterinario(servicio: ServVeterinario): Int = withContext(Dispatchers.IO) {
        var nuevoId = -1
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT insertar_servicio_veterinario(?, ?)",  // Consulta SQL que invoca la función almacenada
        )

        statement.setDouble(1, servicio.precioUni)
        statement.setString(2, servicio.modalidad)

        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            nuevoId = resultSet.getInt(1)
        }

        resultSet.close()
        statement.close()
        dbConnection.close()
        nuevoId
    }

    suspend fun createServicioTransporte(servicio: ServTransporte): Int = withContext(Dispatchers.IO) {
        var nuevoId = -1
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT insertar_servicio_transporte(?, ?)",
        )

        statement.setDouble(1, servicio.precioUni)
        statement.setString(2, servicio.vehiculo)

        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            nuevoId = resultSet.getInt(1)
        }

        resultSet.close()
        statement.close()
        dbConnection.close()
        nuevoId
    }

    suspend fun createServicioAlimenticio(servicio: ServAlimenticio): Int = withContext(Dispatchers.IO) {
        var nuevoId = -1
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT insertar_servicio_alimentacion(?, ?)",
        )

        statement.setDouble(1, servicio.precioUni)
        statement.setString(2, servicio.tipoAlimento)

        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            nuevoId = resultSet.getInt(1)
        }

        resultSet.close()
        statement.close()
        dbConnection.close()
        nuevoId
    }

    ///////////////------------------ACTUALIZACIONES----------------///////////////////////

    suspend fun updateServicioVeterinario(codigo:Int, precioUnit:Double, modalidad: String): Boolean = withContext(Dispatchers.IO) {
        val dbConnection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT actualizar_servicio_veterinario(?,?,?)"
        )

        statement.setInt(1,codigo)
        statement.setDouble(2,precioUnit)
        statement.setString(3,modalidad)

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

    suspend fun updateServicioTransporte(codigo:Int, precioUnit:Double, vehiculo: String): Boolean = withContext(Dispatchers.IO) {
        val dbConnection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT actualizar_servicio_transporte(?,?,?)"
        )

        statement.setInt(1,codigo)
        statement.setDouble(2,precioUnit)
        statement.setString(3,vehiculo)

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

    suspend fun updateServicioAlimenticio(codigo:Int, precioUnit:Double, tipoAlim: String): Boolean = withContext(Dispatchers.IO) {
        val dbConnection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT actualizar_servicio_alimentacion(?,?,?)"
        )

        statement.setInt(1,codigo)
        statement.setDouble(2,precioUnit)
        statement.setString(3,tipoAlim)

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

}


