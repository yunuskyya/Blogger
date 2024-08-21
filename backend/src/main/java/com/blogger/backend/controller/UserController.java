package com.blogger.backend.controller;

import com.blogger.backend.dto.request.RegisterUserRequest;
import com.blogger.backend.service.UserService;
import com.blogger.backend.shared.GenericMessage;
import com.blogger.backend.shared.Messages;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
@AllArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/api/v1/users")
    public GenericMessage registerUser(@Valid @RequestBody RegisterUserRequest request) {
        userService.registerUser(request);
        return new GenericMessage(Messages.getMessageForLocale("blogger.register.user.success.message.successfully",
                LocaleContextHolder.getLocale()));
    }
}
