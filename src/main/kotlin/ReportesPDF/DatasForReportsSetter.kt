package ReportesPDF

import Class_DB.ActividadDB
import Class_DB.AnimalDB
import Class_DB.ContratoDB
import Class_DB.DonacionesDB
import Database.Database
import Utiles.calcularMontoTotalPorAdopciones
import Utiles.calcularMontoTotalPorDonaciones
import Utiles.estimarMantenimientoSeisMeses
import Utiles.estimarMantenimientoSeisMeses
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import java.time.LocalDateTime

@Composable
fun generarReporteContratosVeterinarios( fechaYhora: LocalDateTime) {

    val destination: String = "C:\\Users\\ruben\\IdeaProjects\\Amigos_de_Pata\\src\\main\\kotlin\\Contratos_Conciliados_Con_Veterinarios.pdf"

    LaunchedEffect(Unit) {
        val contratos = ContratoDB.getContratosVeterinariosForReport()

        createPdfContratosVeterinarios(destination, contratos, fechaYhora)
    }
}

@Composable
fun generarReporteContratosTransporte(fechaYhora: LocalDateTime) {

    val destination: String = "C:\\Users\\ruben\\IdeaProjects\\Amigos_de_Pata\\src\\main\\kotlin\\Contratos_Conciliados_Con_Transportes.pdf"

    LaunchedEffect(Unit) {
        val contratos = ContratoDB.getContratosTransporteForReport()

        createPdfContratosTransporte(destination, contratos, fechaYhora)
    }
}

@Composable
fun generarReporteContratosProveedoresAlim(fechaYhora: LocalDateTime) {

    val destination: String = "C:\\Users\\ruben\\IdeaProjects\\Amigos_de_Pata\\src\\main\\kotlin\\Contratos_Conciliados_Con_Proveedores_de_Alimentos.pdf"

    LaunchedEffect(Unit) {
        val contratos = ContratoDB.getContratosProveedoresAlimForReport()

        createPdfContratosProveedoresAlim(destination, contratos, fechaYhora)
    }
}
@Composable
fun generarReporteActividadesDeUnAnimal(idAnimal: Int,fechaYhora: LocalDateTime){


    LaunchedEffect(Unit) {
        val animales = AnimalDB.getAnimalesFilter(idAnimal, null, null, null, null, null, null)
        val animal = animales.get(0)

        val destination: String = "C:\\Users\\ruben\\IdeaProjects\\Amigos_de_Pata\\src\\main\\kotlin\\Actividades_Animal.pdf"

        val actividades = ActividadDB.getActividadesReport(idAnimal)
        val mantenimientoSeisMeses = estimarMantenimientoSeisMeses(actividades)

        createPdfActividadesDeUnAnimal(destination, animal,actividades,mantenimientoSeisMeses, fechaYhora)
    }
}

@Composable
fun generarReportePlanDeIngresos(fechaYhora: LocalDateTime){


    LaunchedEffect(Unit) {

        val destination: String = "C:\\Users\\ruben\\IdeaProjects\\Amigos_de_Pata\\src\\main\\kotlin\\Plan_de_Ingresos_por_Donaciones_y_Adopciones.pdf"
        val animalesAdoptados = AnimalDB.getAnimalesAdoptForReport()
        val montoTotalPorAdopciones = calcularMontoTotalPorAdopciones(animalesAdoptados)
        val donaciones = DonacionesDB.getDonacionesForReport()
        val montoTotalPorDonaciones = calcularMontoTotalPorDonaciones(donaciones)

        createPdfPlanDeIngresos(destination,animalesAdoptados,montoTotalPorAdopciones,donaciones,montoTotalPorDonaciones, fechaYhora)
    }
}