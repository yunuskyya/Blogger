package com.blogger.backend.exception;


import com.blogger.backend.shared.Messages;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(int id) {
        super(Messages.getMessageForLocale("blogger.user.notfound.error.message",
                LocaleContextHolder.getLocale(), id));
    }

    public UserNotFoundException(String username) {
        super(Messages.getMessageForLocale("blogger.user.notfound.error.message",
                LocaleContextHolder.getLocale(), username));
    }
}


