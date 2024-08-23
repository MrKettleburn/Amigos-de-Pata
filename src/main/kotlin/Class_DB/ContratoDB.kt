package Class_DB

import Database.Database
import Models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.Date
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object ContratoDB {

///////////////////-----------------------INSERCIONES----------------------///////////////////////
    suspend fun createContratoVeterinario(contrato: ContratoVeterinario): Boolean  = withContext(Dispatchers.IO){
    val dbConnection = Database.connect()
    val statement = dbConnection.prepareStatement(
        "INSERT INTO contrato(tipo_contrato, descrip, precio_contrato, id_contratado, id_servicio, fecha_inicio, fecha_fin, fecha_conciliacion) VALUES(?,?,?,?,?,?,?,?)"  // QUITAR LUEGO LOS DIAS
    )

    statement.setString(1,"Veterinario")
    statement.setString(2,contrato.descripcion)
    statement.setDouble(3,contrato.precio)
    statement.setInt(4,contrato.idVet)
    statement.setInt(5,contrato.idServ)
    val sqlDateI = Date.valueOf(contrato.fechaInicio)
    statement.setDate(6, sqlDateI)
    val sqlDateF = Date.valueOf(contrato.fechaFin)
    statement.setDate(7, sqlDateF)
    val sqlDateC = Date.valueOf(contrato.fechaConcil)
    statement.setDate(8, sqlDateC)

    val rowsInserted = statement.executeUpdate()
    statement.close()
    dbConnection.close()
    rowsInserted > 0
}

//    suspend fun getContratos(): List<Animal> = withContext(Dispatchers.IO) {
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
//        animales
//    }

    suspend fun getContratosVeterinariosFilter(
        codigo: Int?,
        precioLI: Double?,
        precioLS: Double?,
        nombreVet: String?,
        clinicaVet: String?,
        provinciaVet: String?,
        especialidad: String?,
        modalidadServVet: String?,
        fechaInicioLI: String?,
        fechaInicioLS: String?,
        fechaFinLI: String?,
        fechaFinLS: String?,
        fechaConcilLI: String?,
        fechaConcilLS: String?
    ): List<ContratoVeterinario>  = withContext(Dispatchers.IO) {

        val contratos = mutableListOf<ContratoVeterinario>()
        val dbConnection: Connection = Database.connect()
        val statement= dbConnection.prepareStatement(
            "SELECT * FROM buscar_contratos_veterinario(?,    ?,       ?,       ?,     ?,    ?,     ?,     ?,    ?,    ?,    ?,      ?,     ?,     ?)"
        )//                                                cod   precLI   precLS   nombV  clin  provV  espV  modal fILI  fILS   fFLI   fFLS    fCLI   fCLS

        if (codigo != null) statement.setInt(1, codigo) else statement.setNull(1, java.sql.Types.INTEGER)
        if (precioLI != null) statement.setDouble(2, precioLI) else statement.setNull(2, java.sql.Types.DOUBLE)
        if (precioLS != null) statement.setDouble(3, precioLS) else statement.setNull(3, java.sql.Types.DOUBLE)
        statement.setString(4, nombreVet)
        statement.setString(5, clinicaVet)
        statement.setString(6, provinciaVet)
        statement.setString(7, especialidad)
        statement.setString(8, modalidadServVet)
        statement.setString(9, fechaInicioLI)
        statement.setString(10, fechaInicioLS)
        statement.setString(11, fechaFinLI)
        statement.setString(12, fechaFinLS)
        statement.setString(13, fechaConcilLI)
        statement.setString(14, fechaConcilLS)

        val resultSet = statement.executeQuery()

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        while (resultSet.next()) {
            contratos.add(
                ContratoVeterinario(
                    codigo = resultSet.getInt("id_contrato"),
                    precio = resultSet.getDouble("precio_contrato"),
                    descripcion = resultSet.getString("descrip"),
                    idVet = resultSet.getInt("id_contratado"),
                    nombreVet = resultSet.getString("nombre_contratado"),
                    clinicaVet = resultSet.getString("clinica"),
                    provinciaVet = resultSet.getString("provincia"),
                    direccVet = resultSet.getString("direccion"),
                    especialidad = resultSet.getString("especialidad"),
                    idServ = resultSet.getInt("id_servicio"),
                    modalidadServVet= resultSet.getString("modalidad"),
                    precioUnit = resultSet.getDouble("precio_unitario"),
                    fechaInicio = resultSet.getDate("fecha_inicio").toLocalDate(),
                    fechaFin = resultSet.getDate("fecha_fin").toLocalDate(),
                    fechaConcil = resultSet.getDate("fecha_conciliacion").toLocalDate()
                )
            )
        }
        resultSet.close()
        statement.close()
        dbConnection.close()
        contratos
        //PROBAR INTERFAZ
    }

    suspend fun getContratosProveedorAlimFilter(
        codigo: Int?,
        precioLI: Double?,
        precioLS: Double?,
        nombreProv: String?,
        provinciaProv: String?,
        tipoAlim: String?,
        fechaInicioLI: String?,
        fechaInicioLS: String?,
        fechaFinLI: String?,
        fechaFinLS: String?,
        fechaConcilLI: String?,
        fechaConcilLS: String?
    ): List<ContratoProveedorAlim>  = withContext(Dispatchers.IO) {

        val contratos = mutableListOf<ContratoProveedorAlim>()
        val dbConnection: Connection = Database.connect()
        val statement= dbConnection.prepareStatement(
            "SELECT * FROM buscar_contratos_proveedor(?,    ?,       ?,       ?,      ?,     ?,       ?,    ?,    ?,      ?,     ?,     ?)"
        )//                                              cod   precLI   precLS   nombV   provV  tipoAlim  fILI  fILS   fFLI   fFLS    fCLI   fCLS

        if (codigo != null) statement.setInt(1, codigo) else statement.setNull(1, java.sql.Types.INTEGER)
        if (precioLI != null) statement.setDouble(2, precioLI) else statement.setNull(2, java.sql.Types.DOUBLE)
        if (precioLS != null) statement.setDouble(3, precioLS) else statement.setNull(2, java.sql.Types.DOUBLE)
        statement.setString(4, nombreProv)
        statement.setString(5, provinciaProv)
        statement.setString(6, tipoAlim)
        statement.setString(7, fechaInicioLI)
        statement.setString(8, fechaInicioLS)
        statement.setString(9, fechaFinLI)
        statement.setString(10, fechaFinLS)
        statement.setString(11, fechaConcilLI)
        statement.setString(12, fechaConcilLS)

        val resultSet = statement.executeQuery()

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        while (resultSet.next()) {
            contratos.add(
                ContratoProveedorAlim(
                    codigo = resultSet.getInt("id_contrato"),
                    precio = resultSet.getDouble("precio_contrato"),
                    descripcion = resultSet.getString("descrip"),
                    nombreProv = resultSet.getString("nombre_contratado"),
                    provinciaProv = resultSet.getString("provincia"),
                    direccProv = resultSet.getString("direccion"),
                    tipoAlim= resultSet.getString("tipo_alimento"),
                    precioUnit = resultSet.getDouble("precio_unitario"),
                    fechaInicio = resultSet.getDate("fecha_inicio").toLocalDate(),
                    fechaFin = resultSet.getDate("fecha_fin").toLocalDate(),
                    fechaConcil = resultSet.getDate("fecha_conciliacion").toLocalDate(),
                )
            )
        }
        resultSet.close()
        statement.close()
        dbConnection.close()
        contratos
        //PROBAR INTERFAZ
    }

    suspend fun getContratosTransporteFilter(
        codigo: Int?,
        precioLI: Double?,
        precioLS: Double?,
        nombreProv: String?,
        provinciaProv: String?,
        fechaInicioLI: String?,
        fechaInicioLS: String?,
        fechaFinLI: String?,
        fechaFinLS: String?,
        fechaConcilLI: String?,
        fechaConcilLS: String?
    ): List<ContratoTransporte>  = withContext(Dispatchers.IO) {

        val contratos = mutableListOf<ContratoTransporte>()
        val dbConnection: Connection = Database.connect()
        val statement= dbConnection.prepareStatement(
            "SELECT * FROM buscar_contratos_transporte(?,    ?,       ?,       ?,      ?,      ?,       ?,    ?,      ?,      ?,     ?,     ?)"
        )//                                               cod   precLI   precLS   nombV   provV  tipoAlim   fILI  fILS   fFLI   fFLS    fCLI   fCLS

        if (codigo != null) statement.setInt(1, codigo) else statement.setNull(1, java.sql.Types.INTEGER)
        if (precioLI != null) statement.setDouble(2, precioLI) else statement.setNull(2, java.sql.Types.DOUBLE)
        if (precioLS != null) statement.setDouble(3, precioLS) else statement.setNull(2, java.sql.Types.DOUBLE)
        statement.setString(4, nombreProv)
        statement.setString(5, provinciaProv)
        statement.setString(6, fechaInicioLI)
        statement.setString(7, fechaInicioLS)
        statement.setString(8, fechaFinLI)
        statement.setString(9, fechaFinLS)
        statement.setString(10, fechaConcilLI)
        statement.setString(11, fechaConcilLS)

        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            contratos.add(
                ContratoTransporte(
                    codigo = resultSet.getInt("id_contrato"),
                    precio = resultSet.getDouble("precio_contrato"),
                    descripcion = resultSet.getString("descrip"),
                    nombreTrans = resultSet.getString("nombre_contratado"),
                    provinciaTrans = resultSet.getString("provincia"),
                    direccionTrans = resultSet.getString("direccion"),
                    vehiculo = resultSet.getString("tipo_alimento"),
                    precioUnit = resultSet.getDouble("precio_unitario"),
                    fechaInicio = resultSet.getDate("fecha_inicio").toLocalDate(),
                    fechaFin = resultSet.getDate("fecha_fin").toLocalDate(),
                    fechaConcil = resultSet.getDate("fecha_conciliacion").toLocalDate(),
                )
            )
        }
        resultSet.close()
        statement.close()
        dbConnection.close()
        contratos
        //PROBAR INTERFAZ
    }
}