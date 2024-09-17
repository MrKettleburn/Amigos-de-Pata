
import Views.RefugioApp
import Views.RefugioColorPalette
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.GraphicsDevice
import java.awt.GraphicsEnvironment

@Composable
fun App(window: java.awt.Window) {
    var isLoggedIn by remember { mutableStateOf(false) }

    // Instancia de la paleta de colores
    val colors = RefugioColorPalette()

    if (isLoggedIn) {
        // Cambia la ventana a pantalla completa
        LaunchedEffect(Unit) {
            setFullScreen(window)
        }
    }

    RefugioApp(
        isLoggedIn = isLoggedIn,
        onLogout = { isLoggedIn = false }, // Controla el logout
        onLoginSuccess = { isLoggedIn = true } // Controla el login exitoso
    )
}


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Login",
        state = androidx.compose.ui.window.WindowState(width = 700.dp, height = 600.dp)
    ) {
        App(window)
    }


//    LaunchedEffect(Unit) {
//        generarReporteContratosVeterinarios("C:\\Users\\ruben\\IdeaProjects\\Amigos_de_Pata\\src\\main\\kotlin\\PruebaPDF.pdf", LocalDateTime.now())
//    }
//
//    LaunchedEffect(Unit) {
//        println(generarReporteActividadesDeUnAnimal(9))
//    }
//    val actividades = mutableListOf<ActividadReporte>()
//    val dbConnection: Connection = Database.connect()
//    val statement = dbConnection.prepareStatement(
//        //"SELECT a.id_actividad, a.id_animal, a.fecha , a.hora, a.tipo_actividad, a.descrip_act, c.id_contrato, c.tipo_contrato, a.costo FROM actividad a INNER JOIN contrato c ON a.id_contrato=c.id_contrato WHERE a.id_animal = 9"
//        "SELECT * FROM reporte_actividades_animal(9)"
//    )
//
//    val resultSet = statement.executeQuery()
//    while (resultSet.next()) {
//        actividades.add(
//            ActividadReporte(
//                codigo = resultSet.getInt("id_actividad"),
//                codigoAnim = resultSet.getInt("id_animal"),
//                fecha = resultSet.getDate("fecha").toLocalDate(),
//                hora = resultSet.getTime("hora").toLocalTime(),
//                tipo = resultSet.getString("tipo_actividad"),
//                codigoContr = resultSet.getInt("id_contrato"),
//                detalles = resultSet.getString("detalles"),
//                descrip = resultSet.getString("descrip_act"),
//                costo = resultSet.getDouble("costo")
//            )
//        )
//    }
//    resultSet.close()
//    statement.close()
//    dbConnection.close()
//
//    val formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy")
//    val actividadesMesPasado = sacarActividadesMesPasado(actividades)

//    println("TODAS LAS ACTIVIDADES")
//    actividades.forEach { act -> println(act.fecha.format(formatterDate)) }

//    println("\n\n\n")
//    println("ACTIVIDADES MES PASADO")
//    actividadesMesPasado.forEach { act -> println(act.fecha.format(formatterDate) + "    " + act.costo) }
//
//    val hoy = LocalDate.now()
//    val hace30Dias = hoy.minusDays(30)
//
//    println(hoy.format(formatterDate))
//    println(hace30Dias.format(formatterDate))
}

fun setFullScreen(window: java.awt.Window) {
    val graphicsDevice: GraphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice
    graphicsDevice.fullScreenWindow = window // Configurar la ventana a pantalla completa
}
