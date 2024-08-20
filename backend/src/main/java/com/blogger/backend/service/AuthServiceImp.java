package com.blogger.backend.service;

import com.blogger.backend.config.ModelMapperConfig;
import com.blogger.backend.dto.request.CredentialsRequest;
import com.blogger.backend.dto.response.AuthResponse;
import com.blogger.backend.dto.response.GetUserByEmailResponse;
import com.blogger.backend.exception.AuthenticationException;
import com.blogger.backend.model.Token;
import com.blogger.backend.model.User;
import com.blogger.backend.repository.UserRepository;
import com.blogger.backend.token.TokenService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImp implements AuthService {

    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapperConfig modelMapperConfig;
    private final UserRepository userRepository;


    @Autowired
    public AuthServiceImp(TokenService tokenService, PasswordEncoder passwordEncoder,
                          ModelMapperConfig modelMapperConfig, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapperConfig = modelMapperConfig;
        this.userRepository = userRepository;

    }

    private static final Logger logger = LogManager.getLogger(AuthServiceImp.class);


    @Override
    public AuthResponse authenticate(CredentialsRequest credentials) {
        User inDB = userRepository.findByEmail(credentials.email())
                .orElseGet(() -> userRepository.findByUsername(credentials.username())
                        .orElseThrow(() -> {
                            logger.error("User not found: {}",
                                    credentials.email() != null ? credentials.email() : credentials.username());
                            return new AuthenticationException();
                        }));

        // Kullanıcı doğrulamasını yapın
        if (!passwordEncoder.matches(credentials.password(), inDB.getPassword())) {
            logger.error("Invalid credentials for user: {}", credentials.email() != null ? credentials.email() : credentials.username());
            throw new AuthenticationException();
        } else if (!inDB.isActive() || inDB.isDeleted()) {
            logger.error("User is not active or deleted: {}", credentials.email() != null ? credentials.email() : credentials.username());
            throw new AuthenticationException();
        }

        logger.info("User authenticated: {}", inDB.getEmail());
        GetUserByEmailResponse userResp = modelMapperConfig.modelMapperForResponse().map(inDB, GetUserByEmailResponse.class);
        Token token = tokenService.generateToken(userResp, credentials);
        return new AuthResponse(userResp, token);
    }

    @Override
    public void logout(String authorizationHeader) {
        tokenService.logout(authorizationHeader);
    }
}

