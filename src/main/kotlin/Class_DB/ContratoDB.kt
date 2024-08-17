package Class_DB

import Database.Database
import Models.Animal
import Models.ContratoProveedorAlim
import Models.ContratoTransporte
import Models.ContratoVeterinario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object ContratoDB {



    suspend fun getContratos(): List<Animal> = withContext(Dispatchers.IO) {

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

    suspend fun getContratosVeterinariosFilter(
        codigo: String?,
        precioLI: Double?,
        precioLS: Double?,
        nombreVet: String?,
        clinicaVet: String?,
        provinciaVet: String?,
        direccVet: String?,
        especialidad: String?,
        modalidadServVet: String?,
        precioUnitLI: Double?,
        precioUnitLS: Double?,
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
            "SELECT * FROM buscar_contratos_veterinario(?,    ?,       ?,       ?,     ?,    ?,     ?,    ?,     ?,     ?,    ?,     ?,    ?,    ?,      ?,     ?,     ?)"
        )//                                                cod   precLI   precLS   nombV  clin  provV  dirV   espV  modal  pULI  pULS   fILI  fILS   fFLI   fFLS    fCLI   fCLS

        if (codigo != null) statement.setInt(1, codigo.toInt()) else statement.setNull(1, java.sql.Types.INTEGER)
        if (precioLI != null) statement.setDouble(2, precioLI) else statement.setNull(2, java.sql.Types.DOUBLE)
        if (precioLS != null) statement.setDouble(3, precioLS) else statement.setNull(2, java.sql.Types.DOUBLE)
        statement.setString(4, nombreVet)
        statement.setString(5, clinicaVet)
        statement.setString(6, provinciaVet)
        statement.setString(7, direccVet)
        statement.setString(8, especialidad)
        statement.setString(9, modalidadServVet)
        if (precioUnitLI != null) statement.setDouble(10, precioUnitLI) else statement.setNull(10, java.sql.Types.DOUBLE)
        if (precioUnitLS != null) statement.setDouble(11, precioUnitLS) else statement.setNull(11, java.sql.Types.DOUBLE)
        statement.setString(12, fechaInicioLI)
        statement.setString(13, fechaInicioLS)
        statement.setString(14, fechaFinLI)
        statement.setString(15, fechaFinLS)
        statement.setString(16, fechaConcilLI)
        statement.setString(17, fechaConcilLS)

        val resultSet = statement.executeQuery()

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        while (resultSet.next()) {
            contratos.add(
                ContratoVeterinario(
                    codigo = resultSet.getInt("id_contrato"),
                    precio = resultSet.getDouble("precio_contrato"),
                    descripcion = resultSet.getString("descrip"),
                    nombreVet = resultSet.getString("nombre_contratado"),
                    clinicaVet = resultSet.getString("clinica"),
                    provinciaVet = resultSet.getString("provincia"),
                    direccVet = resultSet.getString("direccion"),
                    especialidad = resultSet.getString("especialidad"),
                    modalidadServVet= resultSet.getString("modalidad"),
                    precioUnit = resultSet.getDouble("precio_unitario"),
                    fechaInicio = LocalDate.parse(resultSet.getString("fecha_inicio"), formatter),
                    fechaFin = LocalDate.parse(resultSet.getString("fecha_fin"), formatter),
                    fechaConcil = LocalDate.parse(resultSet.getString("fecha_conciliacion"), formatter)
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
        codigo: String?,
        precioLI: Double?,
        precioLS: Double?,
        nombreProv: String?,
        provinciaProv: String?,
        direccProv: String?,
        tipoAlim: String?,
        precioUnitLI: Double?,
        precioUnitLS: Double?,
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
            "SELECT * FROM buscar_contratos_proveedor(?,    ?,       ?,       ?,      ?,     ?,      ?,       ?,    ?,     ?,    ?,    ?,      ?,     ?,     ?)"
        )//                                              cod   precLI   precLS   nombV   provV  dirV   tipoAlim  pULI  pULS   fILI  fILS   fFLI   fFLS    fCLI   fCLS

        if (codigo != null) statement.setInt(1, codigo.toInt()) else statement.setNull(1, java.sql.Types.INTEGER)
        if (precioLI != null) statement.setDouble(2, precioLI) else statement.setNull(2, java.sql.Types.DOUBLE)
        if (precioLS != null) statement.setDouble(3, precioLS) else statement.setNull(2, java.sql.Types.DOUBLE)
        statement.setString(4, nombreProv)
        statement.setString(5, provinciaProv)
        statement.setString(6, direccProv)
        statement.setString(7, tipoAlim)
        if (precioUnitLI != null) statement.setDouble(8, precioUnitLI) else statement.setNull(8, java.sql.Types.DOUBLE)
        if (precioUnitLS != null) statement.setDouble(9, precioUnitLS) else statement.setNull(9, java.sql.Types.DOUBLE)
        statement.setString(10, fechaInicioLI)
        statement.setString(11, fechaInicioLS)
        statement.setString(12, fechaFinLI)
        statement.setString(13, fechaFinLS)
        statement.setString(14, fechaConcilLI)
        statement.setString(15, fechaConcilLS)

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
                    fechaInicio = LocalDate.parse(resultSet.getString("fecha_inicio"), formatter),
                    fechaFin = LocalDate.parse(resultSet.getString("fecha_fin"), formatter),
                    fechaConcil = LocalDate.parse(resultSet.getString("fecha_conciliacion"), formatter)
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
        codigo: String?,
        precioLI: Double?,
        precioLS: Double?,
        nombreProv: String?,
        provinciaProv: String?,
        direccProv: String?,
        vehiculo: String?,
        precioUnitLI: Double?,
        precioUnitLS: Double?,
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
            "SELECT * FROM buscar_contratos_transporte(?,    ?,       ?,       ?,      ?,     ?,      ?,       ?,    ?,     ?,    ?,    ?,      ?,     ?,     ?)"
        )//                                               cod   precLI   precLS   nombV   provV  dirV   tipoAlim  pULI  pULS   fILI  fILS   fFLI   fFLS    fCLI   fCLS

        if (codigo != null) statement.setInt(1, codigo.toInt()) else statement.setNull(1, java.sql.Types.INTEGER)
        if (precioLI != null) statement.setDouble(2, precioLI) else statement.setNull(2, java.sql.Types.DOUBLE)
        if (precioLS != null) statement.setDouble(3, precioLS) else statement.setNull(2, java.sql.Types.DOUBLE)
        statement.setString(4, nombreProv)
        statement.setString(5, provinciaProv)
        statement.setString(6, direccProv)
        statement.setString(7, vehiculo)
        if (precioUnitLI != null) statement.setDouble(8, precioUnitLI) else statement.setNull(8, java.sql.Types.DOUBLE)
        if (precioUnitLS != null) statement.setDouble(9, precioUnitLS) else statement.setNull(9, java.sql.Types.DOUBLE)
        statement.setString(10, fechaInicioLI)
        statement.setString(11, fechaInicioLS)
        statement.setString(12, fechaFinLI)
        statement.setString(13, fechaFinLS)
        statement.setString(14, fechaConcilLI)
        statement.setString(15, fechaConcilLS)

        val resultSet = statement.executeQuery()

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

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
                    fechaInicio = LocalDate.parse(resultSet.getString("fecha_inicio"), formatter),
                    fechaFin = LocalDate.parse(resultSet.getString("fecha_fin"), formatter),
                    fechaConcil = LocalDate.parse(resultSet.getString("fecha_conciliacion"), formatter)
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