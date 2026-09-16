package pe.edu.upeu.bibliomobil

import pe.edu.upeu.bibliomobil.domain.model.DetallePrestamo
import pe.edu.upeu.bibliomobil.domain.model.Libro
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PrestamoTest {

    private val libroDummy = Libro(1L, "Libro", "Autor", 2020, 5)

    @Test
    fun diasPrestamoValidoExitoso() {
        val detalle = DetallePrestamo(libroDummy, 10)
        assertEquals(10, detalle.dias)
    }

    @Test
    fun diasPrestamoCeroLanzaExcepcion() {
        assertFailsWith<IllegalArgumentException> {
            DetallePrestamo(libroDummy, 0)
        }
    }

    @Test
    fun diasPrestamoExcedeMaximoLanzaExcepcion() {
        assertFailsWith<IllegalArgumentException> {
            DetallePrestamo(libroDummy, 16)
        }
    }

    @Test
    fun calculoDeMultaCorrecto() {
        val detalle = DetallePrestamo(libroDummy, 7)
        val multa = detalle.multaPorRetraso(3)
        assertEquals(4.50, multa)
    }
}