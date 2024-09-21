package com.blogger.backend.service;

import com.blogger.backend.dto.request.CredentialsRequest;
import com.blogger.backend.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse authenticate(CredentialsRequest credentials);

    void logout(String authorizationHeader);

    int getCurrentUserId();

}


