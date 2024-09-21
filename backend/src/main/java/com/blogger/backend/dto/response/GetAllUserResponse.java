package com.blogger.backend.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class GetAllUserResponse implements Serializable {

    private int id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String image;

}
