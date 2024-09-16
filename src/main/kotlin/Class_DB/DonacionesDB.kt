package Class_DB

import Database.Database
import Models.AnimalAdoptado
import Models.Donacion
import UserLogged.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection

object DonacionesDB {

    suspend fun getDonacionesForReport(): List<Donacion> = withContext(Dispatchers.IO)
    {
        val donaciones = mutableListOf<Donacion>()
        val dbConnection: Connection = Database.connect()
        val statement= dbConnection.prepareStatement(
            "SELECT * FROM reporte_donaciones()"
        )

        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            donaciones.add(
                Donacion(
                    id = resultSet.getInt("id_donacion"),
                    monto = resultSet.getDouble("monto"),
                    nombreAdopt = resultSet.getString("nombre_adoptante")
                )
            )
        }
        resultSet.close()
        statement.close()
        dbConnection.close()
        donaciones
    }

    suspend fun getDonacionesFilter(
        montoInf: Double?,
        montoSup: Double?,
        nombreAdoptante: String?
    ): List<Donacion> = withContext(Dispatchers.IO) {
        val donaciones = mutableListOf<Donacion>()
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT * FROM buscar_donaciones_filtrado(?, ?, ?)"
        )

        if (montoInf != null) statement.setDouble(1,montoInf) else statement.setNull(1,java.sql.Types.DOUBLE)
        if (montoSup != null) statement.setDouble(2,montoSup) else statement.setNull(2,java.sql.Types.DOUBLE)
        statement.setString(3, nombreAdoptante)

        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            donaciones.add(
                Donacion(
                    id = resultSet.getInt("id_donacion"),
                    monto = resultSet.getDouble("monto"),
                    nombreAdopt = resultSet.getString("nombre_adoptante")
                )
            )
        }
        resultSet.close()
        statement.close()
        dbConnection.close()
        donaciones
    }

    suspend fun createDonacion(monto: Double, idAdop: Int): Int = withContext(Dispatchers.IO) {
        var nuevoId = -1
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT insertar_donacion_adoptante(?, ?)"
        )

        statement.setDouble(1, monto)
        statement.setInt(2, idAdop)

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