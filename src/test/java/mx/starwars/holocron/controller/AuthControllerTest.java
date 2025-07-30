package mx.starwars.holocron.controller;

import jakarta.servlet.http.HttpServletRequest;
import mx.starwars.holocron.dto.UserRequest;
import mx.starwars.holocron.dto.security.JwtResponse;
import mx.starwars.holocron.service.AuthService;
import mx.starwars.holocron.service.TokenBlacklistService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private TokenBlacklistService blacklistService;

    @InjectMocks
    private AuthController authController;

    @Test
    void loginOk() {
        String token = "jwt-token";
        UserRequest request = new UserRequest();
        request.setUserName("luke");
        request.setPassword("skywalker");
        when(authService.login("luke", "skywalker")).thenReturn(token);
        ResponseEntity<?> response = authController.login(request);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof JwtResponse);
        assertEquals(token, ((JwtResponse) response.getBody()).getToken());
    }

    @Test
    void loginInvalid() {
        UserRequest request = new UserRequest();
        request.setUserName("han");
        request.setPassword("incorrecto");
        when(authService.login(anyString(), anyString()))
                .thenThrow(new BadCredentialsException("Credenciales inválidas"));
        ResponseEntity<?> response = authController.login(request);
        assertEquals(401, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Map);
        assertEquals("Credenciales inválidas", ((Map<?, ?>) response.getBody()).get("error"));
    }

    @Test
    void logout() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer mock-token");
        ResponseEntity<?> response = authController.logout(mockRequest);
        verify(blacklistService).blacklist("mock-token");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Logout exitoso", response.getBody());
    }

}