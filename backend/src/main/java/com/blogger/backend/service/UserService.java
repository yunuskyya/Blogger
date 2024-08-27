package com.blogger.backend.service;

import com.blogger.backend.dto.request.RegisterUserRequest;
import com.blogger.backend.dto.response.GetAllUserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
     void registerUser(RegisterUserRequest request);
     void activationUser(String token);
     Page<GetAllUserResponse> getAllUsers(Pageable pageable);


}
