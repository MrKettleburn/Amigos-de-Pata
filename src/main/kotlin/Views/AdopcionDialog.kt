package Views

import Class_DB.ActividadDB
import Class_DB.AnimalDB
import Models.Animal
import Utiles.estimarMantenimientoSeisMeses
import Utiles.estimarPrecioDeAdopcion
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource

@Composable
fun DialogAdopcion(
    colors: RefugioColorPalette,
    animal: Animal,
    precioAdopcion: Double,
    onDismissRequest: () -> Unit,
    onConfirmAdoption: () -> Unit
) {


    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(8.dp),
            color = colors.menuBackground
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo
                Image(
                    painter = painterResource("Adopt.png"),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(150.dp)
                        .padding(bottom = 16.dp)
                )

                // Texto de confirmación
                Text(
                    text = "A continuación acepta adoptar a ${animal.nombre} por",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "\$${precioAdopcion}",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Botones
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Volver")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = onConfirmAdoption) {
                        Text("Aceptar")
                    }
                }
            }
        }
    }
}