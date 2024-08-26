package Class_DB

import Database.Database
import Models.Actividad
import Models.ActividadReporte
import Models.Animal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.Date
import java.sql.Time
import java.sql.Types
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.cos

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
                    descrip = resultSet.getString("descrip_act"),
                    costo = resultSet.getDouble("costo")
                )
            )
        }
        resultSet.close()
        statement.close()
        dbConnection.close()
        actividades
        //PROBAR INTERFAZ
    }

    suspend fun getActividadesReport(
        codigoAnim: Int,
    ): List<ActividadReporte>  = withContext(Dispatchers.IO) {

        val actividades = mutableListOf<ActividadReporte>()
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT * FROM reporte_actividades_animal(?)"
        )

        statement.setInt(1, codigoAnim)

        val resultSet = statement.executeQuery()


        while (resultSet.next()) {
            actividades.add(
                ActividadReporte(
                    codigo = resultSet.getInt("id_actividad"),
                    codigoAnim = resultSet.getInt("id_animal"),
                    fecha = resultSet.getDate("fecha").toLocalDate(),
                    hora = resultSet.getTime("hora").toLocalTime(),
                    tipo = resultSet.getString("tipo_actividad"),
                    codigoContr = resultSet.getInt("id_contrato"),
                    detalles = resultSet.getString("detalles"),
                    descrip = resultSet.getString("descrip_act"),
                    costo = resultSet.getDouble("costo")
                )
            )
        }
        resultSet.close()
        statement.close()
        dbConnection.close()
        actividades
        //PROBAR INTERFAZ
    }

    suspend fun createActividad(actividad: Actividad): Boolean = withContext(Dispatchers.IO){
        val dbConnection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "INSERT INTO actividad (id_animal, fecha, hora, tipo_actividad, id_contrato, descrip_act, costo) VALUES(?,?,?,?,?,?,?)"  // QUITAR LUEGO LOS DIAS
        )

        statement.setInt(1,actividad.codigoAnim)
        val sqlDate = Date.valueOf(actividad.fecha)
        statement.setDate(2,sqlDate)
        val sqlTime = Time.valueOf(actividad.hora)
        statement.setTime(3,sqlTime)
        statement.setString(4,actividad.tipo)
        statement.setInt(5,actividad.codigoContr)
        statement.setString(6,actividad.descrip)
        statement.setDouble(7,actividad.costo)

        val rowsInserted = statement.executeUpdate()
        statement.close()
        dbConnection.close()
        rowsInserted > 0
    }
}