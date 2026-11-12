package co.edu.ucc.smartlibrary.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import co.edu.ucc.smartlibrary.repository.LibroRepository;
import co.edu.ucc.smartlibrary.repository.PrestamoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Escenarios de verificación de la Actividad 6 del taller.
 * Datos iniciales (data.sql): estudiantes 1 y 2; libros 1 y 2 disponibles;
 * libro 3 con un préstamo activo. Cada prueba se revierte al terminar (@Transactional).
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PrestamoControllerTest {

    private static final String URL = "/api/prestamos";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private LibroRepository libroRepository;

    private static String cuerpo(Long estudianteId, Long libroId) {
        return "{\"estudianteId\": " + estudianteId + ", \"libroId\": " + libroId + "}";
    }

    @Test
    @DisplayName("Préstamo de libro disponible -> 201 Created y préstamo registrado")
    void registrarPrestamoLibroDisponible() throws Exception {
        long prestamosAntes = prestamoRepository.count();

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(cuerpo(1L, 1L)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.estudiante.id").value(1))
                .andExpect(jsonPath("$.libro.id").value(1))
                .andExpect(jsonPath("$.libro.disponible").value(false))
                .andExpect(jsonPath("$.fechaPrestamo").exists())
                .andExpect(jsonPath("$.activo").value(true));

        assertThat(prestamoRepository.count()).isEqualTo(prestamosAntes + 1);
        assertThat(libroRepository.findById(1L).orElseThrow().isDisponible()).isFalse();
    }

    @Test
    @DisplayName("Libro ya prestado -> 409 Conflict y sin nuevo préstamo")
    void registrarPrestamoLibroNoDisponible() throws Exception {
        long prestamosAntes = prestamoRepository.count();

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(cuerpo(1L, 3L)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("El libro con id 3 no está disponible"));

        assertThat(prestamoRepository.count()).isEqualTo(prestamosAntes);
    }

    @Test
    @DisplayName("Estudiante inexistente -> 404 Not Found")
    void registrarPrestamoEstudianteInexistente() throws Exception {
        long prestamosAntes = prestamoRepository.count();

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(cuerpo(99L, 1L)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("No existe el estudiante con id 99"));

        assertThat(prestamoRepository.count()).isEqualTo(prestamosAntes);
        assertThat(libroRepository.findById(1L).orElseThrow().isDisponible()).isTrue();
    }

    @Test
    @DisplayName("Libro inexistente -> 404 Not Found")
    void registrarPrestamoLibroInexistente() throws Exception {
        long prestamosAntes = prestamoRepository.count();

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(cuerpo(1L, 99L)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("No existe el libro con id 99"));

        assertThat(prestamoRepository.count()).isEqualTo(prestamosAntes);
    }

    @Test
    @DisplayName("Consulta de préstamos -> 200 OK con registros")
    void listarPrestamos() throws Exception {
        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].libro.id").value(3))
                .andExpect(jsonPath("$[0].activo").value(true));
    }

    @Test
    @DisplayName("Solicitud inválida (sin libroId) -> 400 Bad Request y sin préstamo (R4)")
    void registrarPrestamoSolicitudInvalida() throws Exception {
        long prestamosAntes = prestamoRepository.count();

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content("{\"estudianteId\": 1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("libroId es obligatorio"));

        assertThat(prestamoRepository.count()).isEqualTo(prestamosAntes);
    }
}
