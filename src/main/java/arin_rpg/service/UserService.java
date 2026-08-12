package arin_rpg.service;

import arin_rpg.component.TokenComponent;
import arin_rpg.component.ValidatorComponent;
import arin_rpg.configuration.Security;
import arin_rpg.model.User;
import arin_rpg.model.UserRequest;
import arin_rpg.repository.UserRepository;
import arin_rpg.utils.PasswordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final Security security;
    private final TokenComponent tokenComponent;
    private final ValidatorComponent validatorComponent;

    public List<User> getUser() {
        return userRepository.findAll();
    }

    public User CreateUser(User user) {

        validatorComponent.UserIsValid(user);

        user.setPassword(security.passwordEncoder().encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User UpdatePassword(User userUpdate, String authorization) {

        if (!PasswordValidator.isValid(userUpdate.getPassword())) {
            throw new RuntimeException("Password is not valid");
        }

        User user = tokenComponent.getUserFromToken(authorization);

        user.setPassword(security.passwordEncoder().encode(userUpdate.getPassword()));

        return userRepository.save(user);
    }

    public String Login(UserRequest userRequest) {

        User user = userRepository.findByEmail(userRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid Email or Password"));

        if (!security.passwordEncoder().matches(userRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Login");
        }

        return jwtService.generateToken(user.getEmail());
    }

    public User GetMe(String authorization) {
        return tokenComponent.getUserFromToken(authorization);
    }
}
