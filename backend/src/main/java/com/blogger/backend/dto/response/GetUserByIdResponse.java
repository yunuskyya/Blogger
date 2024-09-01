package com.blogger.backend.dto.response;

import lombok.Data;

@Data
public class GetUserByIdResponse {
    private int id;
    private String username;
    private String email;
    private String image;
}
