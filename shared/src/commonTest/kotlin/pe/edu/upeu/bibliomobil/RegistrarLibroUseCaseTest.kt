package pe.edu.upeu.bibliomobil

import kotlinx.coroutines.test.runTest
import pe.edu.upeu.bibliomobil.domain.usecase.LibroInvalidoException
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLibroUseCase
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RegistrarLibroUseCaseTest {

    private lateinit var repo: FakeLibroRepository
    private lateinit var useCase: RegistrarLibroUseCase

    @BeforeTest
    fun setUp() {
        repo = FakeLibroRepository()
        useCase = RegistrarLibroUseCase(repo)
    }

    @Test
    fun registroExitosoConDatosValidos() = runTest {
        val resultado = useCase(" El Quijote ", " Miguel de Cervantes ", "1605", "5")
        assertTrue(resultado.isSuccess)
        val libro = resultado.getOrNull()!!
        assertEquals("El Quijote", libro.titulo)
        assertEquals("Miguel de Cervantes", libro.autor)
        assertEquals(1605, libro.anio)
        assertEquals(5, libro.ejemplares)
    }

    @Test
    fun registroFallaPorTituloVacio() = runTest {
        val resultado = useCase("", "Autor", "2020", "5")
        assertTrue(resultado.isFailure)
        val excepcion = resultado.exceptionOrNull() as LibroInvalidoException
        assertEquals("El título es obligatorio", excepcion.errores.titulo)
    }

    @Test
    fun registroFallaPorAnioNoNumerico() = runTest {
        val resultado = useCase("Libro", "Autor", "dos mil", "5")
        assertTrue(resultado.isFailure)
        val excepcion = resultado.exceptionOrNull() as LibroInvalidoException
        assertEquals("El año debe ser un número entero", excepcion.errores.anio)
    }

    @Test
    fun registroFallaPorEjemplaresNegativos() = runTest {
        val resultado = useCase("Libro", "Autor", "2020", "-2")
        assertTrue(resultado.isFailure)
        val excepcion = resultado.exceptionOrNull() as LibroInvalidoException
        assertEquals("Los ejemplares no pueden ser negativos", excepcion.errores.ejemplares)
    }

    @Test
    fun registroFallaCuandoRepositorioLanzaExcepcion() = runTest {
        repo.debeFallar = true
        val resultado = useCase("Libro", "Autor", "2020", "5")
        assertTrue(resultado.isFailure)
    }
}