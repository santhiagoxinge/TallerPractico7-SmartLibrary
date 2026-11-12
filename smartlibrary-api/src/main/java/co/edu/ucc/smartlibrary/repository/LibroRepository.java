package co.edu.ucc.smartlibrary.repository;

import co.edu.ucc.smartlibrary.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

/** Acceso a datos de Libro. Spring Data genera la implementación. */
public interface LibroRepository extends JpaRepository<Libro, Long> {
}
