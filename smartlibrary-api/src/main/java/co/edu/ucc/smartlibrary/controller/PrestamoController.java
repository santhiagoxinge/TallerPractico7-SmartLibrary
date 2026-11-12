package co.edu.ucc.smartlibrary.controller;

import co.edu.ucc.smartlibrary.dto.PrestamoRequest;
import co.edu.ucc.smartlibrary.model.Prestamo;
import co.edu.ucc.smartlibrary.service.PrestamoService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Capa de presentación: recibe las peticiones HTTP, valida el formato
 * y delega el caso de uso en PrestamoService. No contiene reglas de negocio.
 */
@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping
    public ResponseEntity<Prestamo> registrarPrestamo(@Valid @RequestBody PrestamoRequest request) {
        Prestamo prestamo = prestamoService.registrarPrestamo(request);
        return ResponseEntity.created(URI.create("/api/prestamos/" + prestamo.getId())).body(prestamo);
    }

    @GetMapping
    public ResponseEntity<List<Prestamo>> listarPrestamos() {
        return ResponseEntity.ok(prestamoService.listarPrestamos());
    }
}
