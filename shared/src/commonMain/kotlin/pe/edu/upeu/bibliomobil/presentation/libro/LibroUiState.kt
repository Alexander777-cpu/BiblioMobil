package pe.edu.upeu.bibliomobil.presentation.libro

data class LibroUiState(
    val fase: Fase = Fase.Cargando,
    val formulario: FormularioLibro = FormularioLibro(),
    val registrando: Boolean = false,
    val mensajeExito: String? = null
) {
    sealed interface Fase {
        data object Cargando : Fase
        data object SinLibros : Fase
        data class ConLibros(val libros: List<LibroUi>) : Fase
        data class Error(val mensaje: String) : Fase
    }
}

data class FormularioLibro(
    val titulo: String = "",
    val autor: String = "",
    val anio: String = "",
    val ejemplares: String = "",
    val errorTitulo: String? = null,
    val errorAutor: String? = null,
    val errorAnio: String? = null,
    val errorEjemplares: String? = null
)