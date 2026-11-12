package co.edu.ucc.smartlibrary.exception;

/** Se lanza cuando el estudiante o el libro solicitado no existe (404). */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
