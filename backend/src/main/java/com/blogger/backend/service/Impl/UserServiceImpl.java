package com.blogger.backend.service.Impl;

import com.blogger.backend.dto.request.RegisterUserRequest;
import com.blogger.backend.dto.request.UserUnLockedRequset;
import com.blogger.backend.dto.response.GetAllUserResponse;
import com.blogger.backend.dto.response.GetUserByIdResponse;
import com.blogger.backend.exception.AccessDeniedException;
import com.blogger.backend.exception.GeneralErrorException;
import com.blogger.backend.exception.InvalidTokenException;
import com.blogger.backend.exception.UserNotFoundException;
import com.blogger.backend.model.User;
import com.blogger.backend.model.enums.Role;
import com.blogger.backend.repository.UserRepository;
import com.blogger.backend.service.MailService;
import com.blogger.backend.service.UserService;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final MailService mailService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapperForResponse;
    private final ModelMapper modelMapperForRequest;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    public UserServiceImpl(MailService mailService, UserRepository userRepository, @Qualifier("modelMapperForResponse") ModelMapper modelMapperForResponse,
                           @Qualifier("modelMapperForRequest") ModelMapper modelMapperForRequest, PasswordEncoder passwordEncoder) {
        this.mailService = mailService;
        this.userRepository = userRepository;
        this.modelMapperForResponse = modelMapperForResponse;
        this.modelMapperForRequest = modelMapperForRequest;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(rollbackOn = MailException.class)
    public void registerUser(RegisterUserRequest registerUserRequest) {
            User newUser = modelMapperForRequest.map(registerUserRequest, User.class);
            newUser.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
            newUser.setActivationToken(UUID.randomUUID().toString());
            newUser.setAuthorities(new HashSet<>() {{
                add(Role.ROLE_USER);
            }});
            newUser.setActive(false);
            userRepository.saveAndFlush(newUser);
            mailService.sendAccountActivationEmail(newUser.getEmail(),newUser.getActivationToken());
            logger.info("User registered: {}", newUser.getUsername());
    }

    @Override
    public void activationUser(String token) {
        User inDB = userRepository.findByActivationToken(token);
        if (inDB == null) {
            throw new InvalidTokenException();
        }
        inDB.setActive(true);
        inDB.setActivationToken(null);
        userRepository.save(inDB);
        logger.info("User activated: {}", inDB.getUsername());

    }

    @Override
    public Page<GetAllUserResponse> getAllUsers(Pageable pageable) {
    Page <User> users = userRepository.findAll(pageable);
    return users.map(user -> modelMapperForResponse.map(user, GetAllUserResponse.class));}

    @Override
    public GetUserByIdResponse getUserById(int id) {
        User inDb = userRepository.findById(id).orElseThrow(() -> {
            logger.error("User not found with id: {}", id);
            throw new UserNotFoundException(id);
        });
        return modelMapperForResponse.map(inDb, GetUserByIdResponse.class);
    }

    @Override
    public void unLockedUser(UserUnLockedRequset requset) {
        User userInDb = userRepository.findByEmail(requset.getEmail()).orElseThrow(() -> {
            logger.error("User not found with email: {}", requset.getEmail());
            return new UserNotFoundException("User not found with email: " + requset.getEmail());
        });
        if(!userInDb.isLocked()){
            throw new GeneralErrorException("user.already.unLocked.error.message");
        }
        userInDb.setLocked(false);
        userRepository.save(userInDb);
    }
    @Override
    public void assignRole(int userId, Role newRole, int currentUserId) {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException("Current user not found"));

        if (!currentUser.getAuthorities().contains(Role.ROLE_ADMIN)) {
            throw new AccessDeniedException();
        }

        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Set<Role> updatedRoles = new HashSet<>();
        updatedRoles.add(newRole);
        userToUpdate.setAuthorities(updatedRoles);
        userRepository.save(userToUpdate);
    }

}
