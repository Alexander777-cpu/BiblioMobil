package pe.edu.upeu.bibliomobil.domain.model

data class DetallePrestamo(
    val libro: Libro,
    val dias: Int
) {
    init {
        require(dias in 1..DIAS_MAXIMOS) { "El préstamo debe durar entre 1 y 15 días" }
    }

    fun multaPorRetraso(diasRetraso: Int): Double = diasRetraso * MULTA_DIARIA

    companion object {
        const val DIAS_MAXIMOS = 15
        const val MULTA_DIARIA = 1.50
    }
}

sealed class EstadoPrestamo {
    data object Solicitado : EstadoPrestamo()
    data object Entregado : EstadoPrestamo()
    data object Devuelto : EstadoPrestamo()
    data class Vencido(val diasRetraso: Int) : EstadoPrestamo()
}

data class Prestamo(
    val id: Long,
    val lector: Lector,
    val detalles: List<DetallePrestamo>,
    val estado: EstadoPrestamo
)