package com.blogger.backend.dto.response;

import lombok.Data;

@Data
public class GetAllUserResponse {

    private int id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String image;

    public String getFullName() {
        return (firstName != null ? firstName : "") +
                " " +
                (lastName != null ? lastName : "");
    }

}
