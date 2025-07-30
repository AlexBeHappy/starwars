package mx.starwars.holocron.config.security;

import mx.starwars.holocron.service.TokenBlacklistService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;

/**
 * Configuración principal de seguridad para la aplicación.
 * Define el filtro de autenticación JWT y la cadena de filtros que determina
 * qué rutas están protegidas y cómo se procesan las solicitudes HTTP.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Registra el filtro de autenticación JWT como un Bean.
     * @param secretKey clave secreta para validar la firma del token JWT,
     *                  inyectada desde el archivo de propiedades.
     * @return instancia de JwtAuthenticationFilter
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(@Value("${jwt.secret}") String secretKey,
                                                           TokenBlacklistService tokenBlacklistService) {
        return new JwtAuthenticationFilter(secretKey, tokenBlacklistService);
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> disableJwtFilterRegistration(JwtAuthenticationFilter filter) {
        FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.setEnabled(false);
        return registration;
    }

    /**
     * Configura la cadena de filtros de seguridad de Spring Security.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/starwars/auth/**", "/h2-console/**",  "/swagger-ui.html", "/swagger-ui/**",
                                "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
