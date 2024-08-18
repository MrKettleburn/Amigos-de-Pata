package Database

import java.sql.Connection
import java.sql.DriverManager

object Database {

    private val url = System.getenv("DB_URL") ?: throw IllegalStateException("DB_URL no está configurado")

    fun connect(): Connection {

        val connection = DriverManager.getConnection(url)
        println("Conexión a la base de datos establecida correctamente")
        return connection

    }
}