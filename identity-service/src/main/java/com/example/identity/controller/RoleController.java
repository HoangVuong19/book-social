package com.example.identity.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.identity.config.serialize.ApiResponse;
import com.example.identity.dto.request.RoleRequest;
import com.example.identity.dto.response.RoleResponse;
import com.example.identity.service.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleService roleService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        return ApiResponse.success(roleService.create(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.success(roleService.getAll());
    }

    @DeleteMapping("/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<String> delete(@PathVariable String role) {
        roleService.delete(role);
        return ApiResponse.success("OK");
    }
}
