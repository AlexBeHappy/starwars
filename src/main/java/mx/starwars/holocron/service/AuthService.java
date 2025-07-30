package mx.starwars.holocron.service;

import mx.starwars.holocron.constants.AppConstants;
import mx.starwars.holocron.entity.AppUser;
import mx.starwars.holocron.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthService(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public String login(String userName, String password) {
        AppUser appUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new BadCredentialsException(AppConstants.MSJ_ER_USER_NOT_FOUND));
        if (!appUser.getPassword().equals(password)) {
            throw new BadCredentialsException(AppConstants.MSJ_ER_INVALID_PASSWORD);
        }
        return jwtService.generateToken(userName);
    }

}
