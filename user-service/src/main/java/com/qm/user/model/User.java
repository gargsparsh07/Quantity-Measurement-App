package com.qm.user.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String name;
    @Column(nullable = false, unique = true) private String email;
    private String password;
    @Enumerated(EnumType.STRING) private Role role;
    @Enumerated(EnumType.STRING) private AuthProvider provider;
    private String providerId;
    private LocalDateTime createdAt;

    @PrePersist protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (role == null) role = Role.ROLE_USER;
        if (provider == null) provider = AuthProvider.LOCAL;
    }
}
