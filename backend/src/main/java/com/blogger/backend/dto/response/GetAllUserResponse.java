package com.blogger.backend.dto.response;

import lombok.Data;

@Data
public class GetAllUserResponse {

    private int id;
    private String username;
    private String email;
    private String phoneNumber;
    private boolean active;
    private boolean isDeleted;
}

