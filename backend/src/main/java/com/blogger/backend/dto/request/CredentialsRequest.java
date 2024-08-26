package com.blogger.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CredentialsRequest(@Email String email, @NotBlank String password, String username,String phoneNumber) {
}
