package com.qm.user.dto;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotEmpty private String name;
    @Email @NotEmpty private String email;
    @NotEmpty @Size(min = 6) private String password;
}
