package com.qm.user.dto;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AuthRequest {
    @Email @NotEmpty private String email;
    @NotEmpty private String password;
}
