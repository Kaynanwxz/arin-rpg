package arin_rpg.controller;

import arin_rpg.model.User;
import arin_rpg.model.UserRequest;
import arin_rpg.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @GetMapping()
    public List<User> GetUsers() {
        return userService.getUser();
    }

    @PostMapping()
    public User CreateUser(@RequestBody User user) {
        return userService.CreateUser(user);
    }

    @GetMapping("/login")
    public String Login(@RequestBody UserRequest userRequest) {
        return userService.Login(userRequest);
    }

    @GetMapping("/me")
    public User GetMe(@RequestHeader("Authorization") String authorization){
        return userService.GetMe(authorization);
    }
}
