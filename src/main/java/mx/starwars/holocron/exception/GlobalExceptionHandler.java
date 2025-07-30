package mx.starwars.holocron.exception;

import mx.starwars.holocron.dto.ApiRs;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

/**
 * Manejador global de excepciones para toda la aplicación.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura excepciones del tipo {@link ApiException} y devuelve una respuesta con el código adecuado.
     *
     * @param ex excepción personalizada con código HTTP.
     * @return respuesta HTTP con mensaje de error.
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, String>> handleApiException(ApiException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(Map.of("error", ex.getMessage()));
    }

    /**
     * Fallback para excepciones no controladas.
     *
     * @param ex cualquier excepción no capturada.
     * @return respuesta HTTP con código 500 y mensaje genérico.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor."));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiRs<String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String error = "Parámetro inválido";
        return ResponseEntity.badRequest().body(new ApiRs<>(error, null));
    }
}
