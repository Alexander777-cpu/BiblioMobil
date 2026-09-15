package pe.edu.upeu.bibliomobil.presentation.prestamo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pe.edu.upeu.bibliomobil.presentation.components.EstadoVacio

@Composable
fun PrestamosScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        EstadoVacio(
            icono = Icons.Default.Bookmark,
            titulo = "Préstamos en construcción",
            descripcion = "El módulo de préstamos estará disponible en una próxima versión."
        )
    }
}