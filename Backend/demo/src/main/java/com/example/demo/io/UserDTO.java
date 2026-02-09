package com.example.demo.io;


import com.example.demo.entities.Provider;
import com.example.demo.entities.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private UUID id;
    private String email;
    private String name;
    private String password;
    private String imageUrl;
    private boolean isEnabled;
    private Instant createdAt;
    private Instant updatedAt;
    private Provider provider;
    private Set<Role> roles;
}
