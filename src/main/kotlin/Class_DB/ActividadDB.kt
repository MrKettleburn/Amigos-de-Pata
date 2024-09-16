package Class_DB

import Database.Database
import Models.Actividad
import Models.ActividadReporte
import Models.Animal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.cos

object ActividadDB {

    suspend fun getActividadesConContratoID(idContrato: Int): List<Actividad>? = withContext(Dispatchers.IO){
        val dbConnection: Connection? = null
        try {
            val actividades = mutableListOf<Actividad>()
            val dbConnection = Database.connect()
            val statement = dbConnection.prepareStatement(
                "SELECT * FROM buscar_actividades_por_contrato(?)"
            )

            statement.setInt(1, idContrato)

            val resultSet = statement.executeQuery()

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

            return@withContext actividades

        } catch (e: SQLException) {
            e.printStackTrace()
            return@withContext null
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        } finally {
            try {
                dbConnection?.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getActividadesFilter(
        codigoAnim: Int,
        codigo: Int?,
        fechaLI: String?,
        fechaLS: String?,
        tipo: String?,
        codigoContrato: Int?,
        tipoContrato: String?,
    ): List<Actividad>  = withContext(Dispatchers.IO) {

        val actividades = mutableListOf<Actividad>()
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT * FROM buscar_actividades(?, ?, ?, ?, ?, ?, ?)"
        )

        statement.setInt(1, codigoAnim)
        if (codigo != null) statement.setInt(2, codigo) else statement.setNull(2, Types.INTEGER)
        statement.setString(3, fechaLI)
        statement.setString(4, fechaLS)
        statement.setString(5, tipo)
        if (codigoContrato != null) statement.setInt(6, codigoContrato) else statement.setNull(6, Types.INTEGER)
        statement.setString(7, tipoContrato)

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

    suspend fun createActividad(actividad: Actividad): Boolean = withContext(Dispatchers.IO) {
        val dbConnection: Connection? = null
        try {
            val dbConnection = Database.connect()

            val statement = dbConnection.prepareStatement(
                "INSERT INTO actividad (id_animal, fecha, hora, tipo_actividad, id_contrato, descrip_act, costo) VALUES(?,?,?,?,?,?,?)"
            )

            statement.setInt(1, actividad.codigoAnim)
            val sqlDate = Date.valueOf(actividad.fecha)
            statement.setDate(2, sqlDate)
            val sqlTime = Time.valueOf(actividad.hora)
            statement.setTime(3, sqlTime)
            statement.setString(4, actividad.tipo)
            statement.setInt(5, actividad.codigoContr)
            statement.setString(6, actividad.descrip)
            statement.setDouble(7, actividad.costo)

            val rowsInserted = statement.executeUpdate()

            statement.close()
            dbConnection.close()


            rowsInserted > 0

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

    suspend fun updateActividad(codigo: Int, codigoAnim: Int, fecha: LocalDate, hora: LocalTime, tipo: String, codigoContr:Int, descrip: String, costo:Double): Boolean = withContext(Dispatchers.IO) {
        val dbConnection = Database.connect()
       try{
           val statement = dbConnection.prepareStatement(
            "UPDATE actividad SET fecha=?, hora=?, tipo_actividad=?, id_contrato=?, descrip_act=?, costo=? WHERE id_actividad=? AND id_animal=?"
            )

            val sqlDate = Date.valueOf(fecha)
            statement.setDate(1, sqlDate)
            val sqlTime = Time.valueOf(hora)
            statement.setTime(2,sqlTime)
            statement.setString(3,tipo)
            statement.setInt(4,codigoContr)
            statement.setString(5,descrip)
            statement.setDouble(6,costo)
            statement.setInt(7,codigo)
            statement.setInt(8,codigoAnim)

            val rowsUpdated = statement.executeUpdate()
            statement.close()
            dbConnection.close()
            rowsUpdated > 0

    } catch (e: SQLException) {

        e.printStackTrace()
        false

    } finally {
        dbConnection.close()
    }
    }

    suspend fun deleteActividad(actividId: Int, animalId: Int): Boolean = withContext(Dispatchers.IO) {
        val dbConnection: Connection? = null
        try {

            val dbConnection = Database.connect()
            val statement = dbConnection.prepareStatement(
                "DELETE FROM actividad WHERE id_animal = ? AND id_actividad=?"
            )

            statement.setInt(1, animalId)
            statement.setInt(2, actividId)

            val rowsDeleted = statement.executeUpdate()

            return@withContext rowsDeleted > 0

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
}