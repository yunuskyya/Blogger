package com.blogger.backend.controller;

import com.blogger.backend.dto.request.CreatePostRequest;
import com.blogger.backend.model.Post;
import com.blogger.backend.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.blogger.backend.constant.BloggerConstant.API_V1;

@RestController
@RequestMapping(API_V1 +"/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody CreatePostRequest request, @RequestParam int userId) {
        Post createdPost = postService.createPost(request, userId);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

}

