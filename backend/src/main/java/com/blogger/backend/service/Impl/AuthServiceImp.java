package com.blogger.backend.service.Impl;

import com.blogger.backend.config.ModelMapperConfig;
import com.blogger.backend.dto.request.CredentialsRequest;
import com.blogger.backend.dto.response.AuthResponse;
import com.blogger.backend.dto.response.GetUserByEmailResponse;
import com.blogger.backend.exception.AuthenticationException;
import com.blogger.backend.model.Token;
import com.blogger.backend.model.User;
import com.blogger.backend.repository.UserRepository;
import com.blogger.backend.service.AuthService;
import com.blogger.backend.security.token.TokenService;
import com.blogger.backend.service.MailService;
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
    private final MailService mailService;
    private static final int MAX_ATTEMPTS = 2;



    @Autowired
    public AuthServiceImp(TokenService tokenService, PasswordEncoder passwordEncoder,
                          ModelMapperConfig modelMapperConfig, UserRepository userRepository, MailService mailService) {
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapperConfig = modelMapperConfig;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }


    private static final Logger logger = LogManager.getLogger(AuthServiceImp.class);
    @Override
    public AuthResponse authenticate(CredentialsRequest credentials) {
        User inDB = userRepository.findByEmail(credentials.email())
                .orElseGet(() -> userRepository.findByUsername(credentials.username())
                        .orElseGet(() -> userRepository.findByPhoneNumber(credentials.phoneNumber())
                                .orElseThrow(() -> {
                                    logger.error("User not found: {}",
                                            credentials.email() != null ? credentials.email() :
                                                    (credentials.username() != null ? credentials.username() : credentials.phoneNumber()));
                                    return new AuthenticationException("blogger.user.notfound.error.message");
                                })));

        if (!passwordEncoder.matches(credentials.password(), inDB.getPassword())) {
            logger.error("Invalid credentials for user: {}", credentials.email() != null ? credentials.email() :
                    (credentials.username() != null ? credentials.username() : credentials.phoneNumber()));
            inDB.setLoginAttemps(inDB.getLoginAttemps() + 1);
            logger.error("login attemps: {} ", inDB.getLoginAttemps());
            userRepository.save(inDB);

            if (inDB.getLoginAttemps() > MAX_ATTEMPTS) {
                inDB.setLocked(true);
                mailService.sendAccountLockedEmail(inDB);
                throw new AuthenticationException("blogger.authentication.locked.message");
            }
            throw new AuthenticationException("blogger.authentication.error.message");
        }

        if (!inDB.isActive()) {
            logger.error("User is not active: {}", inDB.getEmail());
            throw new AuthenticationException("blogger.authentication.inactive.message");
        }

        if (inDB.isDeleted()) {
            logger.error("User is deleted: {}", inDB.getEmail());
            throw new AuthenticationException("blogger.authentication.deleted.message");
        }

        if(inDB.isLocked()){
            logger.error("User is locked: {}", inDB.getEmail());
            throw new AuthenticationException("blogger.authentication.locked.message");
        }

        inDB.setLoginAttemps(0);
        userRepository.save(inDB);
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


