package Class_DB

import Database.Database
import Models.Actividad
import Models.Animal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.Types
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object ActividadDB {

    suspend fun getActividadesFilter(
        codigoAnim: Int,
        codigo: Int?,
        fechaLI: String?,
        fechaLS: String?,
        tipo: String?,
        tipoContrato: String?,
    ): List<Actividad>  = withContext(Dispatchers.IO) {

        val actividades = mutableListOf<Actividad>()
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT * FROM buscar_actividades(?, ?, ?, ?, ?, ?)"
        )

        statement.setInt(1, codigoAnim)
        if (codigo != null) statement.setInt(2, codigo) else statement.setNull(2, Types.INTEGER)
        statement.setString(3, fechaLI)
        statement.setString(4, fechaLS)
        statement.setString(5, tipo)
        statement.setString(6, tipoContrato)

        val resultSet = statement.executeQuery()

        val formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formatterTime = DateTimeFormatter.ofPattern("hh:mm a")

        while (resultSet.next()) {
            actividades.add(
                Actividad(
                    codigo = resultSet.getInt("id_actividad"),
                    codigoAnim = resultSet.getInt("id_animal"),
                    fecha = resultSet.getDate("fecha").toLocalDate(),
                    hora = resultSet.getTime("hora").toLocalTime(),
                    tipo = resultSet.getString("tipo_actividad"),
                    codigoContr = resultSet.getInt("id_contrato"),
                    tipoContrato = resultSet.getString("tipo_contrato"),
                    descrip = resultSet.getString("descrip_act")
                )
            )
        }
        resultSet.close()
        statement.close()
        dbConnection.close()
        actividades
        //PROBAR INTERFAZ
    }
}