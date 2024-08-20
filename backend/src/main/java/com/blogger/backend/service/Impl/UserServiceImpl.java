package com.blogger.backend.service.Impl;

import com.blogger.backend.config.ModelMapperConfig;
import com.blogger.backend.dto.request.RegisterUserRequest;
import com.blogger.backend.model.User;
import com.blogger.backend.model.enums.Role;
import com.blogger.backend.repository.UserRepository;
import com.blogger.backend.service.UserService;
import com.blogger.backend.token.TokenService;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapperForResponse;
    private final ModelMapper modelMapperForRequest;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository, @Qualifier("modelMapperForResponse") ModelMapper modelMapperForResponse,
                           @Qualifier("modelMapperForRequest") ModelMapper modelMapperForRequest, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapperForResponse = modelMapperForResponse;
        this.modelMapperForRequest = modelMapperForRequest;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public void registerUser(RegisterUserRequest registerUserRequest) {
        User newUser = modelMapperForRequest.map(registerUserRequest, User.class);
        newUser.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        newUser.setAuthorities(new HashSet<>() {{
            add(Role.ROLE_USER);
        }});
        newUser.setActive(true);
        userRepository.save(newUser);
        logger.info("User registered: {}", newUser.getUsername());
    }

}
