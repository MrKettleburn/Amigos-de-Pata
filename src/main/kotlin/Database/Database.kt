package Database

import java.sql.Connection
import java.sql.DriverManager

object Database {

    private val url = "jdbc:postgresql://2.tcp.eu.ngrok.io:14571/postgres"
    private val user = "postgres"
    private val password = "clari12345*"

    fun connect(): Connection {

        val connection = DriverManager.getConnection(url, user, password)
        println("Conexión a la base de datos establecida correctamente")
        return connection

    }
}