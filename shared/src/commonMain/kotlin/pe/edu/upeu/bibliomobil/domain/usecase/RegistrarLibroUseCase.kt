package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

data class ErroresDeLibro(
    val titulo: String? = null,
    val autor: String? = null,
    val anio: String? = null,
    val ejemplares: String? = null
) {
    val hayErrores: Boolean
        get() = titulo != null || autor != null || anio != null || ejemplares != null
}

class LibroInvalidoException(val errores: ErroresDeLibro) :
    IllegalArgumentException("El libro no cumple las reglas de validación")

class RegistrarLibroUseCase(
    private val repository: LibroRepository
) {
    suspend operator fun invoke(
        titulo: String,
        autor: String,
        anio: String,
        ejemplares: String
    ): Result<Libro> = resultadoDe {
        val errorTitulo = if (titulo.isBlank()) "El título es obligatorio" else null
        val errorAutor = if (autor.isBlank()) "El autor es obligatorio" else null

        val anioInt = anio.trim().toIntOrNull()
        val errorAnio = when {
            anio.isBlank() -> "El año es obligatorio"
            anioInt == null -> "El año debe ser un número entero"
            anioInt !in Libro.ANIO_MINIMO..Libro.ANIO_MAXIMO ->
                "El año debe estar entre ${Libro.ANIO_MINIMO} y ${Libro.ANIO_MAXIMO}"
            else -> null
        }

        val ejemplaresInt = ejemplares.trim().toIntOrNull()
        val errorEjemplares = when {
            ejemplares.isBlank() -> "Los ejemplares son obligatorios"
            ejemplaresInt == null -> "Los ejemplares deben ser un número entero"
            ejemplaresInt < 0 -> "Los ejemplares no pueden ser negativos"
            else -> null
        }

        val errores = ErroresDeLibro(
            titulo = errorTitulo,
            autor = errorAutor,
            anio = errorAnio,
            ejemplares = errorEjemplares
        )

        if (errores.hayErrores) {
            throw LibroInvalidoException(errores)
        }

        val libro = Libro(
            id = 0L,
            titulo = titulo.trim(),
            autor = autor.trim(),
            anio = anioInt!!,
            ejemplares = ejemplaresInt!!
        )

        repository.registrar(libro)
    }
}