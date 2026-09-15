package pe.edu.upeu.bibliomobil.domain.repository

import pe.edu.upeu.bibliomobil.domain.model.Libro

/**
 * Contrato para la gestión del catálogo de libros de la biblioteca.
 */
interface LibroRepository {
    /**
     * Consulta y devuelve la lista completa de libros del catálogo.
     */
    suspend fun listar(): List<Libro>

    /**
     * Registra un nuevo libro en el catálogo y devuelve la entidad con su ID asignado.
     */
    suspend fun registrar(libro: Libro): Libro
}