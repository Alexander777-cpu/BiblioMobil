package pe.edu.upeu.bibliomobil.presentation.lector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.bibliomobil.domain.usecase.LectorInvalidoException
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLectoresUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLectorUseCase

class LectorViewModel(
    private val registrarLector: RegistrarLectorUseCase,
    private val listarLectores: ListarLectoresUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LectorUiState())
    val uiState: StateFlow<LectorUiState> = _uiState.asStateFlow()

    init {
        cargarLectores()
    }

    fun cargarLectores() {
        viewModelScope.launch {
            _uiState.update { it.copy(fase = LectorUiState.Fase.Cargando) }
            val resultado = listarLectores()
            resultado.onSuccess { lectores ->
                _uiState.update { estado ->
                    estado.copy(
                        fase = if (lectores.isEmpty()) {
                            LectorUiState.Fase.SinLectores
                        } else {
                            LectorUiState.Fase.ConLectores(lectores.map { it.aUi() })
                        }
                    )
                }
            }.onFailure { excepcion ->
                _uiState.update { estado ->
                    estado.copy(
                        fase = LectorUiState.Fase.Error(
                            excepcion.message ?: "No se pudo cargar la cartera de lectores"
                        )
                    )
                }
            }
        }
    }

    fun onNombreChange(nuevoNombre: String) {
        _uiState.update {
            it.copy(formulario = it.formulario.copy(nombre = nuevoNombre, errorNombre = null))
        }
    }

    fun onCorreoChange(nuevoCorreo: String) {
        _uiState.update {
            it.copy(formulario = it.formulario.copy(correo = nuevoCorreo, errorCorreo = null))
        }
    }

    fun onTelefonoChange(nuevoTelefono: String) {
        _uiState.update {
            it.copy(formulario = it.formulario.copy(telefono = nuevoTelefono, errorTelefono = null))
        }
    }

    fun registrar() {
        if (_uiState.value.registrando) return

        val form = _uiState.value.formulario
        viewModelScope.launch {
            _uiState.update { it.copy(registrando = true, mensajeExito = null) }
            val resultado = registrarLector(
                nombre = form.nombre,
                correo = form.correo,
                telefono = form.telefono
            )

            resultado.onSuccess { lectorRegistrado ->
                _uiState.update {
                    it.copy(
                        registrando = false,
                        formulario = FormularioLector(),
                        mensajeExito = "Lector \"${lectorRegistrado.nombre}\" registrado correctamente"
                    )
                }
                cargarLectores()
            }.onFailure { excepcion ->
                if (excepcion is LectorInvalidoException) {
                    _uiState.update { estado ->
                        estado.copy(
                            registrando = false,
                            formulario = estado.formulario.copy(
                                errorNombre = excepcion.errores.nombre,
                                errorCorreo = excepcion.errores.correo,
                                errorTelefono = excepcion.errores.telefono
                            )
                        )
                    }
                } else {
                    _uiState.update { estado ->
                        estado.copy(
                            registrando = false,
                            fase = LectorUiState.Fase.Error(
                                excepcion.message ?: "No se pudo cargar la cartera de lectores"
                            )
                        )
                    }
                }
            }
        }
    }
}