package Utiles

import Models.Actividad
import Models.ActividadReporte
import java.time.LocalDate

fun estimarMantenimientoSeisMeses(actividades: List<ActividadReporte>): Double {
        var suma: Double = 0.0

        val actvidadesMesPasado = sacarActividadesMesPasado(actividades)

        actvidadesMesPasado.forEach { act ->  suma += act.costo}

        return suma/30 * 180
}

fun sacarActividadesMesPasado(actividades: List<ActividadReporte>): List<ActividadReporte> {
        val hoy = LocalDate.now()
        val hace30Dias = hoy.minusDays(30)

        return actividades.filter { (it.fecha.isAfter(hace30Dias) &&  it.fecha.isBefore(hoy)) || it.fecha.isEqual(hace30Dias)}
}
