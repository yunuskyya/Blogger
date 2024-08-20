package com.blogger.backend.exception;

import com.blogger.backend.shared.Messages;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException() {
        super(Messages.getMessageForLocale("blogger.access.denied.error.message", LocaleContextHolder.getLocale()));
    }

}
