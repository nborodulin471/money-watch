package ru.moneywatch.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import ru.moneywatch.model.AuthenticationRequest;
import ru.moneywatch.model.dtos.UserDto;
import ru.moneywatch.service.auth.AuthenticationService;

import java.util.HashMap;
import java.util.Map;

/**
 * Контроллер отвечающий за авторизацию и регистрацию.
 *
 * @author Бородулин Никита Петрович.
 */
@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthenticationRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("access_token", authenticationService.login(request.getUsername(), request.getPassword()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> showRegistrationForm(@RequestBody UserDto user) {
        authenticationService.registerNewUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Аккаунт успешно зарегистрирован.");
    }
}
