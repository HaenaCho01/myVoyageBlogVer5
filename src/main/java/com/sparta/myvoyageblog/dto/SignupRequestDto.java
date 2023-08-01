package com.sparta.myvoyageblog.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SignupRequestDto {
    @NotBlank
    @Size(min = 3, max = 10)
    @Pattern(regexp = "^[A-Za-z0-9]*$")
    private String username;

    @NotBlank
    @Size(min = 4, max = 15)
    @Pattern(regexp = "^[A-Za-z0-9~`!@#$%\\\\^&*()-]*$")
    private String password;

    @NotBlank
    private String passwordCheck;

    private boolean admin = false;
    private String adminToken = "";
}