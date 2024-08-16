package Database

import java.sql.Connection
import java.sql.DriverManager

object Database {

    private val url = "jdbc:postgresql://localhost:5432/PruebaProyecto"
    private val user = "postgres"
    private val password = "Ruben.2003"

    fun connect(): Connection {

        val connection = DriverManager.getConnection(url, user, password)
        println("Conexión a la base de datos establecida correctamente")
        return connection

    }
}