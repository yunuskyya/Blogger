package com.blogger.backend.dto.response;

import lombok.Data;

@Data
public class GetUserByEmailResponse {
    private int id;
    private String username;
    private String password;
    private String email;
    private boolean active;
    private boolean isDeleted;
}

