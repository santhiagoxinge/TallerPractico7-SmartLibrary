package co.edu.ucc.smartlibrary.repository;

import co.edu.ucc.smartlibrary.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

/** Acceso a datos de Prestamo. Spring Data genera la implementación. */
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    /** R5: ¿existe un préstamo activo (sin fecha de devolución) para el libro? */
    boolean existsByLibroIdAndFechaDevolucionIsNull(Long libroId);
}
