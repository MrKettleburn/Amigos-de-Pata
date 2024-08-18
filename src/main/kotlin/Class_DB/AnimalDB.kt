package Class_DB

import Models.Animal
import Database.Database
import Models.AnimalAdoptado
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection


object AnimalDB {

    suspend fun createAnimal(nombre: String, especie: String, raza: String, edad: Int, peso: Double, cantDias: Int): Boolean = withContext(Dispatchers.IO) {
        val dbConnection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "INSERT INTO animal (nombre_animal, especie, raza, edad, peso, cant_dias) VALUES(?,?,?,?,?,?)"  // QUITAR LUEGO LOS DIAS
        )

        statement.setString(1,nombre)
        statement.setString(2,especie)
        statement.setString(3,raza)
        statement.setInt(4,edad)
        statement.setDouble(5,peso)
        statement.setInt(6,cantDias)

        val rowsInserted = statement.executeUpdate()
        statement.close()
        dbConnection.close()
        rowsInserted > 0
    }

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
        codigo: Int?,
        name: String?,
        especie: String?,
        raza: String?,
        edad: Int?,
        peso: Double?,
        cantDias: Int?
    ): List<Animal>  = withContext(Dispatchers.IO) {

         val animales = mutableListOf<Animal>()
         val dbConnection: Connection = Database.connect()
         val statement = dbConnection.prepareStatement(
             "SELECT * FROM buscar_animalesCAM(?, ?, ?, ?, ?, ?, ?)"
         )

         if (codigo != null) statement.setInt(1, codigo) else statement.setNull(1, java.sql.Types.INTEGER)
         statement.setString(2, name)
         statement.setString(3, especie)
         statement.setString(4, raza)
         if (edad != null) statement.setInt(5, edad) else statement.setNull(5, java.sql.Types.INTEGER)
         if (peso != null) statement.setDouble(6, peso) else statement.setNull(6, java.sql.Types.DOUBLE)
         if (cantDias != null) statement.setInt(7, cantDias) else statement.setNull(7, java.sql.Types.INTEGER)

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
            "SELECT * FROM buscar_animalesAdopt(?, ?, ?, ?, ?, ?, ?, ?)"
        )
        if (codigo != null) statement.setInt(1, codigo) else statement.setNull(1, java.sql.Types.INTEGER)
        statement.setString(2, nombre)
        statement.setString(3, especie)
        statement.setString(4, raza)
        if (edad != null) statement.setInt(5, edad) else statement.setNull(5, java.sql.Types.INTEGER)
        if (peso != null) statement.setDouble(6, peso) else statement.setNull(6, java.sql.Types.DOUBLE)
        if (cantDias != null) statement.setInt(7, cantDias) else statement.setNull(7, java.sql.Types.INTEGER)
        if (precioAdop != null) statement.setDouble(8,precioAdop) else statement.setNull(8,java.sql.Types.DOUBLE)

        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            animalesAdopt.add(
                AnimalAdoptado(
                    codigo = resultSet.getInt("id_animal"),
                    nombre = resultSet.getString("nombre_animal"),
                    especie = resultSet.getString("especie"),
                    raza = resultSet.getString("raza"),
                    edad = resultSet.getInt("edad"),
                    peso = resultSet.getDouble("peso"),
                    cantDias = resultSet.getInt("cant_dias"),
                    precioAdop = resultSet.getDouble("precio_adopcion")
                )
            )
        }
        resultSet.close()
        statement.close()
        dbConnection.close()
        animalesAdopt
    }
}