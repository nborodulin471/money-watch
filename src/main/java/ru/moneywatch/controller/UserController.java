package ru.moneywatch.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.moneywatch.model.dtos.UserDto;
import ru.moneywatch.service.auth.UserService;

@RestController
@RequestMapping("/api/auth/profile")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") long id) {
        return ResponseEntity.ok(
                userService.get(id)
        );
    }

    @PostMapping("/{id}")
    public ResponseEntity<UserDto> editUser(@PathVariable("id") long id, UserDto userDto) {
        return ResponseEntity.ok(
                userService.edit(id, userDto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
        userService.delete(id);

        return ResponseEntity.ok().build();
    }

}
