package co.edu.ucc.smartlibrary.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Objeto de entrada de POST /api/prestamos.
 * Solo transporta identificadores; no expone las entidades al cliente.
 */
public record PrestamoRequest(
        @NotNull(message = "estudianteId es obligatorio") Long estudianteId,
        @NotNull(message = "libroId es obligatorio") Long libroId) {
}
