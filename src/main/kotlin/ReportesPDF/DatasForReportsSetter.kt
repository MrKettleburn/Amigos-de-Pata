package ReportesPDF

import Class_DB.ActividadDB
import Class_DB.AnimalDB
import Class_DB.ContratoDB
import Database.Database
import Utiles.estimarMantenimientoSeisMeses
import Utiles.estimarMantenimientoSeisMeses
import java.time.LocalDateTime

suspend fun generarReporteContratosVeterinarios(destination: String, fechaYhora: LocalDateTime) {
    // Obtener los contratos desde la base de datos
    val contratos = ContratoDB.getContratosVeterinariosFilter(
        codigo = null,
        precioLI = null,
        precioLS = null,
        nombreVet = null,
        clinicaVet = null,
        provinciaVet = null,
        especialidad = null,
        modalidadServVet = null,
        fechaInicioLI = null,
        fechaInicioLS = null,
        fechaFinLI = null,
        fechaFinLS = null,
        fechaConcilLI = null,
        fechaConcilLS = null
    )

    // Crear el PDF con los contratos obtenidos
    createPdfContratosVeterinarios(destination, contratos, fechaYhora)
}

suspend fun generarReporteActividadesDeUnAnimal(idAnimal: Int): Double{

    val animal = AnimalDB.getAnimalesFilter(idAnimal,null,null,null,null,null,null)
    val actividades = ActividadDB.getActividadesReport(idAnimal)
    val mantenimientoSeisMeses = estimarMantenimientoSeisMeses(actividades)

    return mantenimientoSeisMeses
}