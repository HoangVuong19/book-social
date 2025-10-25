package com.example.identity.dto.response;

import java.util.Set;

public record RoleResponse(String name,
                           String description,
                           Set<PermissionResponse> permissions) {
}
