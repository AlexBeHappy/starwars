package mx.starwars.holocron.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.starwars.holocron.service.TokenBlacklistService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Filtro personalizado para interceptar solicitudes HTTP y validar un token JWT.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String secretKey;
    private final TokenBlacklistService tokenBlacklistService;

    /**
     * Constructor que recibe la clave secreta.
     *
     * @param secretKey clave secreta para validar el JWT
     * @param tokenBlacklistService servicio responsable de consultar la lista negra de tokens JWT
     */
    public JwtAuthenticationFilter(String secretKey, TokenBlacklistService tokenBlacklistService) {
        this.secretKey = secretKey;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    /**
     * Método principal del filtro que intercepta la solicitud y valida el JWT.
     *
     * @param request     solicitud HTTP entrante
     * @param response    respuesta HTTP saliente
     * @param filterChain cadena de filtros
     * @throws ServletException en caso de error con el filtro
     * @throws IOException      en caso de error de E/S
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            if (tokenBlacklistService.isBlacklisted(jwt)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                String username = claims.getSubject();
                if (username != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
