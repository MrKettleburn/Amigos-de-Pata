package Views

import Models.Animal
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

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
                    text = String.format("%.2f", precioAdopcion),
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(bottom = 24.dp),
                    fontSize = 20.sp
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