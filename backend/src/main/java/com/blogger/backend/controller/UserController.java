package com.blogger.backend.controller;

import com.blogger.backend.dto.request.RegisterUserRequest;
import com.blogger.backend.service.UserService;
import com.blogger.backend.shared.GenericMessage;
import com.blogger.backend.shared.Messages;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/register")
    public GenericMessage registerUser(@Valid @RequestBody RegisterUserRequest request) {
        userService.registerUser(request);
        return new GenericMessage(Messages.getMessageForLocale("blogger.register.user.success.message.uccessfully",
                LocaleContextHolder.getLocale()));
    }
}
