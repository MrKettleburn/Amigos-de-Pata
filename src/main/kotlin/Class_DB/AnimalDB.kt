package Class_DB

import Models.Animal
import Database.Database
import Models.AnimalAdoptado
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection


object AnimalDB {

     suspend fun getAnimales(): List<Animal> = withContext(Dispatchers.IO) {

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
         animales
    }

     suspend fun getAnimalesFilter(
        name: String?,
        especie: String?,
        raza: String?,
        edad: Int?,
        peso: Double?,
        cantDias: Int?
    ): List<Animal>  = withContext(Dispatchers.IO) {

        val animales = mutableListOf<Animal>()
        val dbConnection: Connection = Database.connect()
        val statement= dbConnection.prepareStatement(
            "SELECT * FROM buscar_animalesCAM(?, ?, ?, ?, ?, ?)"
        )

        statement.setString(1, name)
        statement.setString(2, especie)
        statement.setString(3, raza)
        if (edad != null) statement.setInt(4, edad) else statement.setNull(4, java.sql.Types.INTEGER)
        if (peso != null) statement.setDouble(5, peso) else statement.setNull(5, java.sql.Types.DOUBLE)
        if (cantDias != null) statement.setInt(6, cantDias) else statement.setNull(6, java.sql.Types.INTEGER)


        val resultSet = statement.executeQuery()


        while (resultSet.next()) {
            animales.add(
                Animal(
                    codigo = resultSet.getInt("id_animal"),
                    nombre = resultSet.getString("nombre_animal"),
                    especie = resultSet.getString("especie"),
                    raza = resultSet.getString("raza"),
                    edad = resultSet.getInt("edad"),
                    peso = resultSet.getDouble("peso"),
                    cantDias = resultSet.getInt("cant_dias")
                )
            )
        }
        resultSet.close()
        statement.close()
        dbConnection.close()
         animales
        //PROBAR INTERFAZ
    }

    suspend fun getAnimalAdoptFilter(
        codigo: Int?,
        nombre: String?,
        especie: String?,
        raza: String?,
        edad: Int?,
        peso: Double?,
        cantDias: Int?,
        precioAdop: Double?
    ): List<AnimalAdoptado> = withContext(Dispatchers.IO) {

        val animalesAdopt = mutableListOf<AnimalAdoptado>()
        val dbConnection: Connection = Database.connect()
        val statement= dbConnection.prepareStatement(
            "SELECT * FROM buscar_animalesAdopt(?, ?, ?, ?, ?, ?)"
        )




        animalesAdopt
    }

}