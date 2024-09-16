package Views

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun ConfirmDeleteAnimalDialog(
    colors: RefugioColorPalette,
    animalName: String,
    onDismissRequest: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colors.surface
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Eliminar Animal",
                    style = MaterialTheme.typography.h6,
                    color = colors.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "¿Estás seguro de que quieres eliminar a $animalName?",
                    style = MaterialTheme.typography.body1,
                    color = colors.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        onConfirmDelete()
                        onDismissRequest()
                    }) {
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}

@Composable
fun ConfirmDeleteActividadDialog(
    colors: RefugioColorPalette,
    actividCod: Int,
    onDismissRequest: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colors.surface
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Eliminar Actividad",
                    style = MaterialTheme.typography.h6,
                    color = colors.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "¿Estás seguro de que quieres eliminar la actividad ${actividCod}?",
                    style = MaterialTheme.typography.body1,
                    color = colors.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        onConfirmDelete()
                        onDismissRequest()
                    }) {
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}

@Composable
fun ConfirmDeleteContratoDialog(
    colors: RefugioColorPalette,
    text: String,
    contratoId: Int,
    onDismissRequest: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colors.surface
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text= "Eliminar Contrato",
                    style = MaterialTheme.typography.h6,
                    color = colors.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "¿Estás seguro de que quieres eliminar el contrato de $text ${contratoId}?",
                    style = MaterialTheme.typography.body1,
                    color = colors.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        onConfirmDelete()
                        onDismissRequest()
                    }) {
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}
@Composable
fun ConfirmDeleteContratadoServicioDialog(
    colors: RefugioColorPalette,
    title: String,
    text: String,
    contrId: Int,
    onDismissRequest: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colors.surface
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text= title,
                    style = MaterialTheme.typography.h6,
                    color = colors.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "¿Está seguro de que quiere eliminar el $text ${contrId}?",
                    style = MaterialTheme.typography.body1,
                    color = colors.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        onConfirmDelete()
                        onDismissRequest()
                    }) {
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}

@Composable
fun ConfirmDeleteContratoWithActDialog(
    colors: RefugioColorPalette,
    title: String,
    text: String,
    contrId: Int,
    onDismissRequest: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colors.surface
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text= title,
                    style = MaterialTheme.typography.h6,
                    color = colors.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text=text,
                    style = MaterialTheme.typography.body1,
                    color = colors.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        onConfirmDelete()
                        onDismissRequest()
                    }) {
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}