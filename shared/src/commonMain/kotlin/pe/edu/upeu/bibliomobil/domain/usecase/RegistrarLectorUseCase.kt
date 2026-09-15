package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository

data class ErroresDeLector(
    val nombre: String? = null,
    val correo: String? = null,
    val telefono: String? = null
) {
    val hayErrores: Boolean
        get() = nombre != null || correo != null || telefono != null
}

class LectorInvalidoException(val errores: ErroresDeLector) :
    IllegalArgumentException("El lector no cumple las reglas de validación")

class RegistrarLectorUseCase(
    private val repository: LectorRepository
) {
    private val regexCorreo = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private val regexTelefono = Regex("^\\d{6,9}$")

    suspend operator fun invoke(
        nombre: String,
        correo: String,
        telefono: String?
    ): Result<Lector> = resultadoDe {
        val errorNombre = if (nombre.isBlank()) "El nombre es obligatorio" else null

        val correoTrim = correo.trim()
        val errorCorreo = when {
            correoTrim.isBlank() -> "El correo es obligatorio"
            !regexCorreo.matches(correoTrim) -> "El correo no tiene un formato válido"
            else -> null
        }

        val telefonoTrim = telefono?.trim()
        val errorTelefono = when {
            telefonoTrim.isNullOrBlank() -> null
            !regexTelefono.matches(telefonoTrim) -> "El teléfono debe tener entre 6 y 9 dígitos"
            else -> null
        }

        val errores = ErroresDeLector(
            nombre = errorNombre,
            correo = errorCorreo,
            telefono = errorTelefono
        )

        if (errores.hayErrores) {
            throw LectorInvalidoException(errores)
        }

        val lector = Lector(
            id = 0L,
            nombre = nombre.trim(),
            correo = correoTrim,
            telefono = if (telefonoTrim.isNullOrBlank()) null else telefonoTrim
        )

        repository.registrar(lector)
    }
}