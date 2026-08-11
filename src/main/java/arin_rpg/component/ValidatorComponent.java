package arin_rpg.component;

import arin_rpg.model.User;
import arin_rpg.repository.UserRepository;
import arin_rpg.utils.BirthValidator;
import arin_rpg.utils.CpfValidator;
import arin_rpg.utils.EmailValidator;
import arin_rpg.utils.PasswordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ValidatorComponent {

    private final UserRepository userRepository;

    public void UserIsValid(User user) {

        if (userRepository.existsByEmail(user.getEmail())) throw new RuntimeException("This email already exist.");

        if (!BirthValidator.isValid(user.getDateOfBirth())) throw new RuntimeException("You need to be over 18 years old.");

        if (!PasswordValidator.isValid(user.getPassword())) throw new RuntimeException("Invalid password");

        if (!EmailValidator.domainExists(user.getEmail())) throw new RuntimeException("Invalid email domain.");

    }
}
