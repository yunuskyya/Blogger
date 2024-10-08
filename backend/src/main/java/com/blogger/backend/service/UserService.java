package com.blogger.backend.service;

import com.blogger.backend.dto.request.RegisterUserRequest;
import com.blogger.backend.dto.request.UserUnLockedRequset;
import com.blogger.backend.dto.response.GetAllUserResponse;
import com.blogger.backend.dto.response.GetUserByIdResponse;
import com.blogger.backend.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
     void registerUser(RegisterUserRequest request);
     void activationUser(String token);
     Page<GetAllUserResponse> getAllUsers(Pageable pageable);
     GetUserByIdResponse getUserById(int id);
     void unLockedUser(UserUnLockedRequset requset);
     void assignRole(int userId, Role newRole, int currentUserId);


}
