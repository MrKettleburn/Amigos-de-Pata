package Views

data class AnimalTableRow(
    val id: String,
    val nombreAnim: String,
    val mainAttributes: Map<String, String>,
    val expandedAttributes: Map<String, String>
)

data class ActividadTableRow(
    val id: String,
    val mainAttributes: Map<String, String>,
    val expandedAttributes: Map<String, String>
)

data class ContratoTableRow(
    val id: String,
    val mainAttributes: Map<String, String>,
    val expandedAttributes: Map<String, String>
)
