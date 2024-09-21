package com.blogger.backend.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class GetUserByEmailResponse implements Serializable {
    private int id;
    private String username;
    private String password;
    private String email;
    private boolean active;
    private boolean isDeleted;
}

