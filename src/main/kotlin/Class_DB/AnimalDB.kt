package Class_DB

import Models.Animal
import Database.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection


object AnimalDB {

     fun getAnimales(): List<Animal> {

        val animales= mutableListOf<Animal>()
        val dbConnection: Connection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "SELECT * FROM animal"
        )
        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            animales.add(
                Animal(
                    codigo= resultSet.getInt("id_animal"),
                    nombre= resultSet.getString("nombre_animal"),
                    especie= resultSet.getString("especie"),
                    raza= resultSet.getString("raza"),
                    edad= resultSet.getInt("edad"),
                    peso= resultSet.getDouble("peso"),
                    cantDias= resultSet.getInt("cant_dias"),
                )
            )
        }
        resultSet.close()
        statement.close()
        dbConnection.close()
        return animales
    }
}