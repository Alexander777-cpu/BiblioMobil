package pe.edu.upeu.bibliomobil.presentation.lector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import pe.edu.upeu.bibliomobil.presentation.components.EstadoVacio
import pe.edu.upeu.bibliomobil.presentation.components.MensajeExito
import pe.edu.upeu.bibliomobil.presentation.components.ValidatedTextField

@Composable
fun LectorScreen(
    viewModel: LectorViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Registrar lector",
                        style = MaterialTheme.typography.titleMedium
                    )

                    ValidatedTextField(
                        value = state.formulario.nombre,
                        onValueChange = viewModel::onNombreChange,
                        label = "Nombre completo",
                        error = state.formulario.errorNombre,
                        enabled = !state.registrando
                    )

                    ValidatedTextField(
                        value = state.formulario.correo,
                        onValueChange = viewModel::onCorreoChange,
                        label = "Correo electrónico",
                        error = state.formulario.errorCorreo,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        enabled = !state.registrando
                    )

                    ValidatedTextField(
                        value = state.formulario.telefono,
                        onValueChange = viewModel::onTelefonoChange,
                        label = "Teléfono (opcional)",
                        error = state.formulario.errorTelefono,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        enabled = !state.registrando
                    )

                    Button(
                        onClick = viewModel::registrar,
                        enabled = !state.registrando,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (state.registrando) "Registrando..." else "Registrar")
                    }

                    if (state.mensajeExito != null) {
                        MensajeExito(mensaje = state.mensajeExito!!)
                    }
                }
            }
        }

        when (val fase = state.fase) {
            is LectorUiState.Fase.Cargando -> {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Cargando cartera de lectores...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            is LectorUiState.Fase.SinLectores -> {
                item {
                    EstadoVacio(
                        icono = Icons.Default.Person,
                        titulo = "Todavía no hay lectores",
                        descripcion = "Registre al primero con el formulario de arriba."
                    )
                }
            }

            is LectorUiState.Fase.ConLectores -> {
                val cantidad = fase.lectores.size
                val tituloConteo = if (cantidad == 1) "1 lector" else "$cantidad lectores"

                item {
                    Text(
                        text = "Cartera de lectores ($tituloConteo)",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                items(fase.lectores, key = { it.id }) { lector ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = lector.nombre,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = lector.correo,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Teléfono: ${lector.telefono}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
            }

            is LectorUiState.Fase.Error -> {
                item {
                    EstadoVacio(
                        icono = Icons.Default.Warning,
                        titulo = "Error de carga",
                        descripcion = fase.mensaje,
                        colorIcono = MaterialTheme.colorScheme.error,
                        accion = {
                            OutlinedButton(onClick = viewModel::cargarLectores) {
                                Text("Reintentar")
                            }
                        }
                    )
                }
            }
        }
    }
}