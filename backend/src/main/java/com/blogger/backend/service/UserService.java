package com.blogger.backend.service;

import com.blogger.backend.dto.request.RegisterUserRequest;

public interface UserService {
     void registerUser(RegisterUserRequest request);
     void activationUser(String token);


}
