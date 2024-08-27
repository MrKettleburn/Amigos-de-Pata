package Class_DB

import Database.Database
import Models.AnimalAdoptado
import Models.Donacion
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

}