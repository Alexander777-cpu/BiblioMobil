package pe.edu.upeu.bibliomobil.presentation.libro

import pe.edu.upeu.bibliomobil.domain.model.Libro

data class LibroUi(
    val id: Long,
    val titulo: String,
    val autor: String,
    val lineaSecundaria: String,
    val requiereReposicion: Boolean
)

fun Libro.aUi(): LibroUi {
    val sufijoEjemplares = if (ejemplares == 1) "1 ejemplar" else "$ejemplares ejemplares"
    return LibroUi(
        id = id,
        titulo = titulo,
        autor = autor,
        lineaSecundaria = "$anio · $sufijoEjemplares",
        requiereReposicion = requiereReposicion
    )
}