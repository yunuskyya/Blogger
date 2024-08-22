package com.blogger.backend.exception;

import com.blogger.backend.shared.Messages;
import org.springframework.context.i18n.LocaleContextHolder;

public class InvalidTokenException  extends RuntimeException{
    public InvalidTokenException() {
        super(Messages.getMessageForLocale("blogger.invalid.token.error.message", LocaleContextHolder.getLocale()));
    }
}
