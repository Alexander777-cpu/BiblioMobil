package pe.edu.upeu.bibliomobil

import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

class FakeLibroRepository : LibroRepository {
    var debeFallar = false
    private val libros = mutableListOf<Libro>()
    private var proximoId = 1L

    override suspend fun listar(): List<Libro> {
        if (debeFallar) throw RuntimeException("Error simulado al listar libros")
        return libros.toList()
    }

    override suspend fun registrar(libro: Libro): Libro {
        if (debeFallar) throw RuntimeException("Error simulado al registrar libro")
        val nuevo = libro.copy(id = proximoId++)
        libros.add(nuevo)
        return nuevo
    }
}

class FakeLectorRepository : LectorRepository {
    var debeFallar = false
    private val lectores = mutableListOf<Lector>()
    private var proximoId = 1L

    override suspend fun listar(): List<Lector> {
        if (debeFallar) throw RuntimeException("Error simulado al listar lectores")
        return lectores.toList()
    }

    override suspend fun registrar(lector: Lector): Lector {
        if (debeFallar) throw RuntimeException("Error simulado al registrar lector")
        val nuevo = lector.copy(id = proximoId++)
        lectores.add(nuevo)
        return nuevo
    }
}