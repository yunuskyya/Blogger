package com.blogger.backend.service;

import com.blogger.backend.dto.request.CreatePostRequest;
import com.blogger.backend.model.Post;

public interface PostService {
    Post createPost(CreatePostRequest request, int userId);
}
