package com.blogger.backend.exception;

import com.blogger.backend.shared.Messages;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED)
public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super(Messages.getMessageForLocale("blogger.authentication.error.message", LocaleContextHolder.getLocale()));
    }

}


