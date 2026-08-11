package arin_rpg.service;

import arin_rpg.component.TokenComponent;
import arin_rpg.component.ValidatorComponent;
import arin_rpg.configuration.SecurityConfig;
import arin_rpg.model.User;
import arin_rpg.model.UserRequest;
import arin_rpg.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final SecurityConfig securityConfig;
    private final TokenComponent tokenComponent;
    private final ValidatorComponent validatorComponent;

    public List<User> getUser() {
        return userRepository.findAll();
    }


    public User CreateUser(User user) {

        validatorComponent.UserIsValid(user);

        user.setPassword(securityConfig.passwordEncoder().encode(user.getPassword()));

        return userRepository.save(user);
    }

    public String Login(UserRequest userRequest) {

        User user = userRepository.findByEmail(userRequest.getEmail()).orElseThrow(() -> new RuntimeException("Invalid Email or Password"));

        securityConfig.passwordEncoder().matches(userRequest.getPassword(), user.getPassword());

        return jwtService.generateToken(user.getEmail());
    }

    public User GetMe(String authorization) {
        return tokenComponent.getUserFromToken(authorization);
    }
}
