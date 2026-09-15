package pe.edu.upeu.bibliomobil.presentation.libro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
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
fun LibroScreen(
    viewModel: LibroViewModel,
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
                        text = "Registrar libro",
                        style = MaterialTheme.typography.titleMedium
                    )

                    ValidatedTextField(
                        value = state.formulario.titulo,
                        onValueChange = viewModel::onTituloChange,
                        label = "Título",
                        error = state.formulario.errorTitulo,
                        enabled = !state.registrando
                    )

                    ValidatedTextField(
                        value = state.formulario.autor,
                        onValueChange = viewModel::onAutorChange,
                        label = "Autor",
                        error = state.formulario.errorAutor,
                        enabled = !state.registrando
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ValidatedTextField(
                            value = state.formulario.anio,
                            onValueChange = viewModel::onAnioChange,
                            label = "Año",
                            error = state.formulario.errorAnio,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            enabled = !state.registrando,
                            modifier = Modifier.weight(1f)
                        )

                        ValidatedTextField(
                            value = state.formulario.ejemplares,
                            onValueChange = viewModel::onEjemplaresChange,
                            label = "Ejemplares",
                            error = state.formulario.errorEjemplares,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            enabled = !state.registrando,
                            modifier = Modifier.weight(1f)
                        )
                    }

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
            is LibroUiState.Fase.Cargando -> {
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
                            text = "Cargando catálogo...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            is LibroUiState.Fase.SinLibros -> {
                item {
                    EstadoVacio(
                        icono = Icons.Default.MenuBook,
                        titulo = "Todavía no hay libros",
                        descripcion = "Registre el primero con el formulario de arriba."
                    )
                }
            }

            is LibroUiState.Fase.ConLibros -> {
                val cantidad = fase.libros.size
                val tituloConteo = if (cantidad == 1) "1 libro" else "$cantidad libros"

                item {
                    Text(
                        text = "Catálogo ($tituloConteo)",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                items(fase.libros, key = { it.id }) { libro ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = libro.titulo,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = libro.autor,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = libro.lineaSecundaria,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }

                            if (libro.requiereReposicion) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = MaterialTheme.colorScheme.errorContainer,
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "Pocos ejemplares",
                                        color = MaterialTheme.colorScheme.onErrorContainer,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }
                    }
                }
            }

            is LibroUiState.Fase.Error -> {
                item {
                    EstadoVacio(
                        icono = Icons.Default.Warning,
                        titulo = "Error de carga",
                        descripcion = fase.mensaje,
                        colorIcono = MaterialTheme.colorScheme.error,
                        accion = {
                            OutlinedButton(onClick = viewModel::cargarLibros) {
                                Text("Reintentar")
                            }
                        }
                    )
                }
            }
        }
    }
}