package pe.edu.upeu.bibliomobil

import pe.edu.upeu.bibliomobil.domain.model.Lector
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class LectorTest {

    @Test
    fun crearLectorValidoConTelefono() {
        val lector = Lector(1L, "Juan Perez", "juan@test.com", "987654321")
        assertEquals("Juan Perez", lector.nombre)
        assertEquals("987654321", lector.telefono)
    }

    @Test
    fun crearLectorValidoSinTelefono() {
        val lector = Lector(1L, "Ana Torres", "ana@test.com", null)
        assertNull(lector.telefono)
    }

    @Test
    fun nombreEnBlancoLanzaExcepcion() {
        assertFailsWith<IllegalArgumentException> {
            Lector(1L, "   ", "ana@test.com", null)
        }
    }

    @Test
    fun correoEnBlancoLanzaExcepcion() {
        assertFailsWith<IllegalArgumentException> {
            Lector(1L, "Ana Torres", "", null)
        }
    }

    @Test
    fun telefonoVacioLanzaExcepcion() {
        assertFailsWith<IllegalArgumentException> {
            Lector(1L, "Ana Torres", "ana@test.com", "   ")
        }
    }
}