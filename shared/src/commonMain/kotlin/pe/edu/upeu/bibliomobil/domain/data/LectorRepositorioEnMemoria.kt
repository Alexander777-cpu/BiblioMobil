package pe.edu.upeu.bibliomobil.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository
import kotlin.random.Random

class LectorRepositorioEnMemoria : LectorRepository {

    private val lectores = mutableListOf<Lector>()
    private var proximoId: Long = 1L
    private val mutex = Mutex()

    override suspend fun listar(): List<Lector> {
        delay(Random.nextLong(300, 801))
        return mutex.withLock {
            lectores.toList()
        }
    }

    override suspend fun registrar(lector: Lector): Lector {
        delay(Random.nextLong(300, 801))
        return mutex.withLock {
            val lectorConId = lector.copy(id = proximoId++)
            lectores.add(lectorConId)
            lectorConId
        }
    }
}