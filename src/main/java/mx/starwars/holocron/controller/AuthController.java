package mx.starwars.holocron.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import mx.starwars.holocron.dto.UserRequest;
import mx.starwars.holocron.dto.security.JwtResponse;
import mx.starwars.holocron.service.AuthService;
import mx.starwars.holocron.service.JwtService;
import mx.starwars.holocron.service.TokenBlacklistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controlador REST que gestiona la autenticación de usuarios dentro del sistema Star Wars.
 */
@Tag(name = "Autenticación", description = "Endpoints para login y logout de usuarios en Star Wars")
@RestController
@RequestMapping("/starwars/auth")
public class AuthController {

    private final AuthService authService;
    private final TokenBlacklistService blacklistService;

    public AuthController(AuthService authService, TokenBlacklistService blacklistService) {
        this.authService = authService;
        this.blacklistService = blacklistService;
    }

    /**
     * Endpoint que gestiona el proceso de inicio de sesión de un usuario.
     *
     * @param userRequest objeto que contiene las credenciales del usuario.
     * @return respuesta HTTP con el token JWT si es exitoso, o estado 401 si falla.
     */
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica al usuario con sus credenciales y retorna un token JWT si son válidas"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest userRequest) {
        try {
            String token = authService.login(userRequest.getUserName(), userRequest.getPassword());
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Maneja la solicitud de cierre de sesión (`logout`) del usuario.
     */
    @Operation(
            summary = "Cerrar sesión",
            description = "Revoca el token JWT actual y finaliza la sesión del usuario"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logout exitoso"),
            @ApiResponse(responseCode = "401", description = "Token inválido o no enviado")
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = extractToken(request);
        if (token != null) {
            blacklistService.blacklist(token);
        }
        return ResponseEntity.ok("Logout exitoso");
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return (authHeader != null && authHeader.startsWith("Bearer "))
                ? authHeader.substring(7)
                : null;
    }

}
