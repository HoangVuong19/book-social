package com.example.identity.controller;

import com.example.identity.dto.request.RegisterRequest;
import com.example.identity.dto.response.PermissionResponse;
import com.example.identity.dto.response.RoleResponse;
import com.example.identity.dto.response.UserResponse;
import com.example.identity.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationService authenticationService;

    private RegisterRequest request;
    private UserResponse userResponse;

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

        PermissionResponse permission = new PermissionResponse(
                "USER_READ",
                "Can read user info"
        );

        RoleResponse role = new RoleResponse(
                "ADMIN",
                "Administrator role",
                Set.of(permission)
        );

        userResponse = new UserResponse(
                "cf0600f538b3",
                "john",
                "John",
                "Doe",
                dob,
                Set.of(role)
        );
    }

    @Test
    void registerUser_controller_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        Mockito.when(authenticationService.register(ArgumentMatchers.any()))
                .thenReturn(userResponse);

        String expectedJson = """
                {
                  "success": true,
                  "data": {
                    "id": "cf0600f538b3",
                    "username": "john",
                    "firstName": "John",
                    "lastName": "Doe",
                    "dob": "1990-01-01",
                    "roles": [
                      {
                        "name": "ADMIN",
                        "description": "Administrator role",
                        "permissions": [
                          {
                            "name": "USER_READ",
                            "description": "Can read user info"
                          }
                        ]
                      }
                    ]
                  },
                  "errors": null
                }
                """;

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));
    }

    @Test
    void registerUser_controller_fail() throws Exception {
        // GIVEN
        request = new RegisterRequest(
                "jo",
                "John12345",
                "Doe",
                "12345678",
                request.dob()
        );

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        String expectedJson = """
                {
                    "success": false,
                    "data": null,
                    "errors": {
                        "code": 400,
                        "message": "Username must be at least 3 characters"
                    }
                }
                """;


        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));
        ;
    }
}
