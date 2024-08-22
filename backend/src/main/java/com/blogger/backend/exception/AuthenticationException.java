package com.blogger.backend.exception;

import com.blogger.backend.shared.Messages;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String messageKey) {
        super(Messages.getMessageForLocale(messageKey, LocaleContextHolder.getLocale()));
    }
}



