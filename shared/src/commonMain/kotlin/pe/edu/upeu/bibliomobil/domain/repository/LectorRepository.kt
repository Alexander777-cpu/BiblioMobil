package pe.edu.upeu.bibliomobil.domain.repository

import pe.edu.upeu.bibliomobil.domain.model.Lector

/**
 * Contrato para la gestión de la cartera de lectores de la biblioteca.
 */
interface LectorRepository {
    /**
     * Consulta y devuelve la lista completa de lectores registrados.
     */
    suspend fun listar(): List<Lector>

    /**
     * Registra un nuevo lector en la biblioteca y devuelve la entidad con su ID asignado.
     */
    suspend fun registrar(lector: Lector): Lector
}