package pe.edu.upeu.bibliomobil.presentation.libro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.bibliomobil.domain.usecase.LibroInvalidoException
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLibrosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLibroUseCase

class LibroViewModel(
    private val registrarLibro: RegistrarLibroUseCase,
    private val listarLibros: ListarLibrosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibroUiState())
    val uiState: StateFlow<LibroUiState> = _uiState.asStateFlow()

    init {
        cargarLibros()
    }

    fun cargarLibros() {
        viewModelScope.launch {
            _uiState.update { it.copy(fase = LibroUiState.Fase.Cargando) }
            val resultado = listarLibros()
            resultado.onSuccess { libros ->
                _uiState.update { estado ->
                    estado.copy(
                        fase = if (libros.isEmpty()) {
                            LibroUiState.Fase.SinLibros
                        } else {
                            LibroUiState.Fase.ConLibros(libros.map { it.aUi() })
                        }
                    )
                }
            }.onFailure { excepcion ->
                _uiState.update { estado ->
                    estado.copy(
                        fase = LibroUiState.Fase.Error(
                            excepcion.message ?: "No se pudo cargar el catálogo"
                        )
                    )
                }
            }
        }
    }

    fun onTituloChange(nuevoTitulo: String) {
        _uiState.update {
            it.copy(formulario = it.formulario.copy(titulo = nuevoTitulo, errorTitulo = null))
        }
    }

    fun onAutorChange(nuevoAutor: String) {
        _uiState.update {
            it.copy(formulario = it.formulario.copy(autor = nuevoAutor, errorAutor = null))
        }
    }

    fun onAnioChange(nuevoAnio: String) {
        _uiState.update {
            it.copy(formulario = it.formulario.copy(anio = nuevoAnio, errorAnio = null))
        }
    }

    fun onEjemplaresChange(nuevosEjemplares: String) {
        _uiState.update {
            it.copy(formulario = it.formulario.copy(ejemplares = nuevosEjemplares, errorEjemplares = null))
        }
    }

    fun registrar() {
        if (_uiState.value.registrando) return

        val form = _uiState.value.formulario
        viewModelScope.launch {
            _uiState.update { it.copy(registrando = true, mensajeExito = null) }
            val resultado = registrarLibro(
                titulo = form.titulo,
                autor = form.autor,
                anio = form.anio,
                ejemplares = form.ejemplares
            )

            resultado.onSuccess { libroRegistrado ->
                _uiState.update {
                    it.copy(
                        registrando = false,
                        formulario = FormularioLibro(),
                        mensajeExito = "Libro \"${libroRegistrado.titulo}\" registrado correctamente"
                    )
                }
                cargarLibros()
            }.onFailure { excepcion ->
                if (excepcion is LibroInvalidoException) {
                    _uiState.update { estado ->
                        estado.copy(
                            registrando = false,
                            formulario = estado.formulario.copy(
                                errorTitulo = excepcion.errores.titulo,
                                errorAutor = excepcion.errores.autor,
                                errorAnio = excepcion.errores.anio,
                                errorEjemplares = excepcion.errores.ejemplares
                            )
                        )
                    }
                } else {
                    _uiState.update { estado ->
                        estado.copy(
                            registrando = false,
                            fase = LibroUiState.Fase.Error(excepcion.message ?: "No se pudo cargar el catálogo")
                        )
                    }
                }
            }
        }
    }
}