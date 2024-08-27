package Utiles

import Models.*
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

fun estimarPrecioDeAdopcion(mantenimientoEnSeisMeses: Double, animal: Animal): Double {

        val razasDemandadasPerros= listOf("Labrador Retriever", "Bulldog Francés", "Golden Retriever", "Pastor Alemán", "Bulldog", "Husky Siberiano")
        val razasDemandadasGatos= listOf("Persa", "Main Coon", "Siamés", "Ragdoll", "Bengala")

        var precioInicial = mantenimientoEnSeisMeses * 0.25

        if(animal.especie=="Perro")
        {
                if(razasDemandadasPerros.contains(animal.raza))
                        precioInicial+= (precioInicial*0.05)
                else
                        precioInicial-= (precioInicial*0.05)

                if(animal.edad>8)
                        precioInicial-= (precioInicial*0.05)
        }
        else if(animal.especie=="Gato")
        {
                if(razasDemandadasGatos.contains(animal.raza))
                        precioInicial+= (precioInicial*0.05)
                else
                        precioInicial-= (precioInicial*0.05)

                if(animal.edad>11)
                        precioInicial-= (precioInicial*0.05)
        }

        return precioInicial
}

fun calcularMontoTotalPorAdopciones(animales: List<AnimalAdoptado>): Double
{
        var monto = 0.0

        animales.forEach { animal -> monto +=animal.precioAdop }

        return monto
}

fun calcularMontoTotalPorDonaciones(donaciones: List<Donacion>): Double
{
        var monto = 0.0

        donaciones.forEach { donacion -> monto +=donacion.monto }

        return monto
}