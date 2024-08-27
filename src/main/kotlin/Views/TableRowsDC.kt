package Views

import java.time.LocalDate
import java.time.LocalTime

data class AnimalTableRow(
    val id: String,
    val nombreAnim: String,
    val especie: String,
    val raza: String,
    val edad: Int,
    val peso: Double,
    val cantDias: Int,
    val fecha_ingreso: LocalDate,
    val mainAttributes: Map<String, String>,
    val expandedAttributes: Map<String, String>
)

data class ActividadTableRow(
    val id: String,
    val codigoAnim: Int,
    val fecha: LocalDate,
    val hora: LocalTime,
    val tipo: String,
    val codigoContr: Int,
    val descrip: String,
    val mainAttributes: Map<String, String>,
    val expandedAttributes: Map<String, String>
)

data class ContratoTableRow(
    val id: String,
    val mainAttributes: Map<String, String>,
    val expandedAttributes: Map<String, String>
)

data class ServicioTableRowAlimenticio(
    val id: String,
    val precioUnit: Double,
    val tipoAlim: String,
    val mainAttributes: Map<String, String>
)

data class ServicioTableRowTransporte(
    val id: String,
    val precioUnit: Double,
    val vehiculo: String,
    val mainAttributes: Map<String, String>
)

data class ServicioVeterinarioTableRow(
    val id: String,
    val precioUnit: Double,
    val modalidad: String,
    val mainAttributes: Map<String, String>
)

data class ProveedorTableRow(
    val id: String,
    val nombre: String,
    val email: String,
    val provincia: String,
    val direccion: String,
    val telefono: String,
    val mainAttributes: Map<String, String>,
    val expandedAttributes: Map<String, String>
)


data class TransportistaTableRow(
    val id: String,
    val nombre: String,
    val email: String,
    val provincia: String,
    val direccion: String,
    val telefono: String,
    val mainAttributes: Map<String, String>,
    val expandedAttributes: Map<String, String>
)

data class VeterinarioTableRow(
    val id: String,
    val nombre: String,
    val email: String,
    val provincia: String,
    val direccion: String,
    val telefono: String,
    val especialidad: String,
    val clinica: String,
    val mainAttributes: Map<String, String>,
    val expandedAttributes: Map<String, String>
)

data class AnimalAdoptadoTableRow(
    val id: String,
    val nombre: String,
    val especie: String,
    val raza: String,
    val edad: String,
    val peso: String,
    val fecha_ingreso: String,
    val precioAdop: String,
    val nombreAdoptante: String,
    val mainAttributes: Map<String, String>,
    val expandedAttributes: Map<String, String>
)
