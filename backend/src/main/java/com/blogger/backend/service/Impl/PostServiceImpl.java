package com.blogger.backend.service.Impl;

import com.blogger.backend.dto.request.CreatePostRequest;
import com.blogger.backend.exception.UserNotFoundException;
import com.blogger.backend.model.Post;
import com.blogger.backend.model.User;
import com.blogger.backend.model.enums.Role;
import com.blogger.backend.repository.PostRepository;
import com.blogger.backend.repository.UserRepository;
import com.blogger.backend.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapperForRequest;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, @Qualifier("modelMapperForRequest") ModelMapper modelMapperForRequest) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.modelMapperForRequest = modelMapperForRequest;
    }

    @Override
    public Post createPost(CreatePostRequest request, int userId) {
        User user= userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found"));

        if (!user.getAuthorities().contains(Role.ROLE_AUTHOR)) {
            throw new RuntimeException("Yalnızca yazarlar yazı oluşturabilir");
        }
        Post post = modelMapperForRequest.map(request, Post.class);
        post.setAuthor(user);
        post.setCreatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }
}
