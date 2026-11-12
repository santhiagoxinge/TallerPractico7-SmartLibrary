package co.edu.ucc.smartlibrary.repository;

import co.edu.ucc.smartlibrary.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

/** Acceso a datos de Estudiante. Spring Data genera la implementación. */
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
}
