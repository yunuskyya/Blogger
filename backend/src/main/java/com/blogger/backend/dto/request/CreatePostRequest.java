package com.blogger.backend.dto.request;

import lombok.Data;

@Data
public class CreatePostRequest {
    private String title;
    private String content;
}
