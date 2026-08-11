package arin_rpg.component;

import arin_rpg.model.User;
import arin_rpg.repository.UserRepository;
import arin_rpg.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TokenComponent {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public User getUserFromToken(String authentication) {
        if (authentication == null || authentication.isBlank()) throw new RuntimeException("Invalid token.");

        if (!authentication.startsWith("Bearer ")) throw new RuntimeException("Invalid token.");

        String token = authentication.replace("Bearer ", "");

        String email = jwtService.getUserFromToken(token);

        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with cpf from token."));
    }
}
