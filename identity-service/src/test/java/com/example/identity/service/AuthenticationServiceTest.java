package com.example.identity.service;

import com.example.identity.dto.request.RegisterRequest;
import com.example.identity.entity.Role;
import com.example.identity.entity.User;
import com.example.identity.exception.NotFoundException;
import com.example.identity.repository.RoleRepository;
import com.example.identity.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource("/test.properties")
public class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private RoleRepository roleRepository;

    private RegisterRequest request;
    private User user;

    @BeforeEach
    void initData() {
        LocalDate dob = LocalDate.of(1990, 1, 1);

        request = new RegisterRequest(
                "john",
                "John12345",
                "Doe",
                "12345678",
                dob
        );

        user = User.builder()
                .id("cf0600f538b3")
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .dob(dob)
                .build();

        Role role = Role.builder()
                .name("USER")
                .permissions(new HashSet<>())
                .build();
    }

    @Test
    void registerUser_service_success() {
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        // WHEN
        var response = authenticationService.register(request);
        // THEN
        Assertions.assertThat(response.id()).isEqualTo("cf0600f538b3");
        Assertions.assertThat(response.username()).isEqualTo("john");
    }

    @Test
    void registerUser_service_fail() {
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // WHEN
        var exception = assertThrows(NotFoundException.class,
                () -> authenticationService.register(request));

        // THEN
        Assertions.assertThat(exception.getCode())
                .isEqualTo(404);
    }
}
