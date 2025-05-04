package ru.moneywatch.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.moneywatch.model.AuthenticationRequest;
import ru.moneywatch.model.dtos.UserDto;
import ru.moneywatch.repository.UserRepository;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    void shouldSuccessfullyRegisterNewUser() throws Exception {
        var request = new UserDto();
        request.setUsername("username");
        request.setPassword("password");
        request.setRole("ROLE_USER");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andDo(print())
                .andExpect(status().isCreated());

        Assertions.assertFalse(userRepository.findByUsername("username").isEmpty());
    }

    @Test
    void shouldFailToRegisterSecondAdmin() throws Exception {
        // Сначала регистрируем первого администратора
        var firstAdmin = new UserDto();
        firstAdmin.setUsername("first_admin");
        firstAdmin.setPassword("password");
        firstAdmin.setRole("ROLE_ADMIN");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(firstAdmin)))
                .andDo(print())
                .andExpect(status().isCreated());
        Assertions.assertFalse(userRepository.findByUsername("first_admin").isEmpty());

        // Затем пытаемся зарегистрировать второго администратора
        var secondAdmin = new UserDto();
        secondAdmin.setUsername("first_admin");
        secondAdmin.setPassword("password");
        secondAdmin.setRole("ROLE_ADMIN");
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(secondAdmin)))
                .andDo(print())
                .andExpect(content().string(containsString("Пользователь с таким именем уже существует")));
        Assertions.assertTrue(userRepository.findByUsername("second_admin").isEmpty());
    }

    @Test
    void shouldAuthenticateRegisteredUser() throws Exception {
        // Регистрируем пользователя перед попыткой войти
        var req = new UserDto();
        req.setUsername("test_user");
        req.setPassword("password");
        req.setRole("ROLE_USER");
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(req)))
                .andDo(print())
                .andExpect(status().isCreated());
        Assertions.assertFalse(userRepository.findByUsername("test_user").isEmpty());

        // Пытаемся войти
        AuthenticationRequest authReq = new AuthenticationRequest("test_user", "password");
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(authReq)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("token")));
    }

    @Test
    void shouldFailAuthenticationForInvalidCredentials() throws Exception {
        AuthenticationRequest invalidAuthReq = new AuthenticationRequest("unknown_user", "bad_password");
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidAuthReq)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    private String asJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
