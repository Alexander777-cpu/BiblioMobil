package pe.edu.upeu.bibliomobil.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository
import kotlin.random.Random

class LibroRepositorioEnMemoria : LibroRepository {

    private val libros = mutableListOf<Libro>()
    private var proximoId: Long = 1L
    private val mutex = Mutex()

    override suspend fun listar(): List<Libro> {
        delay(Random.nextLong(300, 801))
        return mutex.withLock {
            libros.toList()
        }
    }

    override suspend fun registrar(libro: Libro): Libro {
        delay(Random.nextLong(300, 801))
        return mutex.withLock {
            val libroConId = libro.copy(id = proximoId++)
            libros.add(libroConId)
            libroConId
        }
    }
}