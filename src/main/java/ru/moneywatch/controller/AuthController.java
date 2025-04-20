package ru.moneywatch.controller;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import ru.moneywatch.model.dtos.UserDto;
import ru.moneywatch.model.mappers.UserMapper;
import ru.moneywatch.repository.UserRepository;

/**
 * Контроллер отвечающий за авторизацию и регистрацию.
 *
 * @author Бородулин Никита Петрович.
 */
@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());

        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserDto user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        var userEntity = userMapper.toEntity(user);
        userEntity.setEnabled(true);
        userRepository.save(userEntity);

        return "redirect:/api/auth/login";
    }
}
