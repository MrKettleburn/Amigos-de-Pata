package ReportesPDF

import Class_DB.*
import Utiles.calcularMontoTotalPorAdopciones
import Utiles.calcularMontoTotalPorDonaciones
import Utiles.estimarMantenimientoSeisMeses
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime


fun generarReporteContratosVeterinarios(coroutineScope: CoroutineScope, fechaYhora: LocalDateTime) {
    val destination: String = "C:\\Users\\ruben\\IdeaProjects\\Amigos_de_Pata\\src\\main\\kotlin\\Contratos_Conciliados_Con_Veterinarios.pdf"

    coroutineScope.launch {
        val contratos = ContratoDB.getContratosVeterinariosForReport()

        createPdfContratosVeterinarios(destination, contratos, fechaYhora)
    }
}


fun generarReporteContratosTransporte(coroutineScope: CoroutineScope, fechaYhora: LocalDateTime) {

    val destination: String = "C:\\Users\\ruben\\IdeaProjects\\Amigos_de_Pata\\src\\main\\kotlin\\Contratos_Conciliados_Con_Transportes.pdf"

    coroutineScope.launch {
        val contratos = ContratoDB.getContratosTransporteForReport()

        createPdfContratosTransporte(destination, contratos, fechaYhora)
    }
}


fun generarReporteContratosProveedoresAlim(coroutineScope: CoroutineScope, fechaYhora: LocalDateTime) {

    val destination: String = "C:\\Users\\ruben\\IdeaProjects\\Amigos_de_Pata\\src\\main\\kotlin\\Contratos_Conciliados_Con_Proveedores_de_Alimentos.pdf"

    coroutineScope.launch {
        val contratos = ContratoDB.getContratosProveedoresAlimForReport()

        createPdfContratosProveedoresAlim(destination, contratos, fechaYhora)
    }
}


fun generarReporteVeterinariosActivos(coroutineScope: CoroutineScope, clinica: String?, provincia: String?, fechaYhora: LocalDateTime) {

    val destination: String = "C:\\Users\\ruben\\IdeaProjects\\Amigos_de_Pata\\src\\main\\kotlin\\Veterinarios_Activos.pdf"

    coroutineScope.launch {
        val veterinarios = ContratadosDB.getVeterinariosActivosForReport(clinica, provincia)

        createPdfVeterinariosActivos(destination, clinica, provincia, veterinarios, fechaYhora)
    }
}


fun generarReporteActividadesDeUnAnimal(coroutineScope: CoroutineScope,idAnimal: Int,fechaYhora: LocalDateTime){

    coroutineScope.launch{
    val animales = AnimalDB.getAnimalesFilter(idAnimal, null, null, null, null, null, null)
        val animal = animales.get(0)

        val destination: String = "C:\\Users\\ruben\\IdeaProjects\\Amigos_de_Pata\\src\\main\\kotlin\\Actividades_Animal.pdf"

        val actividades = ActividadDB.getActividadesReport(idAnimal)
        val mantenimientoSeisMeses = estimarMantenimientoSeisMeses(actividades)

        createPdfActividadesDeUnAnimal(destination, animal,actividades,mantenimientoSeisMeses, fechaYhora)
    }
}


fun generarReportePlanDeIngresos(coroutineScope: CoroutineScope,fechaYhora: LocalDateTime){


    coroutineScope.launch{
        val destination: String = "C:\\Users\\ruben\\IdeaProjects\\Amigos_de_Pata\\src\\main\\kotlin\\Plan_de_Ingresos_por_Donaciones_y_Adopciones.pdf"
        val animalesAdoptados = AnimalDB.getAnimalesAdoptForReport()
        val montoTotalPorAdopciones = calcularMontoTotalPorAdopciones(animalesAdoptados)
        val donaciones = DonacionesDB.getDonacionesForReport()
        val montoTotalPorDonaciones = calcularMontoTotalPorDonaciones(donaciones)

        createPdfPlanDeIngresos(destination,animalesAdoptados,montoTotalPorAdopciones,donaciones,montoTotalPorDonaciones, fechaYhora)
    }
}