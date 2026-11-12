package co.edu.ucc.smartlibrary.exception;

/** Se lanza cuando se intenta prestar un libro que no está disponible (409). */
public class LibroNoDisponibleException extends RuntimeException {

    public LibroNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
