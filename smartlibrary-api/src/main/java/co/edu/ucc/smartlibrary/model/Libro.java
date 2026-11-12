package co.edu.ucc.smartlibrary.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Entidad del dominio: libro de la biblioteca.
 * Es el experto en información sobre su propia disponibilidad (GRASP Experto).
 */
@Entity
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private boolean disponible;

    protected Libro() {
        // requerido por JPA
    }

    public Libro(String titulo, boolean disponible) {
        this.titulo = titulo;
        this.disponible = disponible;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /** R1: solo un libro disponible puede prestarse. */
    public boolean isDisponible() {
        return disponible;
    }

    /** R3: al prestarse, el libro deja de estar disponible. */
    public void marcarPrestado() {
        this.disponible = false;
    }
}
