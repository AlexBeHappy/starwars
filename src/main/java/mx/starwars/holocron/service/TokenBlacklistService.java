package mx.starwars.holocron.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


/**
 * Servicio responsable de mantener una lista negra de tokens JWT.
 */
@Service
public class TokenBlacklistService {

    private final Set<String> blacklistedTokens = new HashSet<>();

    public void blacklist(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
