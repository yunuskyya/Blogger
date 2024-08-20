package com.blogger.backend.exception;


import java.util.Collections;
import java.util.Map;

import com.blogger.backend.shared.Messages;
import org.springframework.context.i18n.LocaleContextHolder;


public class NotUniqueUsernameException extends RuntimeException{
    public NotUniqueUsernameException() {
        super(Messages.getMessageForLocale("blogger.validation.notunique.username", LocaleContextHolder.getLocale()));
    }

    public Map<String, String> getValidationErrors() {
        return Collections.singletonMap("username",
                Messages.getMessageForLocale("blogger.validation.notunique.username",
                        LocaleContextHolder.getLocale()));
    }
}

