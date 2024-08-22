package Class_DB

import Database.Database
import Models.ServTransporte
import Models.ServAlimenticio
import Models.ServVeterinario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
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

    //-------------------------------------INSERCCIONES----------------------------------------------------------------

    suspend fun createServicioVeterinario(servicio: ServVeterinario): Boolean = withContext(Dispatchers.IO) {
        val dbConnection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "INSERT INTO servicio_veterinario (codigo, modalidad, precio_uni) VALUES(?,?,?)"
        )

        statement.setInt(1, servicio.codigo)
        statement.setString(2, servicio.modalidad)
        statement.setDouble(3, servicio.precioUni)

        val rowsInserted = statement.executeUpdate()
        statement.close()
        dbConnection.close()
        rowsInserted > 0
    }
}


