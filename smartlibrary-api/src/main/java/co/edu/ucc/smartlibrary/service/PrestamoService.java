package co.edu.ucc.smartlibrary.service;

import co.edu.ucc.smartlibrary.dto.PrestamoRequest;
import co.edu.ucc.smartlibrary.exception.LibroNoDisponibleException;
import co.edu.ucc.smartlibrary.exception.RecursoNoEncontradoException;
import co.edu.ucc.smartlibrary.model.Estudiante;
import co.edu.ucc.smartlibrary.model.Libro;
import co.edu.ucc.smartlibrary.model.Prestamo;
import co.edu.ucc.smartlibrary.repository.EstudianteRepository;
import co.edu.ucc.smartlibrary.repository.LibroRepository;
import co.edu.ucc.smartlibrary.repository.PrestamoRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Coordina el caso de uso "Registrar préstamo" (GRASP Controlador de caso de uso)
 * y aplica las reglas de negocio R1-R5.
 */
@Service
public class PrestamoService {

    private final EstudianteRepository estudianteRepository;
    private final LibroRepository libroRepository;
    private final PrestamoRepository prestamoRepository;

    public PrestamoService(EstudianteRepository estudianteRepository,
                           LibroRepository libroRepository,
                           PrestamoRepository prestamoRepository) {
        this.estudianteRepository = estudianteRepository;
        this.libroRepository = libroRepository;
        this.prestamoRepository = prestamoRepository;
    }

    /**
     * Registra un préstamo. Es transaccional: el cambio de disponibilidad del libro
     * y la creación del préstamo se confirman juntos o no se confirma ninguno (R3, R4).
     */
    @Transactional
    public Prestamo registrarPrestamo(PrestamoRequest request) {
        Estudiante estudiante = estudianteRepository.findById(request.estudianteId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el estudiante con id " + request.estudianteId()));

        Libro libro = libroRepository.findById(request.libroId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el libro con id " + request.libroId()));

        // R1 y R5: el libro debe estar disponible y sin un préstamo activo.
        if (!libro.isDisponible() || prestamoRepository.existsByLibroIdAndFechaDevolucionIsNull(libro.getId())) {
            throw new LibroNoDisponibleException(
                    "El libro con id " + libro.getId() + " no está disponible");
        }

        // R2: el préstamo registra estudiante, libro y fecha.
        Prestamo prestamo = new Prestamo(estudiante, libro, LocalDate.now());
        // R3: el libro deja de estar disponible.
        libro.marcarPrestado();

        libroRepository.save(libro);
        return prestamoRepository.save(prestamo);
    }

    @Transactional(readOnly = true)
    public List<Prestamo> listarPrestamos() {
        return prestamoRepository.findAll();
    }
}
