package mx.starwars.holocron.exception;

import org.springframework.http.HttpStatus;

/**
 * Excepción personalizada para API que permite definir el tipo de error y el código HTTP correspondiente.
 */
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    /**
     * Constructor simple con mensaje y estado HTTP.
     *
     * @param message mensaje descriptivo del error.
     * @param status  código de estado HTTP que representa el error.
     */
    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    /**
     * Obtiene el estado HTTP asociado a la excepción.
     *
     * @return HttpStatus personalizado.
     */
    public HttpStatus getStatus() {
        return status;
    }

}
