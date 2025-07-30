package org.game.numberguess.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Username is required")
    @Email(message = "Invalid email format")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "password must be at least 6 characters")
    private String password;
} 