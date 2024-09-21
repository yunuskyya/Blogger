package com.blogger.backend.controller;

import com.blogger.backend.constant.BloggerConstant;
import com.blogger.backend.dto.request.CredentialsRequest;
import com.blogger.backend.dto.response.AuthResponse;
import com.blogger.backend.service.AuthService;
import com.blogger.backend.shared.GenericMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping(BloggerConstant.API_V1 + "/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<AuthResponse> handleAuthentication(@Valid @RequestBody CredentialsRequest credentials) {
        AuthResponse authResponse = authService.authenticate(credentials);
        ResponseCookie cookie = ResponseCookie.from("blogger-token", authResponse.getToken().getTokenId())
                .path("/").sameSite("None").secure(true).httpOnly(true).build();
        authResponse.setToken(null);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(authResponse);
    }

    @DeleteMapping
    public ResponseEntity<GenericMessage> logout(
            @CookieValue(name = "blogger-token", required = false) String cookieValue) {
        if (cookieValue != null && !cookieValue.isEmpty()) {
            var tokenWithPrefix = "bloggerToken " + cookieValue;
            authService.logout(tokenWithPrefix);
        }
        ResponseCookie cookie = ResponseCookie.from("blogger-token", "")
                .path("/").httpOnly(true).maxAge(0).build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new GenericMessage("User logged out successfully!"));
    }
}
