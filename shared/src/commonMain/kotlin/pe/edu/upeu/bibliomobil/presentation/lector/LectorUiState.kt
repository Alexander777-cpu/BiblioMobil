package pe.edu.upeu.bibliomobil.presentation.lector

data class LectorUiState(
    val fase: Fase = Fase.Cargando,
    val formulario: FormularioLector = FormularioLector(),
    val registrando: Boolean = false,
    val mensajeExito: String? = null
) {
    sealed interface Fase {
        data object Cargando : Fase
        data object SinLectores : Fase
        data class ConLectores(val lectores: List<LectorUi>) : Fase
        data class Error(val mensaje: String) : Fase
    }
}

data class FormularioLector(
    val nombre: String = "",
    val correo: String = "",
    val telefono: String = "",
    val errorNombre: String? = null,
    val errorCorreo: String? = null,
    val errorTelefono: String? = null
)