package Class_DB

import Models.Animal
import Database.Database
import Models.AnimalAdoptado
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.Date
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


object AnimalDB {

//    suspend fun getAnimales(): List<Animal> = withContext(Dispatchers.IO) {
//
//        val animales= mutableListOf<Animal>()
//        val dbConnection: Connection = Database.connect()
//        val statement = dbConnection.prepareStatement(
//            "SELECT * FROM animal"
//        )
//        val resultSet = statement.executeQuery()
//
//        while (resultSet.next()) {
//            animales.add(
//                Animal(
//                    codigo= resultSet.getInt("id_animal"),
//                    nombre= resultSet.getString("nombre_animal"),
//                    especie= resultSet.getString("especie"),
//                    raza= resultSet.getString("raza"),
//                    edad= resultSet.getInt("edad"),
//                    peso= resultSet.getDouble("peso"),
//                    cantDias= resultSet.getInt("cant_dias"),
//                )
//            )
//        }
//        resultSet.close()
//        statement.close()
//        dbConnection.close()
//         animales
//    }

    //-------------------------------------CONSULTAS----------------------------------------------------
     suspend fun getAnimalesFilter(
        codigo: Int?,
        name: String?,
        especie: String?,
        raza: String?,
        edad: Int?,
        fechaIngresoLI: String?,
        fechaIngresoLS: String?,
    ): List<Animal>  = withContext(Dispatchers.IO) {

         val animales = mutableListOf<Animal>()
         val dbConnection: Connection = Database.connect()
         val statement = dbConnection.prepareStatement(
             "SELECT * FROM buscar_animales(?, ?, ?, ?, ?, ?, ?)"
         )

         if (codigo != null) statement.setInt(1, codigo) else statement.setNull(1, java.sql.Types.INTEGER)
         statement.setString(2, name)
         statement.setString(3, especie)
         statement.setString(4, raza)
         if (edad != null) statement.setInt(5, edad) else statement.setNull(5, java.sql.Types.INTEGER)
         statement.setString(6, fechaIngresoLI)
         statement.setString(7, fechaIngresoLS)

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
                    fecha_ingreso = resultSet.getDate("fecha_ingreso").toLocalDate(),
                    cantDias = ChronoUnit.DAYS.between(resultSet.getDate("fecha_ingreso").toLocalDate(), LocalDate.now()).toInt()
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
        fechaIngresoLI: String?,
        fechaIngresoLS: String?,
        precioAdopInf: Double?,
        precioAdopSup: Double?
    ): List<AnimalAdoptado> = withContext(Dispatchers.IO) {

        val animalesAdopt = mutableListOf<AnimalAdoptado>()
        val dbConnection: Connection = Database.connect()
        val statement= dbConnection.prepareStatement(
            "SELECT * FROM buscar_animales_adoptados(?, ?, ?, ?, ?, ?, ?, ?, ?)"
        )
        if (codigo != null) statement.setInt(1, codigo) else statement.setNull(1, java.sql.Types.INTEGER)
        statement.setString(2, nombre)
        statement.setString(3, especie)
        statement.setString(4, raza)
        if (edad != null) statement.setInt(5, edad) else statement.setNull(5, java.sql.Types.INTEGER)
        statement.setString(6, fechaIngresoLI)
        statement.setString(7, fechaIngresoLS)
        if (precioAdopInf != null) statement.setDouble(8,precioAdopInf) else statement.setNull(8,java.sql.Types.DOUBLE)
        if (precioAdopSup != null) statement.setDouble(9,precioAdopSup) else statement.setNull(9,java.sql.Types.DOUBLE)

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
                    fecha_ingreso = resultSet.getDate("fecha_ingreso").toLocalDate(),
                    precioAdop = resultSet.getDouble("precio_adopcion")
                )
            )
        }
        resultSet.close()
        statement.close()
        dbConnection.close()
        animalesAdopt
    }

    //_____________________________________INSERCCIONES____________________________________________

    suspend fun createAnimal(animal: Animal): Boolean = withContext(Dispatchers.IO) {
        val dbConnection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "INSERT INTO animal (nombre_animal, especie, raza, edad, peso, fecha_ingreso) VALUES(?,?,?,?,?,?)"  // QUITAR LUEGO LOS DIAS
        )

        statement.setString(1,animal.nombre)
        statement.setString(2,animal.especie)
        statement.setString(3,animal.raza)
        statement.setInt(4,animal.edad)
        statement.setDouble(5,animal.peso)
        val sqlDate = Date.valueOf(animal.fecha_ingreso)
        statement.setDate(6, sqlDate)

        val rowsInserted = statement.executeUpdate()
        statement.close()
        dbConnection.close()
        rowsInserted > 0
    }

    suspend fun createAnimalAdoptado(animalAdoptado: AnimalAdoptado): Boolean = withContext(Dispatchers.IO) {
        val dbConnection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "INSERT INTO animal_adoptado (nombre_animal, especie, raza, edad, peso, fecha_ingreso, precio) VALUES(?,?,?,?,?,?,?)"
        )

        statement.setString(1, animalAdoptado.nombre)
        statement.setString(2, animalAdoptado.especie)
        statement.setString(3, animalAdoptado.raza)
        statement.setInt(4, animalAdoptado.edad)
        statement.setDouble(5, animalAdoptado.peso)
        val sqlDate = Date.valueOf(animalAdoptado.fecha_ingreso)
        statement.setDate(6, sqlDate)
        statement.setDouble(7, animalAdoptado.precioAdop)

        val rowsInserted = statement.executeUpdate()
        statement.close()
        dbConnection.close()
        rowsInserted > 0
    }

    ///////////--------------------------ACTUALIZACIONES------------------------////////////////

    suspend fun updateAnimal(codigo:Int, nombre: String, especie: String, raza:String, edad:Int, peso:Double, fechaIngreso:LocalDate): Boolean = withContext(Dispatchers.IO) {
        val dbConnection = Database.connect()
        val statement = dbConnection.prepareStatement(
            "UPDATE animal SET nombre_animal= ?, especie= ?, raza=?, edad=?, peso=?, fecha_ingreso=? WHERE id_animal=?"  // QUITAR LUEGO LOS DIAS
        )

        statement.setString(1,nombre)
        statement.setString(2,especie)
        statement.setString(3,raza)
        statement.setInt(4,edad)
        statement.setDouble(5,peso)
        val sqlDate = Date.valueOf(fechaIngreso)
        statement.setDate(6, sqlDate)
        statement.setInt(7,codigo)

        val rowsInserted = statement.executeUpdate()
        statement.close()
        dbConnection.close()
        rowsInserted > 0
    }
}