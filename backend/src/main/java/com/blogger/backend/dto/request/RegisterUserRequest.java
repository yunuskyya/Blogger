package com.blogger.backend.dto.request;

import com.blogger.backend.validation.annotations.UniqueEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest{
    @NotBlank(message = "{blogger.validation.notBlank.firstName}")
    @Size(min = 3, max = 150, message ="{blogger.validation.size.firstName}")
    private String firstName;
    @NotBlank(message = "{blogger.validation.notBlank.lastName}")
    @Size(min = 3, max = 150, message ="{blogger.validation.size.lastName}")
    private String lastName;
    @NotBlank(message = "{blogger.validation.notBlank.phoneNumber}")
    @Size(min = 11, max = 13, message ="{blogger.validation.size.phoneNumber}")
    private String phoneNumber;
    @NotBlank(message = "{blogger.validation.notBlank.username}")
    @Size(min = 3, max = 150, message ="{blogger.validation.size.username}")
    private String username;

    @NotBlank(message = "{blogger.validation.notBlank.email}")
    @Size(min = 3, max = 150, message = "{blogger.validation.size.email}")
    @UniqueEmail
    private String email;

    @Size(min = 8, max = 50, message = "{blogger.validation.size.password}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\.]).{8,}$", message = "{blogger.validation.pattern.password}")
    private String password;

    private boolean active= false;


}


