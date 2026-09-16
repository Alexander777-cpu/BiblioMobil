package pe.edu.upeu.bibliomobil

import pe.edu.upeu.bibliomobil.domain.model.Libro
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LibroTest {

    @Test
    fun crearLibroValidoExitoso() {
        val libro = Libro(1L, "Clean Code", "Robert C. Martin", 2008, 5)
        assertEquals("Clean Code", libro.titulo)
        assertEquals(5, libro.ejemplares)
    }

    @Test
    fun tituloEnBlancoLanzaExcepcion() {
        assertFailsWith<IllegalArgumentException> {
            Libro(1L, "   ", "Autor", 2020, 5)
        }
    }

    @Test
    fun autorEnBlancoLanzaExcepcion() {
        assertFailsWith<IllegalArgumentException> {
            Libro(1L, "Título", "", 2020, 5)
        }
    }

    @Test
    fun anioMenorAlMinimoLanzaExcepcion() {
        assertFailsWith<IllegalArgumentException> {
            Libro(1L, "Título", "Autor", 1400, 5)
        }
    }

    @Test
    fun anioMayorAlMaximoLanzaExcepcion() {
        assertFailsWith<IllegalArgumentException> {
            Libro(1L, "Título", "Autor", 2030, 5)
        }
    }

    @Test
    fun ejemplaresNegativosLanzaExcepcion() {
        assertFailsWith<IllegalArgumentException> {
            Libro(1L, "Título", "Autor", 2020, -1)
        }
    }

    @Test
    fun requiereReposicionDevuelveTrueSiMenorATres() {
        val libro = Libro(1L, "Título", "Autor", 2020, 2)
        assertTrue(libro.requiereReposicion)
    }

    @Test
    fun requiereReposicionDevuelveFalseSiTresOMas() {
        val libro = Libro(1L, "Título", "Autor", 2020, 3)
        assertFalse(libro.requiereReposicion)
    }
}