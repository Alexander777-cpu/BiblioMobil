package pe.edu.upeu.bibliomobil.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val ruta: String,
    val titulo: String,
    val icono: ImageVector
) {
    data object Inicio : Screen("inicio", "BiblioMobil", Icons.Default.Home)
    data object Libros : Screen("libros", "Catálogo de Libros", Icons.Default.MenuBook)
    data object Lectores : Screen("lectores", "Cartera de Lectores", Icons.Default.Person)
    data object Prestamos : Screen("prestamos", "Préstamos", Icons.Default.Bookmark)
}

// Lista única exigida por el examen para alimentar Drawer y Barra Superior
val DESTINOS: List<Screen> = listOf(
    Screen.Inicio,
    Screen.Libros,
    Screen.Lectores,
    Screen.Prestamos
)

// Saver para garantizar que pantallaActual sobrevive a la rotación
val ScreenSaver: Saver<Screen, String> = Saver(
    save = { it.ruta },
    restore = { rutaGuardada ->
        DESTINOS.firstOrNull { it.ruta == rutaGuardada } ?: Screen.Inicio
    }
)